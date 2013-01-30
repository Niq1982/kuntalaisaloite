package fi.om.municipalityinitiative.service;

import com.google.common.base.Optional;
import fi.om.municipalityinitiative.exceptions.NotCollectableException;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdto.service.InitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.newdto.ui.ParticipantUICreateDto;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class InitiativeServiceTest {

    @Test
    public void parse_municipalityInitiativeCreateDto() {

        InitiativeUICreateDto createDto = createDtoFillAllFields();
        InitiativeCreateDto initiativeCreateDto = InitiativeService.parse(createDto);

        assertThat(initiativeCreateDto.municipalityId, is(createDto.getMunicipality()));
        assertThat(initiativeCreateDto.name, is(createDto.getName()));
        assertThat(initiativeCreateDto.proposal, is(createDto.getProposal()));

        assertThat(initiativeCreateDto.contactAddress, is(createDto.getContactInfo().getAddress()));
        assertThat(initiativeCreateDto.contactEmail, is(createDto.getContactInfo().getEmail()));
        assertThat(initiativeCreateDto.contactName, is(createDto.getContactInfo().getName()));
        assertThat(initiativeCreateDto.contactPhone, is(createDto.getContactInfo().getPhone()));
    }

    @Test
    public void parse_participantCreateDto() {
        InitiativeUICreateDto createDto = createDtoFillAllFields();
        ParticipantCreateDto participantCreateDto = InitiativeService.parse(createDto, 117L);

        assertThat(participantCreateDto.getHomeMunicipality(), is(createDto.getHomeMunicipality()));
        assertThat(participantCreateDto.getFranchise(), is(true));
        assertThat(participantCreateDto.getMunicipalityInitiativeId(), is(117L));

        assertThat(participantCreateDto.getShowName(), is(createDto.getShowName()));
        assertThat(participantCreateDto.getParticipantName(), is(createDto.getContactInfo().getName()));
    }

    @Test
    public void fails_sending_to_municipality_if_not_collectable() {
        InitiativeDao initiativeDao = mock(InitiativeDao.class);
        InitiativeService service = new InitiativeService();
        service.initiativeDao = initiativeDao;

        InitiativeViewInfo initiativeViewInfo = new InitiativeViewInfo();
        initiativeViewInfo.setMaybeManagementHash(Optional.<String>absent());
        stub(initiativeDao.getById(any(Long.class))).toReturn(initiativeViewInfo);

        try {
            service.sendToMunicipality(0L, "anyhashcode");
            fail("Should have thrown exception");
        } catch (NotCollectableException e) {
            assertThat(e.getMessage(), containsString("Initiative is not collectable"));
        }
    }

    @Test
    public void fails_sending_to_municipality_if_already_sent() {
        InitiativeDao initiativeDao = mock(InitiativeDao.class);
        InitiativeService service = new InitiativeService();
        service.initiativeDao = initiativeDao;

        InitiativeViewInfo initiativeViewInfo = new InitiativeViewInfo();
        initiativeViewInfo.setMaybeManagementHash(Optional.of("anyHash"));
        initiativeViewInfo.setSentTime(Optional.of(new DateTime()));
        stub(initiativeDao.getById(any(Long.class))).toReturn(initiativeViewInfo);

        try {
            service.sendToMunicipality(0L, "anyOtherHashCode");
            fail("Should have thrown exception");
        } catch (NotCollectableException e) {
            assertThat(e.getMessage(), containsString("Initiative already sent"));
        }
    }

    @Test
    public void fails_sending_to_municipality_if_hashcode_does_not_match() {
        InitiativeDao initiativeDao = mock(InitiativeDao.class);
        InitiativeService service = new InitiativeService();
        service.initiativeDao = initiativeDao;

        InitiativeViewInfo initiativeViewInfo = new InitiativeViewInfo();
        initiativeViewInfo.setMaybeManagementHash(Optional.of("some hash"));
        stub(initiativeDao.getById(any(Long.class))).toReturn(initiativeViewInfo);

        try {
            service.sendToMunicipality(0L, "another hash");
            fail("Should have thrown exception");
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), containsString("Invalid initiative verifier"));
        }
    }

    @Test
    public void succeeds_in_sending_to_municipality() {

        InitiativeDao initiativeDao = mock(InitiativeDao.class);
        InitiativeService service = new InitiativeService();
        service.initiativeDao = initiativeDao;

        InitiativeViewInfo initiativeViewInfo = new InitiativeViewInfo();
        initiativeViewInfo.setMaybeManagementHash(Optional.of("hashCode"));
        stub(initiativeDao.getById(any(Long.class))).toReturn(initiativeViewInfo);

        service.sendToMunicipality(0L, "hashCode");

        verify(initiativeDao).markAsSended(0L);
    }

    @Test
    public void create_participant() {

        // TODO:
        ParticipantUICreateDto participantUICreateDto = new ParticipantUICreateDto();
        InitiativeService service = new InitiativeService();
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
        createDto.getContactInfo().setEmail("contact@email.com");
        createDto.getContactInfo().setName("contact name");
        createDto.getContactInfo().setPhone("123456789");
        return createDto;
    }

}
