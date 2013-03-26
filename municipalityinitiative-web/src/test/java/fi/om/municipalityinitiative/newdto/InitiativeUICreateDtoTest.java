package fi.om.municipalityinitiative.newdto;

import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
import org.joda.time.DateTime;
import org.junit.BeforeClass;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class InitiativeUICreateDtoTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }



    private InitiativeUICreateDto createInitiativeWithBasicDetails() {
        InitiativeUICreateDto dto = new InitiativeUICreateDto();
        dto.setName("Some initiative name");
        dto.setProposal("Some initiative content");
        dto.setContactInfo(new ContactInfo());
        dto.getContactInfo().setEmail("some@example.com");
        dto.getContactInfo().setName("Some contact name");
        dto.setRandomNumber(DateTime.now().minusMinutes(1).getMillis());
        return dto;
    }

    private InitiativeUICreateDto createValidInitiativeWithBasicDetails() {
        InitiativeUICreateDto dto = createInitiativeWithBasicDetails();
        dto.setMunicipality(1L);
        dto.setHomeMunicipality(2L);
        dto.setMunicipalMembership(true);
        return dto;
    }


}
