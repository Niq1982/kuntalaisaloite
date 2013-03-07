package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.exceptions.NotCollectableException;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
import fi.om.municipalityinitiative.newdto.ui.SendToMunicipalityDto;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.web.Urls;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class InitiativeServiceTest {

    private InitiativeService service;
    private InitiativeDao initiativeDao;

    @BeforeClass
    public static void initUrls() {
        // This is pretty stupid that this is needed here. Static configurations are...
        Urls.initUrls("baseUrl");
    }

    @Before
    public void setup() {
        initiativeDao = mock(InitiativeDao.class);
        service = new InitiativeService();
        service.initiativeDao = initiativeDao;
        service.municipalityDao = mock(MunicipalityDao.class);
        service.emailService = mock(EmailService.class);
    }

    @Test
    public void fails_sending_to_municipality_if_not_collectable() {

        Initiative initiative = new Initiative();
        initiative.setManagementHash(Maybe.<String>absent());
        stub(initiativeDao.getById(any(Long.class))).toReturn(initiative);

        try {
            service.sendToMunicipality(0L, null, null);
            fail("Should have thrown exception");
        } catch (NotCollectableException e) {
            assertThat(e.getMessage(), containsString("Initiative is not collectable"));
        }
    }

    @Test
    public void fails_sending_to_municipality_if_already_sent() {

        Initiative initiative = new Initiative();
        initiative.setManagementHash(Maybe.of("anyHash"));
        initiative.setSentTime(Maybe.of(new LocalDate()));
        stub(initiativeDao.getById(any(Long.class))).toReturn(initiative);

        try {
            service.sendToMunicipality(0L, null, null);
            fail("Should have thrown exception");
        } catch (NotCollectableException e) {
            assertThat(e.getMessage(), containsString("Initiative already sent"));
        }
    }

    @Test
    public void fails_sending_to_municipality_if_hashcode_does_not_match() {

        Initiative initiative = new Initiative();
        initiative.setManagementHash(Maybe.of("some hash"));
        stub(initiativeDao.getById(any(Long.class))).toReturn(initiative);

        SendToMunicipalityDto sendToMunicipalityDto = new SendToMunicipalityDto();
        sendToMunicipalityDto.setManagementHash("some OTHER hash");

        try {
            service.sendToMunicipality(0L, sendToMunicipalityDto, null);
            fail("Should have thrown exception");
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), containsString("Invalid management hash"));
        }
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
