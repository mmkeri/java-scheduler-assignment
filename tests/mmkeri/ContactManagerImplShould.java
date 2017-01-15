package mmkeri;

import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class ContactManagerImplShould {
    Calendar mockNow;
    Calendar testCalendar = null;
    Calendar testCalendar2 = null;
    Calendar testCalendar3 = null;
    Calendar testCalendar4 = null;
    Calendar testCalendar5 = null;
    Calendar testCalendar6 = null;
    Calendar testCalendar7 = null;
    Calendar testCalendar8 = null;
    Calendar nullCalendar = null;
    Contact contact1 = null;
    Contact contact2 = null;
    Contact contact3 = null;
    Contact contact4 = null;
    Contact contact5 = null;
    Contact contact6 = null;
    Contact contact7 = null;
    Contact contact8 = null;
    Contact nullContact = null;
    Set<Contact> testSet = null;
    Set<Contact> testSet2 = null;
    Set<Contact> emptySet = null;
    Set<Contact> nullSet = null;
    ContactManager testContactManager = null;
    ContactManager testContactManager2 = null;
    PastMeeting testPastMeeting = null;
    PastMeeting testPastMeeting2 = null;
    PastMeeting testPastMeeting3 = null;
    FutureMeeting testFutureMeeting = null;
    FutureMeeting secondTestFutureMeeting = null;
    FutureMeeting testFutureMeeting3 = null;
    FutureMeeting testFutureMeeting4 = null;
    FutureMeeting testFutureMeeting5 = null;
    FutureMeeting testFutureMeeting6 = null;
    String nullString = null;
    String emptyString = "";
    MockIOProviderImpl testIOProvider = null;

    @Before public void setUp(){
        // for purpose of testing, "now" is always 01 Jan 2017
        mockNow = Calendar.getInstance();
        mockNow.set(2017, Calendar.JANUARY, 1);

        contact1 = new ContactImpl(1, "Harry", "Needs a barber");
        contact2 = new ContactImpl(2, "Sally", "Met Harry");
        contact3 = new ContactImpl(3, "Joe", "Just average");
        contact4 = new ContactImpl(4, "Harry", "Still hirsuite");
        contact5 = new ContactImpl(5, "Harry", "Just shave");
        contact6 = new ContactImpl(1, "Harry", "Just shave");
        contact7 = new ContactImpl(2, "Harry", "Still hirsuite");
        contact8 = new ContactImpl(3, "Harry", "Needs a barber");
        testSet = new HashSet<>();
        testSet2 = new HashSet<>();
        emptySet = new HashSet<>();
        testSet.add(contact1);
        testSet.add(contact2);
        testSet.add(contact3);
        testSet2.add(contact1);
        testSet2.add(contact3);
        testCalendar = Calendar.getInstance();
        testCalendar.set(2017, Calendar.JANUARY, 5, 12, 30);
        testCalendar2 = Calendar.getInstance();
        testCalendar2.set(2016, Calendar.FEBRUARY, 2, 13, 45);
        testCalendar3 = Calendar.getInstance();
        testCalendar3.set(2017, Calendar.JUNE, 5, 11, 15);
        testCalendar4 = Calendar.getInstance();
        testCalendar4.setTimeInMillis(1518600600000L);
        testCalendar5 = Calendar.getInstance();
        testCalendar5.set(2017, Calendar.OCTOBER, 31, 11, 45);
        testCalendar6 = Calendar.getInstance();
        testCalendar6.set(2017, Calendar.JUNE, 5, 16, 00);
        testCalendar7 = Calendar.getInstance();
        testCalendar7.set(2017, Calendar.JUNE, 5, 15, 20);
        testCalendar8 = Calendar.getInstance();
        testCalendar8.setTimeInMillis(1428142200000L);
        testIOProvider = new MockIOProviderImpl();
        testContactManager = new ContactManagerImpl(testIOProvider, mockNow, false);
        testContactManager2 = new ContactManagerImpl(testIOProvider, mockNow, false);
        testPastMeeting = new PastMeetingImpl(1, testCalendar2, testSet, "note");
        testPastMeeting2 = new PastMeetingImpl(2, testCalendar8, testSet, "note");
        testPastMeeting3 = new PastMeetingImpl(3, testCalendar, testSet, "note");
        testFutureMeeting = new FutureMeetingImpl(1, testCalendar3, testSet);
        secondTestFutureMeeting = new FutureMeetingImpl(3, testCalendar3, testSet2);
        testFutureMeeting3 = new FutureMeetingImpl(2, testCalendar4, testSet);
        testFutureMeeting4 = new FutureMeetingImpl(4, testCalendar5, testSet);
        testFutureMeeting5 = new FutureMeetingImpl(5, testCalendar6, testSet);
        testFutureMeeting6 = new FutureMeetingImpl(6, testCalendar7, testSet);
    }

    @After public void cleanUp(){
        contact1 = null;
        contact2 = null;
        contact3 = null;
        contact4 = null;
        contact5 = null;
        contact6 = null;
        contact7 = null;
        contact8 = null;
        testSet = null;
        testSet2 = null;
        emptySet = null;
        testCalendar = null;
        testCalendar2 = null;
        testCalendar3 = null;
        testCalendar4 = null;
        testCalendar5 = null;
        testCalendar6 = null;
        testCalendar7 = null;
        testCalendar8 = null;
        testPastMeeting = null;
        testPastMeeting2 = null;
        testPastMeeting3 = null;
        testFutureMeeting = null;
        secondTestFutureMeeting = null;
        testFutureMeeting3 = null;
        testFutureMeeting4 = null;
        testFutureMeeting5 = null;
        testFutureMeeting6 = null;
    }

    @Test
    public void returnAMeetingIdNumberWhenTheAddFutureMeetingMethodIsCalled(){
        int expected = 1;
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        int output = testContactManager.addFutureMeeting(testSet, testCalendar3);
        assertEquals(expected, output);
    }

    @Test
    public void theCorrectNumberWhenSeveralFutureMeetingsAreAdded(){
        int expected = 2;
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        testContactManager.addFutureMeeting(testSet, testCalendar3);
        int output = testContactManager.addFutureMeeting(testSet2, testCalendar3);
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwAnIllegalArgumentExceptionIfTheMeetingIsSetInThePast(){
        testContactManager.addFutureMeeting(testSet, testCalendar2);
    }

    @Test(expected = NullPointerException.class)
    public void throwANullPointerExceptionIfTheDateForAddFutureMeetingIsNull(){
        testContactManager.addFutureMeeting(testSet, nullCalendar);
    }

    @Test(expected = IllegalArgumentException.class)
    public void returnAnIAEIfAnyContactIsUnknownWhenAddFutureMeetingIsCalled(){
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Joe", "Just average");
        testContactManager.addFutureMeeting(testSet2, testCalendar4);
    }

    @Test
    public void createTheCorrectFutureMeetingWhenAddFutureMeetingIsCalled(){
        FutureMeeting expected = testFutureMeeting;
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        testContactManager.addFutureMeeting(testSet, testCalendar3);
        FutureMeeting output = testContactManager.getFutureMeeting(1);
        assertEquals(expected, output);
    }
    @Test
    public void returnTheRequestedFutureMeetingBasedOnTheMeetingId(){
        FutureMeeting expected = testFutureMeeting;
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        testContactManager.addFutureMeeting(testSet, testCalendar3);
        FutureMeeting output = testContactManager.getFutureMeeting(1);
        assertEquals(expected.getId(), output.getId());
        assertEquals(expected.getDate(), output.getDate());
        assertEquals(expected.getContacts(), output.getContacts());
    }

    @Test
    public void returnNullIfThereIsNoFutureMeetingByThatId(){
        FutureMeeting expected = null;
        FutureMeeting output = testContactManager.getFutureMeeting(5);
        assertEquals(expected, output);
    }

    @Test(expected = IllegalStateException.class)
    public void throwExceptionIfTheMeetingRequestedIsInThePastAndGetFutureMeetingIsCalled(){
        testContactManager.addNewPastMeeting(testSet, testCalendar2, "past meeting");
        testContactManager.getFutureMeeting(1);
    }

    @Test
    public void addANewPastMeetingAndReturnAMeetingIdThatIsCorrect(){
        int expected = 1;
        int output = testContactManager.addNewPastMeeting(testSet, testCalendar2, "Not productive");
        assertEquals(expected, output);
    }

    @Test
    public void returnTheCorrectMeetingIdNumberIfMoreThanOneMeetingIsAdded(){
        int expected = 2;
        testContactManager.addNewPastMeeting(testSet, testCalendar2, "productive");
        int output = testContactManager.addNewPastMeeting(testSet2, testCalendar2, "Not productive");
        assertEquals(expected, output);
    }

    @Test
    public void createTheExpectedMeetingWhenAddNewPastMeetingIsCalled(){
        PastMeeting expected = testPastMeeting;
        testContactManager.addNewPastMeeting(testSet, testCalendar2, "note");
        PastMeeting output = testContactManager.getPastMeeting(1);
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void returnAnIllegalArgumentExceptionIfTheListOfContactsPassedToAddNewPastMeetingIsEmpty(){
        testContactManager.addNewPastMeeting(emptySet, testCalendar2, "Boring");
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwAnIllegalArgumentExceptionIfTheDatePassedToAddNewPastMeetingIsInTheFuture(){
        testContactManager.addNewPastMeeting(testSet, testCalendar3, "something's off");
    }

    @Test(expected = NullPointerException.class)
    public void throwANullPointerExceptionIfTheContactsListPassedToAddNewPastMeetingIsNull(){
        testContactManager.addNewPastMeeting(nullSet, testCalendar2, "Empty contacts");
    }

    @Test(expected = NullPointerException.class)
    public void throwANullPointerExceptionIfTheDatePassedToAddNewPastMeetingIsNull(){
        testContactManager.addNewPastMeeting(testSet, nullCalendar, "Empty date");
    }

    @Test(expected = NullPointerException.class)
    public void throwANullPointerExceptionIfTheStringPassedToAddNewPastMeetingIsNull(){
        testContactManager.addNewPastMeeting(testSet, testCalendar2, nullString);
    }

    @Test(expected = IllegalStateException.class)
    public void throwIllegalStateExceptionIfGetPastMeetingCallsAMeetingInTheFuture(){
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        testContactManager.addFutureMeeting(testSet, testCalendar3);
        testContactManager.getPastMeeting(1);
    }

    @Test
    public void returnNullIfTheMeetingCalledByGetPastMeetingDoesNotExist(){
        PastMeeting expected = null;
        testContactManager.addNewPastMeeting(testSet, testCalendar2, "test entry");
        PastMeeting output = testContactManager.getPastMeeting(2);
        assertEquals(expected, output);
    }

    @Test
    public void returnTheCorrectMeetingWhenGetPastMeetingIsCalled(){
        PastMeeting expected = testPastMeeting;
        testContactManager.addNewPastMeeting(testSet, testCalendar2, "note");
        PastMeeting output = testContactManager.getPastMeeting(1);
        assertEquals(expected, output);
    }

    @Test
    public void returnNullIfGetMeetingIsCalledAndTheMeetingDoesNotExist(){
        Meeting expected = null;
        Meeting output = testContactManager.getMeeting(1);
        assertEquals(expected, output);
    }

    @Test
    public void returnTheCorrectMeetingWhenGetMeetingIsCalledOnAMeetingId(){
        Meeting expected = testFutureMeeting;
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        testContactManager.addFutureMeeting(testSet, testCalendar3);
        Meeting output = testContactManager.getMeeting(1);
        assertEquals(expected, output);
    }

    @Test
    public void returnTheCorrectMeetingWhenGetMeetingIsCalledOnAMeetingIdThatIsNot1(){
        Meeting expected = secondTestFutureMeeting;
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        testContactManager.addFutureMeeting(testSet, testCalendar3);
        testContactManager.addFutureMeeting(testSet, testCalendar3);
        testContactManager.addFutureMeeting(testSet2, testCalendar3);
        Meeting output = testContactManager.getMeeting(3);
        assertEquals(expected, output);
    }

    @Test(expected = NullPointerException.class)
    public void throwANullPointerExceptionIfTheContactGivenToGetFutureMeetingListIsNull(){
        testContactManager.getFutureMeetingList(nullContact);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwIllegalArgumentExceptionIfTheContactUsedAsArgumentForGetFutureMeetingListDoesNotExist(){
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Joe", "Just average");
        testContactManager.addFutureMeeting(testSet2, testCalendar3);
        testContactManager.getFutureMeetingList(contact2);
    }

    @Test
    public void returnAListOfFutureMeetingsThatAreScheduledWithTheContactWhenGetFutureMeetingListCalled(){
        List<Meeting> expected = new ArrayList<>();
        expected.add(testFutureMeeting);
        expected.add(testFutureMeeting3);
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        testContactManager.addFutureMeeting(testSet, testCalendar3);
        testContactManager.addFutureMeeting(testSet, testCalendar4);
        testContactManager.addFutureMeeting(testSet2, testCalendar5);
        List<Meeting> output = testContactManager.getFutureMeetingList(contact2);
        assertEquals(expected, output);
    }

    @Test
    public void returnAListOfFutureMeetingsWithNoDuplicatesWhenGetFutureMeetingListIsCalled(){
        List<Meeting> expected = new LinkedList<>();
        expected.add(testFutureMeeting4);
        expected.add(testFutureMeeting3);
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        testContactManager.addFutureMeeting(testSet, testCalendar4);
        testContactManager.addFutureMeeting(testSet, testCalendar5);
        testContactManager.addFutureMeeting(testSet, testCalendar4);
        testContactManager.addFutureMeeting(testSet2, testCalendar3);
        List<Meeting> output = testContactManager.getFutureMeetingList(contact2);
        assertEquals(expected.get(0).getDate(), output.get(0).getDate());
        assertEquals(expected.get(1).getDate(), output.get(1).getDate());
        assertEquals(expected.get(0).getContacts(), output.get(0).getContacts());
        assertEquals(expected.get(1).getContacts(), output.get(1).getContacts());
    }

    @Test
    public void returnAnEmptyListIfTheContactIsNotAssociatedWithAnyOfTheMeetings(){
        List<Meeting> expected = new LinkedList<>();
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        testContactManager.addFutureMeeting(testSet2, testCalendar3);
        testContactManager.addFutureMeeting(testSet2, testCalendar4);
        testContactManager.addFutureMeeting(testSet2, testCalendar5);
        List<Meeting> output = testContactManager.getFutureMeetingList(contact2);
        assertEquals(expected, output);
    }

    @Test
    public void returnAContactIdWhenANewContactIsAddedByAddContact(){
        int expected = 1;
        int output = testContactManager.addNewContact("Mary", "Very nice");
        assertEquals(expected, output);
    }

    @Test
    public void returnsTheCorrectIdNUmberWhenTheContactAddedByAddContactIsNotTheFirstInTheList(){
        int expected = 3;
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        int output = testContactManager.addNewContact("Joe", "Kind of average");
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwIAEWhenTheNameSubmittedToAddNewContactIsEmpty(){
        testContactManager.addNewContact(emptyString, "Kind of vacant");
    }

    @Test (expected = IllegalArgumentException.class)
    public void throwIAEWHenTheNotesSectionSubmittedToAddNewContactIsAnEmptyString(){
        testContactManager.addNewContact("Mary", emptyString);
    }

    @Test(expected = NullPointerException.class)
    public void throwNPEWhenTheNameSubmittedToAddNewContactIsNull(){
        testContactManager.addNewContact(nullString, "Really vacant");
    }

    @Test(expected = NullPointerException.class)
    public void throwNPEWhenTheNotesSectionSubmittedToAddNewContactIsAnEmptyString(){
        testContactManager.addNewContact("Mary", nullString);
    }

    @Test(expected = NullPointerException.class)
        public void throwsNPEIfTheNameSubmittedToGetContactsIsNull(){
        testContactManager.getContacts(nullString);
    }

    @Test
    public void returnsTheEntireListOfContactsIfTheStringGivenToGetContactsIsEmpty(){
        Set<Contact> expected = new HashSet<>();
        expected.add(contact1);
        expected.add(contact2);
        expected.add(contact3);
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        Set<Contact> output = testContactManager.getContacts(emptyString);
        assertEquals(expected, output);
    }

    @Test
    public void returnsASetWithTheContactsWhoseNameContainsThatStringWhenGetContactsIsCalled(){
        Set<Contact> expected = new HashSet<>();
        expected.add(contact6);
        expected.add(contact7);
        expected.add(contact8);
        testContactManager.addNewContact("Harry", "Just shave");
        testContactManager.addNewContact("Harry", "Still hirsuite");
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        Set<Contact> output = testContactManager.getContacts("Harry");
        assertEquals(expected, output);
    }

    @Test (expected = IllegalArgumentException.class)
    public void throwAnIAEWhenNoIDsAreProvidedAsAnArgumentToGetContactsThatTakesIdNumbers(){
        testContactManager.getContacts();
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwAnIAEWhenOneOfTheContactIdsDoesNotExist(){
        testContactManager.addNewContact("Harry", "Needs a haircut");
        testContactManager.addNewContact("Sally", "Is here with Harry");
        testContactManager.addNewContact("Joe", "Just an average guy");
        testContactManager.getContacts(4);
    }

    @Test
    public void returnASetOfContactsWithASingleContactWhenOneIdIsGivenTOGetContacts(){
        Set<Contact> expected = new HashSet<>();
        expected.add(contact2);
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        Set<Contact> output = testContactManager.getContacts(2);
        assertEquals(expected, output);
    }

    @Test
    public void returnASetOfContactsThatCorrespondToTheIdsProvided(){
        Set<Contact> expected = new HashSet<>();
        expected.add(contact2);
        expected.add(contact3);
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        Set<Contact> output = testContactManager.getContacts(2, 3);
        assertEquals(expected, output);
    }

    @Test
    public void returnASetOfContactsThatCorrespondToTheIdsProvidedWhenGivenARepeatedIdValue() {
        Set<Contact> expected = new HashSet<>();
        expected.add(contact2);
        expected.add(contact3);
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        Set<Contact> output = testContactManager.getContacts(2, 2, 2, 2, 3);
        assertEquals(expected, output);
    }

    @Test
    public void returnASetOfContactsThatCorrespondToTheIdsProvidedWhenGivenIdsOutOfOrder() {
        Set<Contact> expected = new HashSet<>();
        expected.add(contact2);
        expected.add(contact3);
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        Set<Contact> output = testContactManager.getContacts(3, 2);
        assertEquals(expected, output);
    }

    @Test
    public void returnASetOfContactsThatCorrespondToTheIdsProvidedWhenGivenIdsOutOfOrderAndMultiples() {
        Set<Contact> expected = new HashSet<>();
        expected.add(contact2);
        expected.add(contact3);
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        Set<Contact> output = testContactManager.getContacts(3, 2, 2, 3, 3, 2);
        assertEquals(expected, output);
    }

    @Test(expected = NullPointerException.class)
    public void throwANPEIfTheDatePassedToGetMeetingListOnIsNull(){
        testContactManager.getMeetingListOn(nullCalendar);
    }

    @Test
    public void returnAnEmptyListIfThereAreNoMeetingsScheduledForThatDay(){
        List<Meeting> expected = new ArrayList<>();
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        testContactManager.addFutureMeeting(testSet2, testCalendar3);
        testContactManager.addFutureMeeting(testSet, testCalendar4);
        testContactManager.addFutureMeeting(testSet, testCalendar3);
        testContactManager.addFutureMeeting(testSet, testCalendar3);
        testContactManager.addFutureMeeting(testSet, testCalendar6);
        testContactManager.addFutureMeeting(testSet, testCalendar7);
        List<Meeting> output = testContactManager.getMeetingListOn(testCalendar5);
        assertEquals(expected, output);
    }

    @Test
    public void returnAListOfMeetingsOnTheDateInQuestionWhenGetMeetingListOnIsCalled(){
        List<Meeting> expected = new LinkedList<>();
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        expected.add(testFutureMeeting6);
        expected.add(testFutureMeeting5);
        testContactManager.addFutureMeeting(testSet2, testCalendar5);
        testContactManager.addFutureMeeting(testSet, testCalendar4);
        testContactManager.addFutureMeeting(testSet, testCalendar5);
        testContactManager.addFutureMeeting(testSet, testCalendar5);
        testContactManager.addFutureMeeting(testSet, testCalendar6);
        testContactManager.addFutureMeeting(testSet, testCalendar7);
        List<Meeting> output = testContactManager.getMeetingListOn(testCalendar3);
        assertEquals(expected, output);
    }

    @Test
    public void returnAListOfMeetingsOnTheDateInQuestionWhenGetMeetingListOnIsCalledNoDuplicates(){
        List<Meeting> expected = new LinkedList<>();
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        expected.add(testFutureMeeting6);
        expected.add(testFutureMeeting5);
        testContactManager.addFutureMeeting(testSet2, testCalendar6);
        testContactManager.addFutureMeeting(testSet, testCalendar4);
        testContactManager.addFutureMeeting(testSet, testCalendar5);
        testContactManager.addFutureMeeting(testSet, testCalendar5);
        testContactManager.addFutureMeeting(testSet, testCalendar6);
        testContactManager.addFutureMeeting(testSet, testCalendar7);
        List<Meeting> output = testContactManager.getMeetingListOn(testCalendar3);
        assertEquals(expected, output);
    }

    @Test(expected = NullPointerException.class)
    public void throwANPEIfTheContactGivenToTheGetPastMeetingListForIsNull(){
        testContactManager.getPastMeetingListFor(nullContact);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwAnIAEIfTheContactGivenToGetPastMeetingListDoesNotExist(){
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Joe", "Just average");
        testContactManager.addNewPastMeeting(testSet2, testCalendar2, "Quiet client");
        testContactManager.getPastMeetingListFor(contact2);
    }
    @Test
    public void returnAnEmptyListIfThereAreNoMeetingsForTheContactGivenToGetPastMeetingListFor(){
        List<PastMeeting> expected = new LinkedList<>();
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        testContactManager.addNewPastMeeting(testSet2, testCalendar, "Fun");
        testContactManager.addNewPastMeeting(testSet2, testCalendar2, "Not so fun");
        testContactManager.addNewPastMeeting(testSet2, testCalendar, "Really boring");
        List<PastMeeting> output = testContactManager.getPastMeetingListFor(contact2);
        assertEquals(expected, output);
    }

    @Test
    public void returnAChronologicallySortedListForContactsInGetPastMeetingListFor(){
        List<PastMeeting> expected = new LinkedList<>();
        expected.add(testPastMeeting2);
        expected.add(testPastMeeting3);
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        testContactManager.addNewPastMeeting(testSet2, testCalendar, "note");
        testContactManager.addNewPastMeeting(testSet, testCalendar8, "note");
        testContactManager.addNewPastMeeting(testSet, testCalendar, "note");
        testContactManager.addNewPastMeeting(testSet2, testCalendar8, "no note");
        List<PastMeeting> output = testContactManager.getPastMeetingListFor(contact2);
        assertEquals(expected, output);
    }

    @Test
    public void returnAListWithoutDuplicatesForGetPastMeetingListFor(){
        List<PastMeeting> expected = new LinkedList<>();
        expected.add(testPastMeeting2);
        expected.add(testPastMeeting3);
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        testContactManager.addNewPastMeeting(testSet2, testCalendar, "note");
        testContactManager.addNewPastMeeting(testSet, testCalendar8, "note");
        testContactManager.addNewPastMeeting(testSet, testCalendar, "note");
        testContactManager.addNewPastMeeting(testSet, testCalendar8, "note");
        List<PastMeeting> output = testContactManager.getPastMeetingListFor(contact2);
    }

    @Test(expected = NullPointerException.class)
    public void throwANPEWhenAddMeetingNotesIsCalledAndTheNotesAreNull(){
        testContactManager.addMeetingNotes(1, nullString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwAnIAEWhenTheMeetingDoesNotExistAndAddMeetingNotesIsCalled(){
        testContactManager.addFutureMeeting(testSet, testCalendar4);
        testContactManager.addFutureMeeting(testSet2, testCalendar3);
        testContactManager.addMeetingNotes(3, "New notes");
    }

    @Test(expected = IllegalStateException.class)
    public void throwAnISEIfTheMeetingSelectedIsNotForTodayAndIsInTheFutureWhenAddMeetingNotesIsCalled(){
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        testContactManager.addFutureMeeting(testSet, testCalendar4);
        testContactManager.addMeetingNotes(1, "A little early");
    }

    @Test
    public void returnANewPastMeetingWithTheCorrectFieldsWhenAddMeetingNotesIsCalledOnAnAppropriateMeeting(){
        Calendar testDate = Calendar.getInstance();
        testDate.add(Calendar.HOUR_OF_DAY, 1);
        PastMeeting expected = new PastMeetingImpl(1, testDate, testSet, "New past meeting");
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewContact("Joe", "Just average");
        testContactManager.addFutureMeeting(testSet, testDate);
        PastMeeting output = testContactManager.addMeetingNotes(1, "New past meeting");
        assertEquals(expected, output);
    }

    @Test
    public void returnAPastMeetingWithNotesAddedToItWhenAddMeetingNotesIsCalledOnAPastMeeting(){
        PastMeeting expected = new PastMeetingImpl(1, testCalendar8, testSet, "First notes ; Second notes");
        testContactManager.addNewPastMeeting(testSet, testCalendar8, "First notes");
        PastMeeting output = testContactManager.addMeetingNotes(1, "Second notes");
        assertEquals(expected, output);
    }

    @Test
    public void verifyThatWhenFlushIsCalledItOpensAndClosesContactsTextFile() {
        // just test closing, don't add anything (leave it unpopulated)
        testContactManager.flush();

        assertEquals("Should open exactly one file for writing",
                1, testIOProvider.mockWriters.size());
        assertEquals("File opened for writing should be named contacts.txt",
                "contacts.txt", testIOProvider.mockWriters.get(0).getKey());
        assertTrue("File opened for writing should have been closed by flush method",
                testIOProvider.mockWriters.get(0).getValue().wasCloseCalled);
        assertEquals("Should result in the following output for empty contact manager",
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<condensedContactManagerInfo/>\n",
                testIOProvider.mockWriters.get(0).getValue().toString());
    }

    @Test
    public void verifyThatWhenFlushWritesContactsToTheContactsTextFile() {
        testContactManager.addNewContact("Harry", "Needs a barber");
        testContactManager.addNewContact("Sally", "Met Harry");

        testContactManager.flush();

        assertEquals("Should open exactly one file for writing",
                1, testIOProvider.mockWriters.size());
        assertEquals("File opened for writing should be named contacts.txt",
                "contacts.txt", testIOProvider.mockWriters.get(0).getKey());
        assertTrue("File opened for writing should have been closed by flush method",
                testIOProvider.mockWriters.get(0).getValue().wasCloseCalled);

        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<condensedContactManagerInfo>\n" +
                "    <contacts id=\"1\" name=\"Harry\" notes=\"Needs a barber\"/>\n" +
                "    <contacts id=\"2\" name=\"Sally\" notes=\"Met Harry\"/>\n" +
                "</condensedContactManagerInfo>\n";
        assertEquals("Should result in the following output for two contacts",
                expected,
                testIOProvider.mockWriters.get(0).getValue().toString());
    }

    @Test
    public void verifyThatWhenFlushWritesAMeetingToTheContactsTextFile() {
        int harry = testContactManager.addNewContact("Harry", "Needs a barber");
        int sally = testContactManager.addNewContact("Sally", "Met Harry");
        testContactManager.addNewPastMeeting(testContactManager.getContacts(harry, sally), testCalendar8,
                "Harry REALLY liked Sally.");
        testContactManager.addFutureMeeting(testContactManager.getContacts(harry, sally), testCalendar4);

        testContactManager.flush();

        assertEquals("Should open exactly one file for writing",
                1, testIOProvider.mockWriters.size());
        assertEquals("File opened for writing should be named contacts.txt",
                "contacts.txt", testIOProvider.mockWriters.get(0).getKey());
        assertTrue("File opened for writing should have been closed by flush method",
                testIOProvider.mockWriters.get(0).getValue().wasCloseCalled);

        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<condensedContactManagerInfo>\n" +
                "    <contacts id=\"1\" name=\"Harry\" notes=\"Needs a barber\"/>\n" +
                "    <contacts id=\"2\" name=\"Sally\" notes=\"Met Harry\"/>\n" +
                "    <meetings id=\"1\" dateTime=\"1428142200000\" contacts=\"2 1\" notes=\"Harry REALLY liked Sally.\"/>\n" +
                "    <meetings id=\"2\" dateTime=\"1518600600000\" contacts=\"2 1\" notes=\"\"/>\n" +
                "</condensedContactManagerInfo>\n";
        assertEquals("Should result in the following output for two contacts",
                expected,
                testIOProvider.mockWriters.get(0).getValue().toString());
    }


}
