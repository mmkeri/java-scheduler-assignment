package impl;

/**
 * Created by mmker on 12-Jan-17.
 */

        import spec.Contact;
        import spec.IOProvider;
        import spec.Meeting;
        import spec.PastMeeting;

        import javax.xml.bind.*;
        import javax.xml.bind.annotation.XmlAttribute;
        import javax.xml.bind.annotation.XmlElement;
        import javax.xml.bind.annotation.XmlRootElement;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.Reader;
        import java.io.Writer;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.logging.Level;
        import java.util.logging.Logger;


/**
 * A compact representation of the contact manager's state, optimised for
 * reading / writing to files (and other media)
 */
@XmlRootElement
public final class CondensedContactManagerInfo {

    static final JAXBContext JAXB_CONTEXT;
    static {
        JAXBContext staticContext = null;
        try {
            staticContext = JAXBContext.newInstance(CondensedContactManagerInfo.class);
        } catch (JAXBException e) {
            Logger.getGlobal().log(Level.SEVERE, "Error while initializing JAXB, this is a student-error in annotation usage.", e);
        }
        JAXB_CONTEXT = staticContext;
    }

    /**
     * a list of ContactInfo objects
     */
    @XmlElement
    private List<ContactInfo> contacts;
    /**
     * a list of MeetingInfo objects
     */
    @XmlElement
    private List<MeetingInfo> meetings;

    /**
     * @return a list of type ContactInfo
     */
    public List<ContactInfo> getContacts(){
        return contacts;
    }

    /**
     * @return a list of type MeetingInfo
     */
    public List<MeetingInfo>  getMeetings(){
        return meetings;
    }

    /**
     * No-argument constructor that initializes the CondensedContactManagerInfo object
     * with two generic ArrayLists
     */
    public CondensedContactManagerInfo() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor method. Accepts a list of Contacts and a list of Meetings and sets them
     * equal to the contacts and meetings fields of the object respectively
     * @param newContacts a list of type ContactInfo
     * @param newMeetings a list of type MeetingInfo
     */
    public CondensedContactManagerInfo(List<ContactInfo> newContacts, List<MeetingInfo> newMeetings) {
        this.contacts = newContacts;
        this.meetings = newMeetings;
    }

    /**
     * Creates a new Jaxb un-marshaller using the supplied reader
     * @param reader an instance of an XML file reader
     * @return  a CondensedContactManagerInfo object holding the ContactInfo and
     * MeetingInfo that had been saved previously
     */
    public static CondensedContactManagerInfo unmarshal(Reader reader) {
        try {
            Unmarshaller jaxbUnmarshaller = JAXB_CONTEXT.createUnmarshaller();
            return (CondensedContactManagerInfo) jaxbUnmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            Logger.getGlobal().log(Level.SEVERE, "Error while parsing contact manager database", e);
            return null;
        }
    }

    /**
     * Uses the provided IO provider to open the XML file used to store the ContactInfo
     * and MeetingInfo from a previous session of the ContactManager
     * @param ioProvider
     * @param path file name in the form of a String
     * @return A CondensedContactManager containing Contact and Meeting Info
     */
    public static CondensedContactManagerInfo unmarshalFromFile(IOProvider ioProvider, String path) {
        Reader reader = null;
        try {
            reader = ioProvider.openReader("contacts.txt");
            return CondensedContactManagerInfo.unmarshal(reader);
        } catch (FileNotFoundException fnf) {
            Logger.getGlobal().log(Level.SEVERE, "Error while trying to open file", fnf);
            // In case of file not found, default values / empty contact manager is created
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioex) {
                    Logger.getGlobal().log(Level.SEVERE, "Error while trying to close file", ioex);
                }
            }
        }
    }

    /**
     * Creates a new Jaxb Marshaller and then writes the contents of
     * the ContactInfo and MeetingInfo contained in the current Contact Manger
     * to an XML file
     * @param writer an instance of an XML file writer
     * @throws IOException if a JAXBException occurs while trying to write the
     * data to file a new IOException is thrown to the calling function
     */
    public void marshal(Writer writer) throws IOException {
        try {
            Marshaller jaxbMarshaller = JAXB_CONTEXT.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(this, writer);
        } catch (JAXBException e) {
            throw new IOException(e);
        }
    }

    /**
     * A storage object generated from a Contact object in preparation for condensing
     * and saving to and XML file when the ContactManager is closed
     */
    @XmlRootElement
    static class ContactInfo {
        /**
         * an integer value that is unique to each ContactInfo object
         */
        @XmlAttribute
        private int id;
        /**
         * A String value representing the name of the contact
         */
        @XmlAttribute
        private String name;
        /**
         * A String value the represents any additional information about the contact
         */
        @XmlAttribute
        private String notes;

        /**
         * Constructor for a new ContactInfo object that takes a single Contact as an argument.
         * Extracts the contact's contact-id, contact-name and notes and stores them in the object
         * @param contact a Contact object
         */
        public ContactInfo(Contact contact) {
            this.id = contact.getId();
            this.name = contact.getName();
            this.notes = contact.getNotes();
        }

        /**
         * No-argument constructor that will generate a new ContactInfo object with empty fields.
         */
        public ContactInfo() { }

        /**
         * @return the id value as an integer of a ContactInfo object.
         */
        public int getId() {
            return id;
        }

        /**
         * @return the name value as a string of a ContactInfo object.
         */
        public String getName() {
            return name;
        }

        /**
         * @return the notes from a ContactInfo object as a string.
         */
        public String getNotes() {
            return notes;
        }
    }

    /**
     * A storage object generated from a Meeting object in preparation for condensing
     * and saving to and XML file when the ContactManager is closed
     */
    @XmlRootElement
    static class MeetingInfo {
        /**
         * An integer value that is unique to each MeetingInfo object
         */
        @XmlAttribute
        private int id;
        /**
         * A Calendar object that represents the date of the meeting
         */
        @XmlAttribute
        private long dateTime;
        /**
         * A list of Integers that contains the unique id numbers of the contacts that were at
         * the meeting
         */
        @XmlAttribute
        private List<Integer> contacts;
        /**
         * A String value representing any additional information about the meeting
         */
        @XmlAttribute
        private String notes;

        /**
         * Constructor for a new MeetingInfo object that takes a single Meeting as an argument.
         * Extracts the meeting's meeting-id, date of the meeting, the list of the ID numbers of
         * the Contacts associated with the meeting and any notes (if present) and stores them in the object
         * @param meeting a Meeting object
         */
        public MeetingInfo(Meeting meeting) {
            this.id = meeting.getId();
            this.dateTime = meeting.getDate().getTimeInMillis();
            this.contacts = new ArrayList<>();
            for (Contact contact : meeting.getContacts()) {
                getContacts().add(contact.getId());
            }
            this.notes = getNotesHelper(meeting);
        }

        /**
         * If the meeting submitted to the MeetingInfo constructor is a PastMeeting and contains
         * notes associated with that meeting this method returns a String composed of those notes
         * If a PastMeeting object is not passed an empty string is returned
         * @param meeting a Meeting object. Either a Future or Past meeting
         * @return a String generated from the notes field of a PastMeeting or an empty String
         */
        static String getNotesHelper(Meeting meeting) {
            return (meeting instanceof PastMeeting) ? ((PastMeeting) meeting).getNotes() : "";
        }

        /**
         * No-arguments contructor for that initializes all parameters to null
         */
        public MeetingInfo() { }

        /**
         * @return the id value of the MeetingInfo object as an integer
         */
        public int getId() {
            return id;
        }

        /**
         * @return the date and time of the MeetingInfo object as a long
         */
        public long getDateTime() {
            return dateTime;
        }

        /**
         * @return a list of the Contact ID numbers as a list of integers
         */
        public List<Integer> getContacts() {
            return contacts;
        }

        /**
         * @return a String representing the notes associated with Meeting Object
         */
        public String getNotes() {
            return notes;
        }
    }
}
