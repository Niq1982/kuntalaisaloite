package fi.om.municipalityinitiative.dto;

import javax.validation.constraints.NotNull;

/**
 * This contains representative or reserve author information that is allowed to show in public views
 */
public class RepresentativeInfo extends AuthorInfo {

    @NotNull
    private ContactInfo contactInfo = new ContactInfo();
    
    public RepresentativeInfo(Author author) {
        super(author);
        contactInfo = new ContactInfo(author.getContactInfo().getEmail(), 
                                      author.getContactInfo().getPhone(), 
                                      author.getContactInfo().getAddress());
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void assignContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

}
