package test;

import spec.Contact;
import impl.ContactImpl;
import impl.MeetingImpl;
import test.helpers.MockMeetingImpl;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;

public class MeetingImplShould {

    Calendar testCalendar = null;
    Calendar nullCalendar = null;
    Set<Contact> testContactList = null;
    Set<Contact> nullContactList = null;
    Contact testContact = null;
    MeetingImpl testMeeting = null;
    Set<Contact> emptyContactList = null;

    @Before public void setUp(){
        testCalendar = Calendar.getInstance();
        testCalendar.set(2017, 0, 15, 12, 25);
        testContact = new ContactImpl(1, "test Contact", "this is a test");
        testContactList = new HashSet<>();
        testContactList.add(testContact);
        testMeeting = new MockMeetingImpl(5, testCalendar, testContactList);
        emptyContactList = new HashSet<>();
    }

    @After public void cleanUp(){
        testCalendar = null;
        testContactList = null;
        testContact = null;
        testMeeting = null;
    }

    @Test
    public void returnTheCorrectValueOfTheMeetingId(){
        int expected = 5;
        int output = testMeeting.getId();
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwAnIllegalArgumentExceptionIfGivenAZeroValueForMeetingId(){
        MeetingImpl innerTestMeeting = new MockMeetingImpl(0, testCalendar, testContactList);
    }

    @Test (expected = IllegalArgumentException.class)
    public void throwAnIllegalArgumentExceptionIfGivenANegativeValueForMeetingId(){
        MeetingImpl innerTestMeeting = new MockMeetingImpl(-5, testCalendar, testContactList);
    }

    @Test (expected = IllegalArgumentException.class)
    public void throwAnIllegalArgumentExceptionIfTheSetOfContactsIsEmptyWhenPassedToTheConstructor(){
        MeetingImpl innerTestMeeting = new MockMeetingImpl(5, testCalendar,emptyContactList);
    }

    @Test
    public void returnTheCorrectDateForAMeeting(){
        Calendar expected = Calendar.getInstance();
        expected.set(2017, 0, 15, 12, 25);
        long expectedTimeInMilliseconds = expected.getTimeInMillis();
        Calendar output = testMeeting.getDate();
        long outputTimeInMilliseconds = output.getTimeInMillis();
        assertTrue("Dates are not close enough to be considered equal", Math.abs(expectedTimeInMilliseconds - outputTimeInMilliseconds) < 10);
    }

    @Test(expected = NullPointerException.class)
    public void throwANullPointerExceptionIfTheDateObjectIsNullWhenPassedToTheConstructor(){
        MeetingImpl innerTestMeeting = new MockMeetingImpl(5, nullCalendar, testContactList);
    }

    @Test(expected = NullPointerException.class)
    public void throwANullPointerExceptionIfTheContactSetIsNullWhenPassedToTheConstructor(){
        MeetingImpl innerTestMeeting = new MockMeetingImpl(5, testCalendar, nullContactList);
    }

    @Test
    public void returnTheDetailsOfThePeopleThatWereAtTheMeeting(){
        Contact test1 = new ContactImpl(1, "test", "test");
        Contact test2 = new ContactImpl(2, "tests", "tests");
        Contact test3 = new ContactImpl(3, "tested", "tested");
        Set<Contact> expected = new HashSet<>();
        expected.add(test1);
        expected.add(test2);
        expected.add(test3);
        Set<Contact> testList = new HashSet<>();
        testList.add(test1);
        testList.add(test2);
        testList.add(test3);
        MeetingImpl innerTestMeeting = new MockMeetingImpl(5, testCalendar, testList);
        Set<Contact> output = innerTestMeeting.getContacts();
        assertEquals(expected, output);

    }
}
