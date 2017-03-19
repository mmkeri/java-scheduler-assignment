package test;

import impl.*;
import spec.Contact;
import spec.ContactManager;
import spec.IOProvider;
import spec.Meeting;
import org.junit.*;
import test.helpers.MockIOProviderImpl;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

/**
 * Created by mmker on 08-Jan-17.
 */
public class ContactManagerImplConstructor {
    IOProvider originalIOProvider;
    CurrentTimeProvider originalTimeProvider;
    boolean originalAutoFlushSetting;

    Calendar mockNow;
    MockIOProviderImpl commonTestProvider = null;
    Contact contactHarry = new ContactImpl(1, "Harry", "Needs a barber");
    Contact contactSally = new ContactImpl(2, "Sally", "Met Harry");
    Set<Contact> testContacts = new HashSet<>();
    Calendar testCalendar1 = Calendar.getInstance();
    Calendar testCalendar2 = Calendar.getInstance();
    Meeting testPastMeeting = null;
    Meeting testFutureMeeting = null;

    @Before
    public void setUp(){
        originalAutoFlushSetting = ContactManagerImpl.getAutoFlushEnabled();
        originalIOProvider = ContactManagerImpl.getCurrentIOProvider();
        originalTimeProvider = ContactManagerImpl.getCurrentTimeProvider();


        mockNow = Calendar.getInstance();
        // for purpose of testing, "now" is always 01 Jan 2017
        mockNow.set(2017, Calendar.JANUARY, 1);
        commonTestProvider = new MockIOProviderImpl();

        ContactManagerImpl.setAutoFlushEnabled(false);
        ContactManagerImpl.setCurrentIOProvider(commonTestProvider);
        ContactManagerImpl.setCurrentTimeProvider(() -> mockNow);

        testContacts.add(contactHarry);
        testContacts.add(contactSally);
        testCalendar1.setTimeInMillis(1428142200000L);
        testPastMeeting = new PastMeetingImpl(1, testCalendar1, testContacts, "Harry REALLY liked Sally.");
        testCalendar2.setTimeInMillis(1518600600000L);
        testFutureMeeting = new FutureMeetingImpl(2, testCalendar2, testContacts);
    }

    @After
    public void cleanUp(){
        mockNow = null;
        commonTestProvider = null;
        ContactManagerImpl.setAutoFlushEnabled(originalAutoFlushSetting);
        ContactManagerImpl.setCurrentIOProvider(originalIOProvider);
        ContactManagerImpl.setCurrentTimeProvider(originalTimeProvider);
    }

    class SampleTextProvider implements IOProvider {
        public final List<Map.Entry<String, TestReader>> openCalls = new ArrayList<>();
        private final String text;

        public SampleTextProvider(String text) { this.text = text; }

        class TestReader extends StringReader {
            public boolean wasCloseCalled = false;
            public TestReader() {
                super(text);
            }

            @Override
            public void close() {
                wasCloseCalled = true;
                super.close();
            }
        }

        @Override
        public Reader openReader(String path) throws FileNotFoundException {
            TestReader reader = new TestReader();
            openCalls.add(new AbstractMap.SimpleEntry<>(path, reader));
            return reader;
        }

        @Override
        public Writer openWriter(String path) throws IOException {
            throw new AssertionError("Writing is not covered in this testcase");
        }
    };

    @Test
    public void verifyThatWhenTheConstructorIsCalledItOpensAndClosesContactsTextFile(){

        SampleTextProvider testCaseProvider = new SampleTextProvider(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<condensedContactManagerInfo/>\n");

        ContactManagerImpl.setCurrentIOProvider(testCaseProvider);
        ContactManager testContactManager = new ContactManagerImpl();

        assertEquals("Must attempt to open a file!",
                1, testCaseProvider.openCalls.size());
        assertEquals("file name requested must be contacts.txt",
                "contacts.txt", testCaseProvider.openCalls.get(0).getKey());
        assertTrue("Reader must be closed before end of construction",
                testCaseProvider.openCalls.get(0).getValue().wasCloseCalled);
    }

    @Test
    public void verifyConstructionWhenProvidedWithTwoSavedContacts() {

        SampleTextProvider testCaseProvider = new SampleTextProvider(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<condensedContactManagerInfo>\n" +
                        "    <contacts id=\"1\" name=\"Harry\" notes=\"Needs a barber\"/>\n" +
                        "    <contacts id=\"2\" name=\"Sally\" notes=\"Met Harry\"/>\n" +
                        "</condensedContactManagerInfo>\n");

        ContactManagerImpl.setCurrentIOProvider(testCaseProvider);

        ContactManager testContactManager = new ContactManagerImpl();

        assertEquals("Must attempt to open a file!",
                1, testCaseProvider.openCalls.size());
        assertEquals("file name requested must be contacts.txt",
                "contacts.txt", testCaseProvider.openCalls.get(0).getKey());
        assertTrue("Reader must be closed before end of construction",
                testCaseProvider.openCalls.get(0).getValue().wasCloseCalled);
        assertEquals(contactHarry, testContactManager.getContacts(1).iterator().next());
        assertEquals(contactSally, testContactManager.getContacts(2).iterator().next());
        try {
            testContactManager.getContacts(3);
            Assert.fail("Contact does not exist. Expected exception");
        } catch (IllegalArgumentException e){};
    }

    @Test
    public void verifyConstructionWhenProvidedWithPastAndFutureMeetings() {

        SampleTextProvider testCaseProvider = new SampleTextProvider(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<condensedContactManagerInfo>\n" +
                        "    <contacts id=\"1\" name=\"Harry\" notes=\"Needs a barber\"/>\n" +
                        "    <contacts id=\"2\" name=\"Sally\" notes=\"Met Harry\"/>\n" +
                        "    <meetings id=\"1\" dateTime=\"1428142200000\" contacts=\"2 1\" notes=\"Harry REALLY liked Sally.\"/>\n" +
                        "    <meetings id=\"2\" dateTime=\"1518600600000\" contacts=\"2 1\" notes=\"\"/>\n" +
                        "</condensedContactManagerInfo>\n");

        ContactManagerImpl.setCurrentIOProvider(testCaseProvider);
        ContactManager testContactManager = new ContactManagerImpl();

        assertEquals("Must attempt to open a file!",
                1, testCaseProvider.openCalls.size());
        assertEquals("file name requested must be contacts.txt",
                "contacts.txt", testCaseProvider.openCalls.get(0).getKey());
        assertTrue("Reader must be closed before end of construction",
                testCaseProvider.openCalls.get(0).getValue().wasCloseCalled);
        assertEquals(contactHarry, testContactManager.getContacts(1).iterator().next());
        assertEquals(contactSally, testContactManager.getContacts(2).iterator().next());
        try{
            testContactManager.getContacts(3);
            Assert.fail("Contact does not exist. Expected exception");
        } catch (IllegalArgumentException e){};
        assertEquals(testPastMeeting, testContactManager.getPastMeeting(1));
        assertEquals(testFutureMeeting, testContactManager.getFutureMeeting(2));
    }
}
