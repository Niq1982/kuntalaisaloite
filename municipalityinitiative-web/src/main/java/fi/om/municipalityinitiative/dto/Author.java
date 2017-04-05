package fi.om.municipalityinitiative.dto;

import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.service.id.Id;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.LocalDate;

public abstract class Author<E extends Id> {

    private Maybe<Municipality> municipality;
    private ContactInfo contactInfo;
    private LocalDate createTime;
    private E id;

    public E getId() {
        return id;
    }

    public void setId(E id) {
        this.id = id;
    }

    public abstract boolean isVerified();

    public Maybe<Municipality> getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Maybe<Municipality> municipality) {
        this.municipality = municipality;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public LocalDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }
}
