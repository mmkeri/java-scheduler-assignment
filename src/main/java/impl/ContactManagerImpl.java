package impl;

import spec.*;

import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;


public class ContactManagerImpl implements ContactManager {
    private static final List<ContactManagerImpl> autoFlushManagers = new ArrayList<>();

    // Normally, we would provide these as constructor arguments, but according to assignment specifications,
    // we cannot create overloads that accept these as parameters :(
    private static IOProvider currentIOProvider = new RealIOProviderImpl();
    private static CurrentTimeProvider currentTimeProvider = Calendar::getInstance;
    private static boolean autoFlushEnabled = false;

    private final IOProvider ioProvider;
    private final Calendar nowCalendar;
    private final Map<Integer, Contact> contactList = new HashMap<>();
    private final Map<Integer , FutureMeeting> futureMeetingList = new HashMap<>();
    private final Map<Integer, PastMeeting> pastMeetingList = new HashMap<>();
    private final NavigableMap<Calendar, Meeting> meetingList = new TreeMap<>();

    private int lastCreatedContactId = 0;
    private int lastCreatedMeetingId = 0;

    /**
     *
     */
    public ContactManagerImpl() {
        boolean autoFlush = ContactManagerImpl.autoFlushEnabled;
        this.nowCalendar = currentTimeProvider.getNow();
        this.ioProvider = currentIOProvider;

        // read from contacts
        CondensedContactManagerInfo condensedInfo = CondensedContactManagerInfo.unmarshalFromFile(ioProvider, "contacts.txt");

        // suggestion: extract to helper method
        // reconstitute from condensed, if reading from store succeeded
        if (condensedInfo != null) {
            this.lastCreatedContactId = initialiseContactsFromCondensedInfo(condensedInfo);
            this.lastCreatedMeetingId = initialiseMeetingsFromCondensedInfo(condensedInfo);
        }

        // manager is constructed correctly. If Autoflush was requested, remember that
        if (autoFlush) {
            autoFlushManagers.add(this);
        }
    }

    /**
     * Used during construction, to initialize the contact data structures given the provided condensedInfo
     * @param condensedInfo the object used for flushing to/from disk
     * @return the last created contact Id
     */
    private int initialiseContactsFromCondensedInfo(CondensedContactManagerInfo condensedInfo) {
        int maxContactId = Integer.MIN_VALUE;
        for (CondensedContactManagerInfo.ContactInfo contactInfo : condensedInfo.getContacts()) {
            Contact contact = new ContactImpl(contactInfo.getId(), contactInfo.getName(), contactInfo.getNotes());
            contactList.put(contact.getId(), contact);
            maxContactId = Math.max(contactInfo.getId(), maxContactId);
        }
        return Math.max(0, maxContactId);
    }

    /**
     * Used during construction, to initialize the meeting data structures given the provided condensedInfo
     * @param condensedInfo the object used for flushing to/from disk
     * @return the last created meeting Id
     */
    private int initialiseMeetingsFromCondensedInfo(CondensedContactManagerInfo condensedInfo) {
        final long now = this.nowCalendar.getTimeInMillis();
        int maxMeetingId = Integer.MIN_VALUE;

        for (CondensedContactManagerInfo.MeetingInfo meetingInfo : condensedInfo.getMeetings()) {
            Set<Contact> attendees = new HashSet<>();
            for (Integer id : meetingInfo.getContacts()) {
                attendees.add(contactList.get(id));
            }
            Calendar meetingTime = Calendar.getInstance();
            meetingTime.setTimeInMillis(meetingInfo.getDateTime());

            if (meetingInfo.getDateTime() > now) {
                FutureMeeting meeting = new FutureMeetingImpl(meetingInfo.getId(), meetingTime, attendees);
                futureMeetingList.put(meeting.getId(), meeting);
                meetingList.put(meetingTime, meeting);
            } else {
                PastMeeting meeting = new PastMeetingImpl(meetingInfo.getId(), meetingTime, attendees, meetingInfo.getNotes());
                pastMeetingList.put(meeting.getId(), meeting);
                meetingList.put(meetingTime, meeting);
            }
            maxMeetingId = Math.max(meetingInfo.getId(), maxMeetingId);
        }
        return Math.max(0, maxMeetingId);
    }


    /** gets initialised when program starts / class is loaded */
    static {
        Thread shutdownHook = new Thread() {
            @Override
            public synchronized void start() {
                for(ContactManagerImpl manager : autoFlushManagers) {
                    try {
                        manager.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) throws IllegalArgumentException, NullPointerException {
        if(date == null){
            throw new NullPointerException();
        }
        if(date.before(this.nowCalendar)){
            throw new IllegalArgumentException();
        }
        for(Contact contact : contacts){
            if(!contactList.containsValue(contact)){
                throw new IllegalArgumentException();
            }
        }
        // new id is last + 1
        int newMeetingId = lastCreatedMeetingId + 1;

        FutureMeeting newMeeting = new FutureMeetingImpl(newMeetingId, date, contacts);
        futureMeetingList.put(newMeetingId, newMeeting);
        meetingList.put(date, newMeeting);

        // now record that we've used this ID
        lastCreatedMeetingId = newMeetingId;
        return newMeetingId;
    }

    @Override
    public PastMeeting getPastMeeting(int id) throws IllegalStateException{
        if(futureMeetingList.containsKey(id)){
            throw new IllegalStateException();
        }
        if(!pastMeetingList.containsKey(id)){
            return null;
        } else {
            return pastMeetingList.get(id);
        }
    }

    @Override
    public FutureMeeting getFutureMeeting(int id) throws IllegalStateException{
        if(pastMeetingList.containsKey(id)){
            throw new IllegalStateException();
        }
        if(!futureMeetingList.containsKey(id)){
            return null;
        }else {
            return futureMeetingList.get(id);
        }
    }

    @Override
    public Meeting getMeeting(int id) {
        if(pastMeetingList.containsKey(id)) {
            return pastMeetingList.get(id);
        } else if (futureMeetingList.containsKey(id)) {
            return futureMeetingList.get(id);
        }else{
            return null;
        }
    }

    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) throws IllegalArgumentException, NullPointerException{
        if(contact == null){
            throw new NullPointerException();
        }
        if(!contactList.containsValue(contact)){
            throw new IllegalArgumentException();
        }
        List<Meeting> result = new ArrayList<>();
        for (Meeting meeting: this.meetingList.tailMap(this.nowCalendar).values()) {
            if (meeting.getContacts().contains(contact)) {
                result.add(meeting);
            }
        }
        return result;
    }

    @Override
    public List<Meeting> getMeetingListOn(Calendar date) {
        if(date == null){
            throw new NullPointerException();
        }
        Calendar startOfDay = Calendar.getInstance();
        startOfDay.setTimeInMillis(date.getTimeInMillis());
        startOfDay.set(Calendar.HOUR_OF_DAY, 0);
        startOfDay.set(Calendar.MINUTE, 00);
        startOfDay.set(Calendar.SECOND, 0);
        startOfDay.set(Calendar.MILLISECOND, 0);

        Calendar endOfDay = Calendar.getInstance();
        endOfDay.setTimeInMillis(startOfDay.getTimeInMillis());
        endOfDay.add(Calendar.DATE, 1);

        return new ArrayList<>(this.meetingList.subMap(startOfDay, true, endOfDay, false).values());
    }

    @Override
    public List<PastMeeting> getPastMeetingListFor(Contact contact) throws IllegalArgumentException, NullPointerException{
        if(contact == null){
            throw new NullPointerException();
        }
        if(!contactList.containsValue(contact)){
            throw new IllegalArgumentException();
        }

        List<PastMeeting> result = new ArrayList<>();
        for (Meeting meeting: this.meetingList.headMap(this.nowCalendar).values()) {
            if (meeting.getContacts().contains(contact)) {
                result.add((PastMeeting) meeting);
            }
        }
        return result;
    }

    @Override
    public int addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) throws IllegalArgumentException, NullPointerException{
        if(text == null){
            throw new NullPointerException();
        }
        if(date == null){
            throw new NullPointerException();
        }
        if(date.after(this.nowCalendar)){
            throw new IllegalArgumentException();
        }
        if(contacts == null){
            throw new NullPointerException();
        }
        if(contacts.isEmpty()){
            throw new IllegalArgumentException();
        }

        int newMeetingId = lastCreatedMeetingId + 1;

        PastMeeting newMeeting = new PastMeetingImpl(newMeetingId, date, contacts, text);
        pastMeetingList.put(newMeetingId, newMeeting);
        meetingList.put(date, newMeeting);

        // now record that we've used this ID
        lastCreatedMeetingId = newMeetingId;
        return newMeetingId;
    }

    @Override
    public PastMeeting addMeetingNotes(int id, String text) throws IllegalArgumentException, NullPointerException{
        if(text == null) {
            throw new NullPointerException();
        }
        if(futureMeetingList.containsKey(id)) {
            throw new IllegalStateException();
        } else if(pastMeetingList.containsKey(id)){
            PastMeeting oldMeeting = pastMeetingList.remove(id);
            PastMeeting newMeeting = new PastMeetingImpl(oldMeeting.getId(),
                    oldMeeting.getDate(),
                    oldMeeting.getContacts(),
                    oldMeeting.getNotes() + " ; " + text);
            pastMeetingList.put(id, newMeeting);
            meetingList.put(newMeeting.getDate(), newMeeting);
            return newMeeting;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public int addNewContact(String name, String notes) throws IllegalArgumentException, NullPointerException{
        if(name == null || notes == null){
            throw new NullPointerException();
        }
        if(name.equals("") || notes.equals("")){
            throw new IllegalArgumentException();
        }
        int newContactId = lastCreatedContactId + 1;
        lastCreatedContactId = newContactId;

        Contact newContact = new ContactImpl(newContactId, name, notes);
        contactList.put(newContactId, newContact);
        return newContactId;
    }

    @Override
    public Set<Contact> getContacts(String name) throws NullPointerException{
        if(name == null){
            throw new NullPointerException();
        }
        if(name.equals("")){
            return new HashSet<>(contactList.values());
        }else {
            // Suggestion: we can speed this up to O(1) if we add an index by name
            return contactList.values().stream()
                    .filter(p -> p.getName().equals(name))
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public Set<Contact> getContacts(int... ids) throws IllegalArgumentException{
        if(ids.length == 0) {
            throw new IllegalArgumentException();
        }
        Set<Contact> retrievedContacts = new HashSet<>();
        for(int id : ids){
            Contact result = contactList.get(id);
            if(result == null){
                throw new IllegalArgumentException();
            } else {
                retrievedContacts.add(result);
            }
        }
        return retrievedContacts;
    }

    @Override
    public void flush() {
        Writer writer = null;
        CondensedContactManagerInfo condensedInfo = this.computeCondensedVersion();
        try {
            writer = this.ioProvider.openWriter("contacts.txt");
            condensedInfo.marshal(writer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // not much we can do here if this is already cascading
                    // due to an earlier error
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * takes the contact and meeting lists and converts the objects within them to a
     * more condensed version that is easier to then write to an XML file
     * @return a CondensedContactManagerInfo object 
     */
    private CondensedContactManagerInfo computeCondensedVersion() {
        List<CondensedContactManagerInfo.ContactInfo> contacts = new ArrayList<>();
        for (Contact contact : this.contactList.values()) {
            contacts.add(new CondensedContactManagerInfo.ContactInfo(contact));
        }
        List<CondensedContactManagerInfo.MeetingInfo> meetings = new ArrayList<>();
        for (Meeting meeting : this.meetingList.values()) {
            meetings.add(new CondensedContactManagerInfo.MeetingInfo(meeting));
        }
        return new CondensedContactManagerInfo(contacts, meetings);
    }

    public static boolean getAutoFlushEnabled() {
        return autoFlushEnabled;
    }
    public static void setAutoFlushEnabled(boolean autoFlushEnabled) {
        ContactManagerImpl.autoFlushEnabled = autoFlushEnabled;
    }

    public static CurrentTimeProvider getCurrentTimeProvider() {
        return currentTimeProvider;
    }
    public static void setCurrentTimeProvider(CurrentTimeProvider timeProvider) {
        currentTimeProvider = timeProvider;
    }

    public static IOProvider getCurrentIOProvider() {
        return currentIOProvider;
    }
    public static void setCurrentIOProvider(IOProvider ioProvider) {
        currentIOProvider = ioProvider;
    }
}
