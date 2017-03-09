package impl;

import spec.Contact;

/**
 * Created by mmker on 03-Jan-17.
 */
public final class ContactImpl implements Contact {
    private int contactId = 0;
    private String name = null;
    private String notes = "";

    /**
     * Constructor of a new Contact Object and sets the value of the contactId, name and notes
     * with the arguments provided
     * @param id integer value representing the Contact ID number
     * @param newName String value representing the name of the Contact
     * @param newNotes String value for any notes/observations/thoughts about the meeting
     */
    public ContactImpl(int id, String newName, String newNotes){
        if(id <= 0){
            throw new IllegalArgumentException();
        }else {
            this.contactId = id;
        }

        // call .length() which will result in a null pointer exception if the arguments were null
        // done this way because the instructions stated that this method needed to throw a Null
        // Pointer Exception but the PMD cited it as redundant. A method call on an empty newName
        // or newNotes will throw the expected exception
        newName.length();
        newNotes.length();

        this.name = newName;
        this.notes = newNotes;
    }

    /**
     * Secondary constructor for a Contact object. Used if no notes are attached to the contact
     * at the time of its creation. Sets the contactId and name equal to the arguments provided.
     * @param id
     * @param newName
     */
    public ContactImpl(int id, String newName){
        if(id <= 0) {
            throw new IllegalArgumentException();
        } else {
            this.contactId = id;
        }
        newName.length();
        this.name = newName;
    }

    /**
     * @return the value of contactId of a Contact object as an integer.
     */
    @Override
    public int getId() {
        return contactId;
    }

    /**
     * @return the value of the name of a Contact object as a String.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return the value of the notes of a Contact object as a String.
     */
    @Override
    public String getNotes() {
        return notes;
    }

    /**
     * Allows for notes to be added to a Contact object if none were included at the
     * time of the object's creation or if additional notes need to be added at a later time.
     * @param note the notes to be added as a String
     */
    @Override
    public void addNotes(String note) {
        if (notes.equals("")) {
            notes = note;
        }else{
            notes = notes + " ; " + note;
        }
    }

    /**
     * override of hashCode to use the elements of a Contact since they are
     * returned in a Set. This made testing difficult since individual
     * elements could not be returned
     * @return a unique hashcode for this object
     */
    @Override
    public int hashCode() {
        return this.getId() ^ this.getName().hashCode() ^ this.getNotes().hashCode();
    }

    /**
     * Override of equals to use Id, Name and Notes for comparison
     * @param obj
     * @return a boolean result
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ContactImpl)) {
            return false;
        }
        ContactImpl other = (ContactImpl) obj;
        return other.getId() == this.getId() &&
                other.getName().equals(this.getName()) &&
                other.getNotes().equals(this.getNotes());
    }
}
