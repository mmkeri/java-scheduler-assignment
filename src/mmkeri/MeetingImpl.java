package mmkeri;

import java.util.Calendar;
import java.util.Set;

public abstract class MeetingImpl implements Meeting {

    protected int meetingId = 0;
    protected Calendar meetingDate = null;
    protected Set<Contact> meetingContacts = null;

    public MeetingImpl(int id, Calendar date, Set<Contact> contacts) throws IllegalArgumentException, NullPointerException{
        if(id <= 0) {
            throw new IllegalArgumentException();
        } else {
            this.meetingId = id;
        }
        if(date == null) {
            throw new NullPointerException();
        } else {
            this.meetingDate = date;
        }
        if(contacts == null){
            throw new NullPointerException();
        }else if(contacts.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            this.meetingContacts = contacts;
        }
    }
    public int getId() {
        return meetingId;
    }

    public Calendar getDate() {
        return meetingDate;
    }

    public Set<Contact> getContacts() {
        return meetingContacts;
    }

    @Override
    public int hashCode() {
       return this.getDate().hashCode() ^ Integer.hashCode(this.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof MeetingImpl))
            return false;
        MeetingImpl other = (MeetingImpl)obj;
        return other.getDate().equals(this.getDate()) && other.getId() == this.getId()
                && other.getContacts().equals(this.getContacts());
    }
}
