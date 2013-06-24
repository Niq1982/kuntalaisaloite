package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class InitiativeDraftUIEditDtoTest {

    @Test
    public void parse_from_initiative() {
        Initiative originalInitiative = ReflectionTestUtils.modifyAllFields(new Initiative());
        ContactInfo originalContactInfo = ReflectionTestUtils.modifyAllFields(new ContactInfo());
        InitiativeDraftUIEditDto dto = InitiativeDraftUIEditDto.parse(originalInitiative, originalContactInfo);

        assertThat(dto.getMunicipality().getId(), is(originalInitiative.getMunicipality().getId()));
//        assertThat(dto.getState(), is(originalInitiative.getState()));
        assertThat(dto.getContactInfo().getAddress(), is(originalContactInfo.getAddress()));
        assertThat(dto.getContactInfo().getPhone(), is(originalContactInfo.getPhone()));
        assertThat(dto.getContactInfo().getName(), is(originalContactInfo.getName()));
        assertThat(dto.getContactInfo().getEmail(), is(originalContactInfo.getEmail()));
        assertThat(dto.getExtraInfo(), is(originalInitiative.getExtraInfo()));
        assertThat(dto.getName(), is(originalInitiative.getName()));
        assertThat(dto.getProposal(), is(originalInitiative.getProposal()));
        assertThat(dto.getExternalParticipantCount(), is(originalInitiative.getExternalParticipantCount()));
//        assertThat(dto.getState(), is(originalInitiative.getState()));
        ReflectionTestUtils.assertNoNullFields(dto);

    }
}
