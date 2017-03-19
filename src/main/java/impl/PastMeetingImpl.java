package impl;

import spec.Contact;
import spec.PastMeeting;

import java.util.Calendar;
import java.util.Set;

/**
 * Created by mmker on 04-Jan-17.
 */

/**
 * A meeting object that represents meetings that have occurred in the past
 */
public final class PastMeetingImpl extends MeetingImpl implements PastMeeting, Comparable<PastMeeting> {
    /**
     * A String that contains any notes or comments about the meeting
     */
    private final String meetingNotes;

    /**
     * Initializes a new PastMeeting so that it contains an id value, date, contact and
     * notes
     * @param id an integer value that represents a unique id number for the meeting
     * @param date a Calendar object that represents the date the meeting occurred on
     * @param contacts a Set of Contact objects representing the attendees of the meeting
     * @param notes a String that contains any additional information about the meeting
     */
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

    /**
     * a unique number representing a specific PastMeetingImpl object using the date and id
     * for calculation
     * @return a unique hashcode as a signed integer value
     */
    @Override
    public int hashCode() {
        return this.getDate().hashCode() ^ Integer.hashCode(this.getId());
    }

    @Override
    public String toString() {
        return String.format("{%1$s:%2$s:%3$s}", getId(), getContacts(), getNotes());
    }

    /**
     * Tests whether the submitted instance of PastMeetingImpl is equal to that of another.
     * Equality is based whether the objects have equal dates, id numbers, Contact sets and
     * notes values.
     * @param obj
     * @return boolean result
     */
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

    @Override
    public int compareTo(PastMeeting o) {
        return this.getDate().compareTo(o.getDate());
    }
}
