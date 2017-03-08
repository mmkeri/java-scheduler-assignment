package test;

import static org.junit.Assert.*;

import spec.Contact;
import impl.ContactImpl;
import org.junit.*;

/**
 * Created by mmker on 03-Jan-17.
 */
public class ContactImplShould {
    Contact testContact = null;
    Contact testContact2 = null;
    String testNullString = null;

    @Before public void setUpContacts() {
        testContact = new ContactImpl(5, "testContact", "this is a test");
        testContact2 = new ContactImpl(5, "test Contact 2");
        testNullString = null;
    }

   @After public void cleanUp(){
       testContact = null;
       testContact2 = null;
       testNullString = null;
   }

    @Test
    public final void getIDShouldReturnTheIdNumberForTheContact(){
        int expected = 5;
        int output = testContact.getId();
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void returnAnIllegalArgumentExceptionWhenGivenAZeroIdValueToFirstConstructor(){
        Contact secondContact = new ContactImpl(0, "testContact", "this is a test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getReturnAnIllegalArgumentExceptionWhenGivenANegativeValueToFirstConstructor(){
        Contact thirdContact = new ContactImpl(-1, "testContact", "this is a test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void returnAnIllegalArgumentExceptionWhenGivenAZeroValueToSecondConstructor(){
        Contact secondContact = new ContactImpl(0, "testContact");
    }

    @Test(expected = IllegalArgumentException.class)
    public void returnAnIllegalArgumentExceptionWhenGivenANegativeValueToSecondConstructor(){
        Contact secondContact = new ContactImpl(-5, "testContact");
    }

    @Test
    public void returnTheNameOfTheContactWhenAddedUsingTheFirstConstructor(){
        String expected = "testContact";
        String output = testContact.getName();
        assertEquals(expected, output);
    }

    @Test
    public void returnTheNameOfTheContactWhenAddedUsingTheSecondConstructor(){
        String expected = "test Contact 2";
        String output = testContact2.getName();
        assertEquals(expected, output);
    }


    @Test(expected = NullPointerException.class)
    public void returnANullPointerExceptionWhenPassedAnEmptyStringForNameToTheFirstConstructor(){
        Contact secondContact = new ContactImpl(5,testNullString, "this is a test");
    }

    @Test(expected = NullPointerException.class)
    public void returnANullPointerExceptionWhenPassedAnEmptyStringForNameToTheSecondConstructor(){
        Contact secondContact = new ContactImpl(5, testNullString);
    }

    @Test
    public void returnTheValueForNotes(){
        String expected = "this is a test";
        String output = testContact.getNotes();
        assertEquals(expected, output);
    }

    @Test(expected = NullPointerException.class)
    public void returnANullPointerExceptionWhenPassedAnEmptyStringForNotesToTheFirstConstructor(){
        Contact secondContact = new ContactImpl(5, "testContact", testNullString);
    }

    @Test
    public void returnEmptyStringIfNoNotesHaveBeenEnteredInForTheContact(){
        String expected = "";
        String output = testContact2.getNotes();
        assertEquals(expected, output);
    }

    @Test
    public void addANoteToTheContact(){
        String expected = "this is another test";
        testContact2.addNotes("this is another test");
        String output = testContact2.getNotes();
        assertEquals(expected, output);
    }

    @Test
    public void concatentateTheOldAndNewStringsIfNotesAlreadyHasContent(){
        String expected = "this is a test ; this is another test";
        testContact.addNotes("this is another test");
        String output = testContact.getNotes();
        assertEquals(expected, output);
    }

    @Test
    public void addsThreeNotesCorrectlyUsingTheAddNotesMethod(){
        String expected = "this is a test ; this is another test ; this is the last test";
        testContact2.addNotes("this is a test");
        testContact2.addNotes("this is another test");
        testContact2.addNotes("this is the last test");
        String output = testContact2.getNotes();
        assertEquals(expected, output);
    }
}