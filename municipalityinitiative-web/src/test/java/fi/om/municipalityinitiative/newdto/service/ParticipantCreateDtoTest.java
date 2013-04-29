package fi.om.municipalityinitiative.newdto.service;

import fi.om.municipalityinitiative.newdto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ParticipantCreateDtoTest {

    @Test
    public void parse_from_ParticipantUiCreateDto() throws Exception {
        ParticipantUICreateDto uiCreateDto = ReflectionTestUtils.modifyAllFields(new ParticipantUICreateDto());
        uiCreateDto.setHomeMunicipality(uiCreateDto.getMunicipality()); // Set same municipality so franchise may be true

        ParticipantCreateDto participantCreateDto = ParticipantCreateDto.parse(uiCreateDto, 117L);

        assertThat(participantCreateDto.getHomeMunicipality(), is(uiCreateDto.getHomeMunicipality()));
        assertThat(participantCreateDto.getMunicipalityInitiativeId(), is(117L));
        assertThat(participantCreateDto.isShowName(), is(uiCreateDto.getShowName()));
        assertThat(participantCreateDto.getParticipantName(), is(uiCreateDto.getParticipantName()));
        assertThat(participantCreateDto.getEmail(), is(uiCreateDto.getParticipantEmail()));
        assertThat(participantCreateDto.getMunicipalMembership(), is(uiCreateDto.getMunicipalMembership()));

        ReflectionTestUtils.assertNoNullFields(participantCreateDto);
    }

}
