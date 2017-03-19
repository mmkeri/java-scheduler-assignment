package impl;

import spec.Contact;
import spec.FutureMeeting;

import java.util.Calendar;
import java.util.Set;

public final class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {

    /**
     * Constructor method that takes an id, date and a set of Contact objects and
     * generates a FutureMeeting object
     * @param id integer value that is a unique identifier for this meeting
     * @param date Calendar object that represents the date of when the meeting will occur
     * @param contacts a Set of Contact objects that are associated with this meeting
     */
    public FutureMeetingImpl(int id, Calendar date, Set<Contact> contacts){
        super(id, date, contacts);
    }

    /**
     * Generates a unique hashcode based on the date of the meeting and the Id number for the
     * particulr meeting
     * @return a unique hashcode
     */
    @Override
    public int hashCode() {
        return this.getDate().hashCode() ^ Integer.hashCode(this.getId());
    }

    /**
     * Tests whether the submitted instance of FutureMeeting is equal to that of another.
     * Equality is based whether the objects have equal dates, id numbers and Contact sets.
     * @param obj specifically a FutureMeetingImpl.
     * @return boolean result.
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof FutureMeetingImpl)) {
            return false;
        }
        FutureMeetingImpl other = (FutureMeetingImpl)obj;
        return other.getDate().equals(this.getDate()) && other.getId() == this.getId()
                && other.getContacts().equals(this.getContacts());
    }

    /**
     * Produces a string based on the objects ID, Date and Contacts
     * @return a String
     */
    @Override
    public String toString() {
        return String.format("{%1$s:%2$tc:%3$s}", getId(), getDate(), getContacts());
    }
}
