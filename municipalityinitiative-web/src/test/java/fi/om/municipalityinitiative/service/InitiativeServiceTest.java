package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
import org.junit.Before;

import static org.mockito.Mockito.mock;

public class InitiativeServiceTest {

    private InitiativeService service;
    private InitiativeDao initiativeDao;

    // Currently no unit tests for initiativeservice. @see fi.om.municipalityinitiative.service.InitiativeServiceIntegrationTest

    @Before
    public void setup() {
        initiativeDao = mock(InitiativeDao.class);
        service = new InitiativeService();
        service.initiativeDao = initiativeDao;
        service.municipalityDao = mock(MunicipalityDao.class);
        service.emailService = mock(EmailService.class);
    }

    private InitiativeUICreateDto createDtoFillAllFields() {
        InitiativeUICreateDto createDto = new InitiativeUICreateDto();
        createDto.setFranchise(true);
        createDto.setMunicipalMembership(true);
        createDto.setHomeMunicipality(7L);
        createDto.setMunicipality(15L);
        createDto.setName("name field");
        createDto.setProposal("proposal");
        createDto.setShowName(true);
        createDto.setContactInfo(new ContactInfo());
        createDto.getContactInfo().setAddress("contact address");
        createDto.getContactInfo().setEmail("contact@example.com");
        createDto.getContactInfo().setName("contact name");
        createDto.getContactInfo().setPhone("123456789");
        return createDto;
    }

}
