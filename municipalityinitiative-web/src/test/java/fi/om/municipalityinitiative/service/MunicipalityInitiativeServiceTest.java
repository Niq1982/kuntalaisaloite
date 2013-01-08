package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdto.ComposerCreateDto;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.newweb.MunicipalityInitiativeUICreateDto;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class MunicipalityInitiativeServiceTest {

    @Test
    public void parse_municipalityInitiativeCreateDto() {

        MunicipalityInitiativeUICreateDto createDto = createDtoFillAllFields();
        MunicipalityInitiativeCreateDto municipalityInitiativeCreateDto = MunicipalityInitiativeService.parse(createDto);

        assertThat(municipalityInitiativeCreateDto.municipalityId, is(createDto.getMunicipality()));
        assertThat(municipalityInitiativeCreateDto.name, is(createDto.getName()));
        assertThat(municipalityInitiativeCreateDto.proposal, is(createDto.getProposal()));

        assertThat(municipalityInitiativeCreateDto.contactAddress, is(nullValue()));
        assertThat(municipalityInitiativeCreateDto.contactEmail, is(nullValue()));
        assertThat(municipalityInitiativeCreateDto.contactName, is(nullValue()));
        assertThat(municipalityInitiativeCreateDto.contactPhone, is(nullValue()));
    }

    @Test
    public void parse_composerCreateDto() {
        MunicipalityInitiativeUICreateDto createDto = createDtoFillAllFields();
        ComposerCreateDto composerCreateDto = MunicipalityInitiativeService.parse(createDto, 117L);

        assertThat(composerCreateDto.municipalityId, is(createDto.getHomeMunicipality()));
        assertThat(composerCreateDto.right_of_voting, is(true));
        assertThat(composerCreateDto.municipalityInitiativeId, is(117L));

        assertThat(composerCreateDto.showName, is(false)); // TODO: Fix
        assertThat(composerCreateDto.name, is(nullValue()));
    }

    private MunicipalityInitiativeUICreateDto createDtoFillAllFields() {
        MunicipalityInitiativeUICreateDto createDto = new MunicipalityInitiativeUICreateDto();
        createDto.setFranchise(true);
        createDto.setMunicipalMembership(true);
        createDto.setHomeMunicipality(7);
        createDto.setMunicipality(15);
        createDto.setName("name field");
        createDto.setProposal("proposal");
        return createDto;
    }

}
