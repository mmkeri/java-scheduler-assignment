package mmkeri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Collectors.*;
import java.util.Collections;

import mmkeri.DistinctByKeyPredicate.*;
import java.util.Calendar.*;
import java.util.stream.IntStream;

import static mmkeri.CondensedContactManagerInfo.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ContactManagerImpl implements ContactManager{

    private int contactId = 0;
    private int meetingId = 0;
    private Map<Integer , FutureMeeting> futureMeetingList = new HashMap<>();
    private Map<Integer, PastMeeting> pastMeetingList = new HashMap<>();
    private Map<Integer, Contact> contactList = new HashMap<>();
    private NavigableMap<Calendar, Meeting> meetingList = new TreeMap<>();
    private final IOProvider ioProvider;
    private static List<ContactManagerImpl> autoFlushManagers = new ArrayList<>();

    public ContactManagerImpl(){
        this(new RealIOProviderImpl(), Calendar.getInstance(), true /*autoFlush*/);
    }

    public ContactManagerImpl(IOProvider ioProvider, Calendar nowCalendar, boolean autoFlush){
        final long now = nowCalendar.getTimeInMillis();
        this.ioProvider = ioProvider;

        // read from contacts
        Reader reader = null;
        CondensedContactManagerInfo condensedInfo = null;
        try {
            reader = ioProvider.openReader("contacts.txt");
            condensedInfo = CondensedContactManagerInfo.unmarshal(reader);
        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
            // In case of file not found, default values / empty contact manager is created
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                }
            }
        }

        // reconstitute from condensed, if reading from store succeeded
        if (condensedInfo != null) {
            int maxContactId = Integer.MIN_VALUE,
                    maxMeetingId = Integer.MIN_VALUE;
            for (ContactInfo contactInfo : condensedInfo.contacts) {
                Contact contact = new ContactImpl(contactInfo.id, contactInfo.name, contactInfo.notes);
                contactList.put(contact.getId(), contact);
                maxContactId = Math.max(contactInfo.id, maxContactId);
            }
            maxContactId = Math.max(0, maxContactId);

            for (MeetingInfo meetingInfo : condensedInfo.meetings) {
                Set<Contact> attendees = new HashSet<>();
                for (Integer id : meetingInfo.contacts) {
                    attendees.add(contactList.get(id));
                }
                Calendar meetingTime = Calendar.getInstance();
                meetingTime.setTimeInMillis(meetingInfo.dateTime);

                if (meetingInfo.dateTime > now) {
                    FutureMeeting meeting = new FutureMeetingImpl(meetingInfo.id, meetingTime, attendees);
                    futureMeetingList.put(meeting.getId(), meeting);
                    meetingList.put(meetingTime, meeting);
                } else {
                    PastMeeting meeting = new PastMeetingImpl(meetingInfo.id, meetingTime, attendees, meetingInfo.notes);
                    pastMeetingList.put(meeting.getId(), meeting);
                    meetingList.put(meetingTime, meeting);
                }
                maxMeetingId = Math.max(meetingInfo.id, maxMeetingId);
            }
            maxMeetingId = Math.max(0, maxMeetingId);
            this.contactId = maxContactId;
            this.meetingId = maxMeetingId;
        }

        // manager is constructed correctly. If Autoflush was requested, remember that
        if (autoFlush) {
            autoFlushManagers.add(this);
        }
    }

    /** gets initialised when program starts / class is loaded */
    static {
        Thread shutdownHook = new Thread() {
            @Override
            public synchronized void start() {
                for(ContactManagerImpl manager : autoFlushManagers) {
                    try {
                        manager.flush();
                    } catch (Exception e) { }
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
        if(CheckDateEarlier.isDateEarlier(date)){
            throw new IllegalArgumentException();
        }
        for(Contact contact : contacts){
            if(!contactList.containsValue(contact)){
                throw new IllegalArgumentException();
            }
        }
        meetingId++;
        FutureMeeting newMeeting = new FutureMeetingImpl(meetingId, date, contacts);
        futureMeetingList.put(meetingId, newMeeting);
        meetingList.put(date, newMeeting);
        return meetingId;
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
        List<Meeting> result = futureMeetingList.entrySet()
                .stream()
                .map(x -> x.getValue())
                .filter(n -> n.getContacts().contains(contact))
                .filter(DistinctByKeyPredicate.distinctByKey((FutureMeeting p) -> p.getDate()))
                .sorted(Comparator.comparing((FutureMeeting m) -> m.getDate().getTimeInMillis()))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<Meeting> getMeetingListOn(Calendar date) {
        if(date == null){
            throw new NullPointerException();
        }
        Calendar startOfDay = Calendar.getInstance();
        Calendar endOfDay = Calendar.getInstance();
        startOfDay = SetStartOfDay.setStart(date);
        endOfDay = SetEndOfDay.setEnd(startOfDay, endOfDay);
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
        List<PastMeeting> result = pastMeetingList.entrySet()
                .stream()
                .map(x -> x.getValue())
                .filter(n -> n.getContacts().contains(contact))
                .filter(DistinctByKeyPredicate.distinctByKey((PastMeeting p) -> p.getDate()))
                .sorted(Comparator.comparing((PastMeeting m) -> m.getDate().getTimeInMillis()))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public int addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) throws IllegalArgumentException, NullPointerException{
        CheckDateEarlier testDate = new CheckDateEarlier();
        if(text == null){
            throw new NullPointerException();
        }
        if(date == null){
            throw new NullPointerException();
        }
        if(!testDate.isDateEarlier(date)){
            throw new IllegalArgumentException();
        }
        if(contacts == null){
            throw new NullPointerException();
        }
        if(contacts.isEmpty()){
            throw new IllegalArgumentException();
        }
        meetingId++;
        PastMeeting newMeeting = new PastMeetingImpl(meetingId, date, contacts, text);
        pastMeetingList.put(meetingId, newMeeting);
        meetingList.put(date, newMeeting);
        return meetingId;
    }

    @Override
    public PastMeeting addMeetingNotes(int id, String text) throws IllegalArgumentException, NullPointerException{
        if(text == null) {
            throw new NullPointerException();
        }
        if(!pastMeetingList.containsKey(id) && !futureMeetingList.containsKey(id)){
            throw new IllegalArgumentException();
        }
        Calendar startOfDay = Calendar.getInstance();
        Calendar endOfDay = Calendar.getInstance();
        startOfDay = SetStartOfDay.setStart(startOfDay);
        endOfDay = SetEndOfDay.setEnd(startOfDay, endOfDay);
        if(futureMeetingList.containsKey(id)) {
            if (futureMeetingList.get(id).getDate().getTimeInMillis() > endOfDay.getTimeInMillis()) {
                throw new IllegalStateException();
            }
            if (futureMeetingList.get(id).getDate().getTimeInMillis() > startOfDay.getTimeInMillis() &&
                    futureMeetingList.get(id).getDate().getTimeInMillis() < endOfDay.getTimeInMillis()) {
                FutureMeeting todaysMeeting = futureMeetingList.remove(id);
                PastMeeting newPastMeeting = new PastMeetingImpl(todaysMeeting.getId(), todaysMeeting.getDate(), todaysMeeting.getContacts(), text);
                pastMeetingList.put(id, newPastMeeting);
                return newPastMeeting;
            }
        }else if(pastMeetingList.containsKey(id)){
            PastMeeting oldMeeting = pastMeetingList.remove(id);
            PastMeeting newMeeting = new PastMeetingImpl(oldMeeting.getId(), oldMeeting.getDate(), oldMeeting.getContacts()
            , (oldMeeting.getNotes() + " ; " + text));
            return newMeeting;
        }
        return null;
    }

    @Override
    public int addNewContact(String name, String notes) throws IllegalArgumentException, NullPointerException{
        if(name == "" || notes == ""){
            throw new IllegalArgumentException();
        }
        if(name == null || notes == null){
            throw new NullPointerException();
        }
        contactId++;
        Contact newContact = new ContactImpl(contactId, name, notes);
        contactList.put(contactId, newContact);
        return contactId;
    }

    @Override
    public Set<Contact> getContacts(String name) throws NullPointerException{
        if(name == null){
            throw new NullPointerException();
        }
        if(name == ""){
            return contactList.entrySet()
                    .stream()
                    .map(x -> x.getValue())
                    .collect(Collectors.toSet());
        }else {
            Set<Contact> result = contactList.entrySet()
                    .stream()
                    .map(x -> x.getValue())
                    .filter(p -> p.getName() == name)
                    .collect(Collectors.toSet());
            return result;
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

    /*takes the contact and meeting lists and converts the objects within them to a
    * more condensed version that is easier to then write to an XML file
    */
    private CondensedContactManagerInfo computeCondensedVersion() {
        List<ContactInfo> contacts = new ArrayList<>();
        for (Contact contact : this.contactList.values()) {
            contacts.add(new ContactInfo(contact));
        }
        List<MeetingInfo> meetings = new ArrayList<>();
        for (Meeting meeting : this.meetingList.values()) {
            meetings.add(new MeetingInfo(meeting));
        }
        return new CondensedContactManagerInfo(contacts, meetings);
    }
}
