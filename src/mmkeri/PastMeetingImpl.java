package mmkeri;

import java.util.Calendar;
import java.util.Set;

/**
 * Created by mmker on 04-Jan-17.
 */
public class PastMeetingImpl extends MeetingImpl implements PastMeeting{

    private String meetingNotes = "";

    public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts, String notes){
        super(id, date, contacts);
        if(notes == null) {
            throw new NullPointerException();
        }else {
            this.meetingNotes = notes;
        }
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
        if(!(obj instanceof PastMeetingImpl))
            return false;
        PastMeetingImpl other = (PastMeetingImpl)obj;
        return other.getDate().equals(this.getDate()) && other.getId() == this.getId()
                && other.getContacts().equals(this.getContacts())
                && other.getNotes().equals(this.getNotes());
    }
}
