package mmkeri;

/**
 * Created by mmker on 12-Jan-17.
 */

        import sun.reflect.generics.reflectiveObjects.NotImplementedException;

        import javax.xml.bind.*;
        import javax.xml.bind.annotation.XmlAttribute;
        import javax.xml.bind.annotation.XmlElement;
        import javax.xml.bind.annotation.XmlRootElement;
        import java.io.IOException;
        import java.io.Reader;
        import java.io.Writer;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.List;

/**
 * A compact representation of the contact manager's state, optimised for
 * reading / writing to files (and other media)
 */
@XmlRootElement
public class CondensedContactManagerInfo {

    static final JAXBContext jaxbContext;
    static {
        try {
            jaxbContext = JAXBContext.newInstance(CondensedContactManagerInfo.class);
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @XmlElement
    List<ContactInfo> contacts;
    @XmlElement
    List<MeetingInfo> meetings;

    public CondensedContactManagerInfo() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public CondensedContactManagerInfo(List<ContactInfo> contacts, List<MeetingInfo> meetings) {
        this.contacts = contacts;
        this.meetings = meetings;
    }

    public static CondensedContactManagerInfo unmarshal(Reader reader) {
        try {
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (CondensedContactManagerInfo) jaxbUnmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void marshal(Writer writer) throws IOException {
        try {
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(this, writer);
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @XmlRootElement
    static class ContactInfo {
        @XmlAttribute
        int id;
        @XmlAttribute
        String name;
        @XmlAttribute
        String notes;
        public ContactInfo(Contact contact) {
            this.id = contact.getId();
            this.name = contact.getName();
            this.notes = contact.getNotes();
        }
        public ContactInfo() { }
    }

    @XmlRootElement
    static class MeetingInfo {
        @XmlAttribute
        int id;
        @XmlAttribute
        long dateTime;
        @XmlAttribute
        List<Integer> contacts;
        @XmlAttribute
        String notes;
        public MeetingInfo(Meeting meeting) {
            this.id = meeting.getId();
            this.dateTime = meeting.getDate().getTimeInMillis();
            this.contacts = new ArrayList<>();
            for (Contact contact : meeting.getContacts()) {
                contacts.add(contact.getId());
            }
            this.notes = (meeting instanceof PastMeeting) ? ((PastMeeting) meeting).getNotes() : "";
        }
        public MeetingInfo() { }
    }
}
