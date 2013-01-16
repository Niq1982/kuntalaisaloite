package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.ParticipantCreateDto;
import fi.om.municipalityinitiative.newweb.MunicipalityInitiativeUICreateDto;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MunicipalityInitiativeServiceTest {

    @Test
    public void parse_municipalityInitiativeCreateDto() {

        MunicipalityInitiativeUICreateDto createDto = createDtoFillAllFields();
        MunicipalityInitiativeCreateDto municipalityInitiativeCreateDto = MunicipalityInitiativeService.parse(createDto);

        assertThat(municipalityInitiativeCreateDto.municipalityId, is(createDto.getMunicipality()));
        assertThat(municipalityInitiativeCreateDto.name, is(createDto.getName()));
        assertThat(municipalityInitiativeCreateDto.proposal, is(createDto.getProposal()));

        assertThat(municipalityInitiativeCreateDto.contactAddress, is(createDto.getContactAddress()));
        assertThat(municipalityInitiativeCreateDto.contactEmail, is(createDto.getContactEmail()));
        assertThat(municipalityInitiativeCreateDto.contactName, is(createDto.getContactName()));
        assertThat(municipalityInitiativeCreateDto.contactPhone, is(createDto.getContactPhone()));
    }

    @Test
    public void parse_composerCreateDto() {
        MunicipalityInitiativeUICreateDto createDto = createDtoFillAllFields();
        ParticipantCreateDto participantCreateDto = MunicipalityInitiativeService.parse(createDto, 117L);

        assertThat(participantCreateDto.municipalityId, is(createDto.getHomeMunicipality()));
        assertThat(participantCreateDto.franchise, is(true));
        assertThat(participantCreateDto.municipalityInitiativeId, is(117L));

        assertThat(participantCreateDto.showName, is(createDto.getShowName())); // TODO: Fix XXX what?
        assertThat(participantCreateDto.name, is(createDto.getContactName()));
    }

    private MunicipalityInitiativeUICreateDto createDtoFillAllFields() {
        MunicipalityInitiativeUICreateDto createDto = new MunicipalityInitiativeUICreateDto();
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
