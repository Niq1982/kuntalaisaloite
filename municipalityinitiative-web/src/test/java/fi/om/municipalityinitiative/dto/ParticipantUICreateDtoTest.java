package fi.om.municipalityinitiative.dto;

import fi.om.municipalityinitiative.dto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.util.Membership;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class ParticipantUICreateDtoTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void municipalMembership_is_needed_if_municipalities_are_not_the_same() {
        ParticipantUICreateDto dto = createParticipantWithNameAndEmail();

        dto.assignMunicipality(1L);
        dto.setHomeMunicipality(2L);
        dto.setMunicipalMembership(null);

        Set<ConstraintViolation<ParticipantUICreateDto>> violations = validator.validate(dto);

        assertThat(violations, hasSize(1));
        assertThat(getFirst(violations).getPropertyPath().toString(), is("municipalMembership"));
        assertThat(getFirst(violations).getMessage(), is("ValidMunicipalMembership"));
    }

    @Test
    public void municipalMembership_cannot_be_false_if_municipalities_are_not_the_same() {
        ParticipantUICreateDto dto = createParticipantWithNameAndEmail();

        dto.assignMunicipality(1L);
        dto.setHomeMunicipality(2L);
        dto.setMunicipalMembership(Membership.none);

        Set<ConstraintViolation<ParticipantUICreateDto>> violations = validator.validate(dto);

        assertThat(violations, hasSize(1));
        assertThat(getFirst(violations).getPropertyPath().toString(), is("municipalMembership"));
        assertThat(getFirst(violations).getMessage(), is("ValidMunicipalMembership"));
    }

    @Test
    public void municipalMembership_can_be_true_if_municipalities_are_not_the_same() {
        ParticipantUICreateDto dto = createParticipantWithNameAndEmail();

        dto.assignMunicipality(1L);
        dto.setHomeMunicipality(2L);
        dto.setMunicipalMembership(Membership.community);

        Set<ConstraintViolation<ParticipantUICreateDto>> violations = validator.validate(dto);

        assertThat(violations, hasSize(0));
    }

    @Test
    public void too_fast_submit_() {

        ParticipantUICreateDto dto = validParticipant();
        dto.setRandomNumber(DateTime.now().minusSeconds(2).getMillis());

        Set<ConstraintViolation<ParticipantUICreateDto>> violations = validator.validate(dto);

        assertThat(violations, hasSize(1));
        assertThat(getFirst(violations).getPropertyPath().toString(), is("randomNumber"));
        assertThat(getFirst(violations).getMessage(), is("{NotTooFastSubmit}"));

    }

    private ParticipantUICreateDto validParticipant() {
        ParticipantUICreateDto dto = createParticipantWithNameAndEmail();

        dto.assignMunicipality(1L);
        dto.setHomeMunicipality(2L);
        dto.setMunicipalMembership(Membership.community);
        return dto;
    }

    private static ConstraintViolation<ParticipantUICreateDto> getFirst(Set<ConstraintViolation<ParticipantUICreateDto>> violations) {
        return violations.iterator().next();
    }

    private ParticipantUICreateDto createParticipantWithNameAndEmail() {
        ParticipantUICreateDto dto = new ParticipantUICreateDto();
        dto.setRandomNumber(DateTime.now().minusMinutes(1).getMillis());
        dto.setParticipantName("Some random name");
        dto.setParticipantEmail("email@example.com");
        return dto;
    }
}
