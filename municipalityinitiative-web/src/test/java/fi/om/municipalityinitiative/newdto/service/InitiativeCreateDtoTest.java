package fi.om.municipalityinitiative.newdto.service;

import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class InitiativeCreateDtoTest {

    @Test
    public void parse_municipalityInitiativeCreateDto() throws Exception {

        InitiativeUICreateDto createDto = ReflectionTestUtils.modifyAllFields(new InitiativeUICreateDto());
        InitiativeCreateDto initiativeCreateDto = InitiativeCreateDto.parse(createDto);

        assertThat(initiativeCreateDto.municipalityId, is(createDto.getMunicipality()));
        assertThat(initiativeCreateDto.name, is(createDto.getName()));
        assertThat(initiativeCreateDto.proposal, is(createDto.getProposal()));

        assertThat(initiativeCreateDto.contactAddress, is(createDto.getContactInfo().getAddress()));
        assertThat(initiativeCreateDto.contactEmail, is(createDto.getContactInfo().getEmail()));
        assertThat(initiativeCreateDto.contactName, is(createDto.getContactInfo().getName()));
        assertThat(initiativeCreateDto.contactPhone, is(createDto.getContactInfo().getPhone()));

        initiativeCreateDto.managementHash = Maybe.absent(); // This is supposed to be null so setting value for notNullCheck
        ReflectionTestUtils.assertNoNullFields(initiativeCreateDto);

    }
}
