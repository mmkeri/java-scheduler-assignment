package test;

import impl.ContactImpl;
import impl.MeetingImpl;
import test.helpers.MockMeetingImpl;
import impl.PastMeetingImpl;
import spec.Contact;
import spec.PastMeeting;
import org.junit.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;

public class PastMeetingImplShould {

    Calendar testCalendar = null;
    Calendar nullCalendar = null;
    Set<Contact> testContactList = null;
    Set<Contact> nullContactList = null;
    Contact testContact = null;
    MeetingImpl testMeeting = null;
    Set<Contact> emptyContactList = null;
    PastMeeting testPastMeeting = null;

    @Before public void setUp(){
        testCalendar = Calendar.getInstance();
        testCalendar.set(2017, 0, 15, 12, 25);
        testContact = new ContactImpl(1, "test Contact", "this is a test");
        testContactList = new HashSet<>();
        testContactList.add(testContact);
        testMeeting = new MockMeetingImpl(5, testCalendar, testContactList);
        emptyContactList = new HashSet<>();
        testPastMeeting = new PastMeetingImpl(5, testCalendar, testContactList, "notes from meeting");
    }

    @After public void cleanUp(){
        testCalendar = null;
        testContactList = null;
        testContact = null;
        testMeeting = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwAnIllegalArgumentExceptionIfAZeroIsGivenForMeetingId(){
        PastMeeting testMeeting = new PastMeetingImpl(0, testCalendar, testContactList, "boring");
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwAnIllegalArgumentExceptionIfANegativeNumberIsGivenForMeetingId(){
        PastMeeting testMeeting = new PastMeetingImpl(-5, testCalendar, testContactList, "boring");
    }

    @Test(expected = NullPointerException.class)
    public void throwANullPointerExceptionIfANullStringIsPassedToTheConstructorForTheNotesParameter(){
        String nullString = null;
        PastMeeting testMeeting = new PastMeetingImpl(5, testCalendar, testContactList, nullString);
    }

    @Test
    public void returnTheNotesFromTheMeetingThatHaveBeenEntered(){
        String expected = "notes from meeting";
        String output = testPastMeeting.getNotes();
        assertEquals(expected, output);
    }

    @Test
    public void returnAnEmptyStringIfThatIsWhatIsPassedToTheConstructorForTheNotesParameter(){
        String expected = "";
        PastMeeting testMeeting = new PastMeetingImpl(5, testCalendar, testContactList, "");
        String output = testMeeting.getNotes();
        assertEquals(expected, output);
    }
}
