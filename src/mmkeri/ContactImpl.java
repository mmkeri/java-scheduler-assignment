package mmkeri;

/**
 * Created by mmker on 03-Jan-17.
 */
public class ContactImpl implements Contact {
    private int contactId = 0;
    private String name = null;
    private String notes = "";

    public ContactImpl(int Id, String name, String notes) throws NullPointerException, IllegalArgumentException{
        if(Id <= 0){
            throw new IllegalArgumentException();
        }else {
            this.contactId = Id;
        }
        if (name == null || notes == null) {
            throw new NullPointerException();
        }else {
            this.name = name;
            this.notes = notes;
        }
    }

    public ContactImpl(int Id, String name)throws NullPointerException, IllegalArgumentException{
        if(Id <= 0) {
            throw new IllegalArgumentException();
        } else {
            this.contactId = Id;
        }
        if(name == null){
            throw new NullPointerException();
        } else {
            this.name = name;
        }
    }

    @Override
    public int getId() {
        return contactId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNotes() {
        return notes;
    }

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
     * @return
     */
    @Override
    public int hashCode() {
        return this.getId() ^ this.getName().hashCode() ^ this.getNotes().hashCode();
    }

    /**
     * Override of equals to use Id, Name and Notes for comparison
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ContactImpl))
            return false;
        ContactImpl other = (ContactImpl) obj;
        return other.getId() == this.getId() &&
                other.getName().equals(this.getName()) &&
                other.getNotes().equals(this.getNotes());
    }
}
