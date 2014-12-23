package fi.om.municipalityinitiative.dto;

import fi.om.municipalityinitiative.util.Membership;

public class YouthInitiativeCreateDto {
    private Long municipality;
    private ContactInfo contactInfo = new ContactInfo();
    private String name;
    private String proposal;
    private String extraInfo;
    private long youthInitiativeId;
    private String locale;

    public void setMunicipality(Long municipality) {
        this.municipality = municipality;
    }

    public Long getMunicipality() {
        return municipality;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public String getProposal() {
        return proposal;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setYouthInitiativeId(long youthInitiativeId) {
        this.youthInitiativeId = youthInitiativeId;
    }

    public long getYouthInitiativeId() {
        return youthInitiativeId;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getLocale() {
        return locale;
    }

    public static class ContactInfo {
        private String name;
        private String email;
        private String phone;
        private Long municipality;
        private Membership membership = Membership.none;

        public Membership getMembership() {
            return membership;
        }

        public void setMembership(Membership membership) {
            this.membership = membership;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Long getMunicipality() {
            return municipality;
        }

        public void setMunicipality(Long municipality) {
            this.municipality = municipality;
        }
    }
}
