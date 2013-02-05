package fi.om.municipalityinitiative.newdto.service;

import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ParticipantCreateDtoTest {

    @Test
    public void parse_from_uicreatedto() throws Exception {
        InitiativeUICreateDto createDto = ReflectionTestUtils.modifyAllFields(new InitiativeUICreateDto());
        ParticipantCreateDto participantCreateDto = ParticipantCreateDto.parse(createDto, 117L);

        assertThat(participantCreateDto.getHomeMunicipality(), is(createDto.getHomeMunicipality()));
        assertThat(participantCreateDto.getFranchise(), is(true));
        assertThat(participantCreateDto.getMunicipalityInitiativeId(), is(117L));

        assertThat(participantCreateDto.getShowName(), is(createDto.getShowName()));
        assertThat(participantCreateDto.getParticipantName(), is(createDto.getContactInfo().getName()));

        ReflectionTestUtils.assertNoNullFields(participantCreateDto);
    }
}
