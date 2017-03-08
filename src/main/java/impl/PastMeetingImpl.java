package impl;

import spec.Contact;
import spec.PastMeeting;

import java.util.Calendar;
import java.util.Set;

/**
 * Created by mmker on 04-Jan-17.
 */
public final class PastMeetingImpl extends MeetingImpl implements PastMeeting {

    private String meetingNotes = "";

    public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts, String notes){
        super(id, date, contacts);
        //included to ensure that a null pointer exception is thrown if no argument given for notes
        notes.length();
        this.meetingNotes = notes;
    }


    @Override
    public String getNotes() {
        return meetingNotes;
    }

    @Override
    public int hashCode() {
        return this.getDate().hashCode() ^ Integer.hashCode(this.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof PastMeetingImpl)) {
            return false;
        }
        PastMeetingImpl other = (PastMeetingImpl)obj;
        return other.getDate().equals(this.getDate()) && other.getId() == this.getId()
                && other.getContacts().equals(this.getContacts())
                && other.getNotes().equals(this.getNotes());
    }
}
