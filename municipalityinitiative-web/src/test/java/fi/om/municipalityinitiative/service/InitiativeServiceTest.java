package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdto.InitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.InitiativeUICreateDto;
import fi.om.municipalityinitiative.newdto.ParticipantCreateDto;
import fi.om.municipalityinitiative.newdto.ParticipantUIICreateDto;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class InitiativeServiceTest {

    @Test
    public void parse_municipalityInitiativeCreateDto() {

        InitiativeUICreateDto createDto = createDtoFillAllFields();
        InitiativeCreateDto initiativeCreateDto = InitiativeService.parse(createDto);

        assertThat(initiativeCreateDto.municipalityId, is(createDto.getMunicipality()));
        assertThat(initiativeCreateDto.name, is(createDto.getName()));
        assertThat(initiativeCreateDto.proposal, is(createDto.getProposal()));

        assertThat(initiativeCreateDto.contactAddress, is(createDto.getContactAddress()));
        assertThat(initiativeCreateDto.contactEmail, is(createDto.getContactEmail()));
        assertThat(initiativeCreateDto.contactName, is(createDto.getContactName()));
        assertThat(initiativeCreateDto.contactPhone, is(createDto.getContactPhone()));
    }

    @Test
    public void parse_composerCreateDto() {
        InitiativeUICreateDto createDto = createDtoFillAllFields();
        ParticipantCreateDto participantCreateDto = InitiativeService.parse(createDto, 117L);

        assertThat(participantCreateDto.getHomeMunicipality(), is(createDto.getHomeMunicipality()));
        assertThat(participantCreateDto.getFranchise(), is(true));
        assertThat(participantCreateDto.getMunicipalityInitiativeId(), is(117L));

        assertThat(participantCreateDto.getShowName(), is(createDto.getShowName()));
        assertThat(participantCreateDto.getParticipantName(), is(createDto.getContactName()));
    }

    @Test
    public void create_participant() {

        // TODO:
        ParticipantUIICreateDto participantUIICreateDto = new ParticipantUIICreateDto();
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
        createDto.setContactAddress("contact address");
        createDto.setContactEmail("contact@email.com");
        createDto.setContactName("contact name");
        createDto.setContactPhone("123456789");
        createDto.setShowName(true);
        return createDto;
    }

}
