package fi.om.municipalityinitiative.newdto;

import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
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

public class InitiativeUICreateDtoTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void no_matter_what_franchise_is_if_not_collecting() {

        InitiativeUICreateDto dto = createInitiativeWithBasicDetails();

        dto.setMunicipality(1L);
        dto.setHomeMunicipality(1L);
        dto.setCollectable(false);

        dto.setFranchise(null);
        assertThat(validator.validate(dto), hasSize(0));

        dto.setFranchise(false);
        assertThat(validator.validate(dto), hasSize(0));
        dto.setFranchise(true);
        assertThat(validator.validate(dto), hasSize(0));

    }

    @Test
    public void franchise_is_needed_if_municipalities_are_same_and_we_are_collecting() {
        InitiativeUICreateDto dto = createInitiativeWithBasicDetails();

        dto.setMunicipality(1L);
        dto.setHomeMunicipality(1L);
        dto.setCollectable(true);

        dto.setFranchise(null);

        Set<ConstraintViolation<InitiativeUICreateDto>> violations = validator.validate(dto);

        assertThat(violations, hasSize(1));
        assertThat(getFirst(violations).getPropertyPath().toString(), is("franchise"));
        assertThat(getFirst(violations).getMessage(), is("ValidCreateFranchise"));
    }

    @Test
    public void franchise_can_be_true_or_false_if_municipalities_are_the_same_and_are_collecting() {
        InitiativeUICreateDto dto = createInitiativeWithBasicDetails();

        dto.setMunicipality(1L);
        dto.setHomeMunicipality(1L);

        dto.setFranchise(false);
        assertThat(validator.validate(dto), hasSize(0));

        dto.setFranchise(true);
        assertThat(validator.validate(dto), hasSize(0));
    }

    @Test
    public void municipalMembership_is_needed_if_municipalities_are_not_the_same() {
        InitiativeUICreateDto dto = createInitiativeWithBasicDetails();

        dto.setMunicipality(1L);
        dto.setHomeMunicipality(2L);
        dto.setMunicipalMembership(null);

        Set<ConstraintViolation<InitiativeUICreateDto>> violations = validator.validate(dto);

        assertThat(violations, hasSize(1));
        assertThat(getFirst(violations).getPropertyPath().toString(), is("municipalMembership"));
        assertThat(getFirst(violations).getMessage(), is("ValidMunicipalMembership"));
    }


    @Test
    public void municipalMembership_cannot_be_false_if_municipalities_are_not_the_same() {
        InitiativeUICreateDto dto = createInitiativeWithBasicDetails();

        dto.setMunicipality(1L);
        dto.setHomeMunicipality(2L);
        dto.setMunicipalMembership(false);

        Set<ConstraintViolation<InitiativeUICreateDto>> violations = validator.validate(dto);

        assertThat(violations, hasSize(1));
        assertThat(getFirst(violations).getPropertyPath().toString(), is("municipalMembership"));
        assertThat(getFirst(violations).getMessage(), is("ValidMunicipalMembership"));
    }

    @Test
    public void municipalMembership_can_be_true_if_municipalities_are_not_the_same() {
        InitiativeUICreateDto dto = createInitiativeWithBasicDetails();

        dto.setMunicipality(1L);
        dto.setHomeMunicipality(2L);
        dto.setMunicipalMembership(true);

        Set<ConstraintViolation<InitiativeUICreateDto>> violations = validator.validate(dto);

        assertThat(violations, hasSize(0));
    }

    private InitiativeUICreateDto createInitiativeWithBasicDetails() {
        InitiativeUICreateDto dto = new InitiativeUICreateDto();
        dto.setName("Some initiative name");
        dto.setProposal("Some initiative content");
        dto.setContactInfo(new ContactInfo());
        dto.getContactInfo().setEmail("some@email.com");
        dto.getContactInfo().setName("Some contact name");
        return dto;
    }

    private ConstraintViolation<InitiativeUICreateDto> getFirst(Set<ConstraintViolation<InitiativeUICreateDto>> violations) {
        return violations.iterator().next();
    }
}
