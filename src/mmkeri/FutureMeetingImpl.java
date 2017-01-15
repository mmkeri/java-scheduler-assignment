package mmkeri;

import java.util.Calendar;
import java.util.Set;

public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {

    public FutureMeetingImpl(int id, Calendar date, Set<Contact> contacts){
        super(id, date, contacts);
    }

    @Override
    public int hashCode() {
        return this.getDate().hashCode() ^ Integer.hashCode(this.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof FutureMeetingImpl))
            return false;
        FutureMeetingImpl other = (FutureMeetingImpl)obj;
        return other.getDate().equals(this.getDate()) && other.getId() == this.getId()
                && other.getContacts().equals(this.getContacts());
                //&& other.getNotes().equals(this.getNotes());
    }
}
