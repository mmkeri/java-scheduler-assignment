package impl;

/**
 * Created by mmker on 12-Jan-17.
 */

        import spec.Contact;
        import spec.Meeting;
        import spec.PastMeeting;

        import javax.xml.bind.*;
        import javax.xml.bind.annotation.XmlAttribute;
        import javax.xml.bind.annotation.XmlElement;
        import javax.xml.bind.annotation.XmlRootElement;
        import java.io.IOException;
        import java.io.Reader;
        import java.io.Writer;
        import java.util.ArrayList;
        import java.util.List;

/**
 * A compact representation of the contact manager's state, optimised for
 * reading / writing to files (and other media)
 */
@XmlRootElement
public final class CondensedContactManagerInfo {

    static final JAXBContext JAXB_CONTEXT;
    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(CondensedContactManagerInfo.class);
        } catch (JAXBException e) {
            throw new InvalidAnnotationsException(e);
        }
    }

    @XmlElement
    private List<ContactInfo> contacts;
    @XmlElement
    private List<MeetingInfo> meetings;

    public List<ContactInfo> getContacts(){
        return contacts;
    }

    public List<MeetingInfo>  getMeetings(){
        return meetings;
    }


    public CondensedContactManagerInfo() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public CondensedContactManagerInfo(List<ContactInfo> newContacts, List<MeetingInfo> newMeetings) {
        this.contacts = newContacts;
        this.meetings = newMeetings;
    }

    public static CondensedContactManagerInfo unmarshal(Reader reader) {
        try {
            Unmarshaller jaxbUnmarshaller = JAXB_CONTEXT.createUnmarshaller();
            return (CondensedContactManagerInfo) jaxbUnmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw new ContactManagerDecodeException(e);
        }
    }

    public void marshal(Writer writer) throws IOException {
        try {
            Marshaller jaxbMarshaller = JAXB_CONTEXT.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(this, writer);
        } catch (JAXBException e) {
            throw new IOException(e);
        }
    }

    @XmlRootElement
    static class ContactInfo {
        @XmlAttribute
        private int id;
        @XmlAttribute
        private String name;
        @XmlAttribute
        private String notes;
        public ContactInfo(Contact contact) {
            this.id = contact.getId();
            this.name = contact.getName();
            this.notes = contact.getNotes();
        }
        public ContactInfo() { }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getNotes() {
            return notes;
        }
    }

    @XmlRootElement
    static class MeetingInfo {
        @XmlAttribute
        private int id;
        @XmlAttribute
        private long dateTime;
        @XmlAttribute
        private List<Integer> contacts;
        @XmlAttribute
        private String notes;
        public MeetingInfo(Meeting meeting) {
            this.id = meeting.getId();
            this.dateTime = meeting.getDate().getTimeInMillis();
            this.contacts = new ArrayList<>();
            for (Contact contact : meeting.getContacts()) {
                getContacts().add(contact.getId());
            }
            this.notes = getNotesHelper(meeting);
        }

        static String getNotesHelper(Meeting meeting) {
            return (meeting instanceof PastMeeting) ? ((PastMeeting) meeting).getNotes() : "";
        }

        public MeetingInfo() { }

        public int getId() {
            return id;
        }

        public long getDateTime() {
            return dateTime;
        }

        public List<Integer> getContacts() {
            return contacts;
        }

        public String getNotes() {
            return notes;
        }
    }
}
