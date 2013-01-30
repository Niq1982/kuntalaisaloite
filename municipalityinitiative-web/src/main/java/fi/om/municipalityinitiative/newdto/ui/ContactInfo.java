package fi.om.municipalityinitiative.newdto.ui;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class ContactInfo {

    /**
     *  Using @Pattern instead of @Email, because hibernate's email validation was quite not good enough.
     *  Hibernate's Email validator passes emails like: "address@domain", "address@domain.com."
     *
     *  Regexp is from: http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
     *  - Added char '+' and '-' for the original regexp.
     */
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-\\+]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @NotEmpty
    private String name;

    @NotEmpty
    @Pattern(regexp = EMAIL_PATTERN)
    private String email;

    private String phone;

    private String address;


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


}
