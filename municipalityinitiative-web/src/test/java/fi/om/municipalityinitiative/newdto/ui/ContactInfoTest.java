package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ContactInfoTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void if_email_is_empty_string_only_show_notNull_validation_error_and_no_emailPattern_error() {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setName("name");
        contactInfo.setEmail("");

        Set<ConstraintViolation<ContactInfo>> validationErrors = validator.validate(contactInfo);

        assertThat(validationErrors, hasSize(1));
        assertThat(getFirst(validationErrors).getPropertyPath().toString(), is("email"));
        assertThat(getFirst(validationErrors).getMessage(), is("may not be empty"));
    }

    @Test
    public void validate_invalid_email() {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setName("name");
        contactInfo.setEmail("wrong_email");

        Set<ConstraintViolation<ContactInfo>> validationErrors = validator.validate(contactInfo);

        assertThat(validationErrors, hasSize(1));
        assertThat(getFirst(validationErrors).getPropertyPath().toString(), is("email"));
        assertThat(getFirst(validationErrors).getMessage(), containsString("must match"));
    }

    @Test
    public void constructor_fills_all_fields() {
        ContactInfo original = ReflectionTestUtils.modifyAllFields(new ContactInfo());
        ContactInfo copy = new ContactInfo(original);
        ReflectionTestUtils.assertNoNullFields(copy);
        ReflectionTestUtils.assertReflectionEquals(copy, original);
    }

    private ConstraintViolation<ContactInfo> getFirst(Set<ConstraintViolation<ContactInfo>> violations) {
        return violations.iterator().next();
    }

}
