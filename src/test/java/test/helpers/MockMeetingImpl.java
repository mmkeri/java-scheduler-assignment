package test.helpers;

import impl.MeetingImpl;
import spec.Contact;

import java.util.Set;
import java.util.Calendar;

public class MockMeetingImpl extends MeetingImpl {

    /**
     * Instantiates a simple version of the Meeting interface for the purpose of testing the base
     * functions outlined in the class interface
     * @param id an integer value that represents a unique identifier for the meeting
     * @param date a Calendar object that represents the date that the meeting is scheduled for
     * @param contacts a Set of Contact objects representing the attendees of the meeting
     */
    public MockMeetingImpl(int id, Calendar date, Set<Contact> contacts){
        super(id, date, contacts);
    }
}
