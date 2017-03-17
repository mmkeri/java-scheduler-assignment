package impl;

import spec.Contact;
import spec.Meeting;

import java.util.Calendar;
import java.util.Set;

public abstract class MeetingImpl implements Meeting {
    /**
     * unique user Id number as an integer value
     */
    private int meetingId = 0;
    /**
     * date associated with the meeting as a Calendar object
     */
    private Calendar meetingDate = null;
    /**
     * a set of Contact objects representing the different people who will be attending the meeting
     */
    private Set<Contact> meetingContacts = null;

    /**
     * Constructor of a Meeting object that accepts an id, date and contacts
     * @param id integer value representing a unique identifier for the meeting
     * @param date Calendar item representing the date the meeting will take place
     * @param contacts a set of Contact objects representing the people who will attending the meeting
     */
    public MeetingImpl(int id, Calendar date, Set<Contact> contacts){
        if(id <= 0) {
            throw new IllegalArgumentException();
        }
        if(contacts.isEmpty()) {
            throw new IllegalArgumentException();
        }

        // use the date and contacts parameters because the spec says that we have to trigger a null pointer exception
        // during construction
        date.getTimeInMillis();
        contacts.isEmpty();

        this.meetingId = id;
        this.meetingDate = date;
        this.meetingContacts = contacts;
    }
    public final int getId() {
        return meetingId;
    }

    public final Calendar getDate() {
        return meetingDate;
    }

    public final Set<Contact> getContacts() {
        return meetingContacts;
    }
}
