package impl;

import spec.Contact;

import java.util.Set;
import java.util.Calendar;

public class MockMeetingImpl extends MeetingImpl {

    /*
    Created for the purpose of testing the Meeting class
     */
    public MockMeetingImpl(int id, Calendar date, Set<Contact> contacts){
        super(id, date, contacts);
    }
}
