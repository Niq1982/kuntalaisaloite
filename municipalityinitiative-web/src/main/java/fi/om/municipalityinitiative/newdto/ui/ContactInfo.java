package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ContactInfo {

    /**
     *  Using @Pattern instead of @Email, because hibernate's email validation was quite not good enough.
     *  Hibernate's Email validator passes emails like: "address@domain", "address@domain.com."
     *
     *  Regexp is from: http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
     *  - Added char '+' and '-' for the original regexp.
     */
    public static final String EMAIL_PATTERN = "^([_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-\\+]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})|)$";

    @NotEmpty
    @Size(max = InitiativeConstants.CONTACT_NAME_MAX)
    private String name;

    @NotEmpty
    @Pattern(regexp = EMAIL_PATTERN)
    @Size(max = InitiativeConstants.CONTACT_EMAIL_MAX)
    private String email;

    @Size(max = InitiativeConstants.CONTACT_PHONE_MAX)
    private String phone;

    @Size(max = InitiativeConstants.CONTACT_ADDRESS_MAX)
    private String address;

    private boolean showName;

    public ContactInfo(ContactInfo original) {
        this.name = original.name;
        this.email = original.email;
        this.phone = original.phone;
        this.address = original.address;
        this.showName = original.showName;
    }

    public ContactInfo() { }


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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isShowName() {
        return showName;
    }

    public void setShowName(boolean showName) {
        this.showName = showName;
    }
}
