package impl;

import spec.Contact;
import spec.Meeting;

import java.util.Calendar;
import java.util.Set;

public abstract class MeetingImpl implements Meeting {

    private int meetingId = 0;
    private Calendar meetingDate = null;
    private Set<Contact> meetingContacts = null;

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
