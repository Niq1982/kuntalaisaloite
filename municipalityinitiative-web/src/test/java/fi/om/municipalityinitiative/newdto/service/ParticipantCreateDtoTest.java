package fi.om.municipalityinitiative.newdto.service;

import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
import fi.om.municipalityinitiative.newdto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ParticipantCreateDtoTest {

    @Test
    public void parse_from_InitiativeUiCreateDto() throws Exception {
        InitiativeUICreateDto uiCreateDto = ReflectionTestUtils.modifyAllFields(new InitiativeUICreateDto());
        uiCreateDto.setHomeMunicipality(uiCreateDto.getMunicipality()); // Set same municipality so franchise may be true

        ParticipantCreateDto participantCreateDto = ParticipantCreateDto.parse(uiCreateDto, 117L);

        assertThat(participantCreateDto.getHomeMunicipality(), is(uiCreateDto.getHomeMunicipality()));
        assertThat(participantCreateDto.isFranchise(), is(true));
        assertThat(participantCreateDto.getMunicipalityInitiativeId(), is(117L));
        assertThat(participantCreateDto.isShowName(), is(uiCreateDto.getShowName()));
        assertThat(participantCreateDto.getParticipantName(), is(uiCreateDto.getContactInfo().getName()));

        ReflectionTestUtils.assertNoNullFields(participantCreateDto);
    }

    @Test
    public void parse_from_ParticipantUiCreateDto() throws Exception {
        ParticipantUICreateDto uiCreateDto = ReflectionTestUtils.modifyAllFields(new ParticipantUICreateDto());
        uiCreateDto.setHomeMunicipality(uiCreateDto.getMunicipality()); // Set same municipality so franchise may be true

        ParticipantCreateDto participantCreateDto = ParticipantCreateDto.parse(uiCreateDto, 117L);

        assertThat(participantCreateDto.getHomeMunicipality(), is(uiCreateDto.getHomeMunicipality()));
        assertThat(participantCreateDto.isFranchise(), is(true));
        assertThat(participantCreateDto.getMunicipalityInitiativeId(), is(117L));
        assertThat(participantCreateDto.isShowName(), is(uiCreateDto.getShowName()));
        assertThat(participantCreateDto.getParticipantName(), is(uiCreateDto.getParticipantName()));

        ReflectionTestUtils.assertNoNullFields(participantCreateDto);
    }

    @Test
    public void participantUiCreateDto_franchise_is_always_false_if_municipalities_differ() throws IllegalAccessException {
        ParticipantUICreateDto uiCreateDto = new ParticipantUICreateDto();
        uiCreateDto.setFranchise(true);
        uiCreateDto.setMunicipality(99L);

        uiCreateDto.setHomeMunicipality(100L);
        assertThat(ParticipantCreateDto.parse(uiCreateDto, 0L).isFranchise(), is(false));

        uiCreateDto.setHomeMunicipality(99L);
        assertThat(ParticipantCreateDto.parse(uiCreateDto, 0L).isFranchise(), is(true));
    }

    @Test
    public void initiativeUiCreateDto_franchise_is_always_false_if_municipalities_differ() throws IllegalAccessException {
        ParticipantUICreateDto uiCreateDto = new ParticipantUICreateDto();
        uiCreateDto.setFranchise(true);
        uiCreateDto.setMunicipality(99L);

        uiCreateDto.setHomeMunicipality(100L);
        assertThat(ParticipantCreateDto.parse(uiCreateDto, 0L).isFranchise(), is(false));

        uiCreateDto.setHomeMunicipality(99L);
        assertThat(ParticipantCreateDto.parse(uiCreateDto, 0L).isFranchise(), is(true));
    }

}
