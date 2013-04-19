package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class InitiativeDraftUIEditDtoTest {

    @Test
    public void parse_from_initiative() {
        Initiative original = ReflectionTestUtils.modifyAllFields(new Initiative());
        InitiativeDraftUIEditDto dto = InitiativeDraftUIEditDto.parse(original);

        assertThat(dto.getMunicipality().getId(), is(original.getMunicipality().getId()));
        assertThat(dto.getState(), is(original.getState()));
        assertThat(dto.getContactInfo().getAddress(), is(original.getAuthor().getContactInfo().getAddress()));
        assertThat(dto.getContactInfo().getPhone(), is(original.getAuthor().getContactInfo().getPhone()));
        assertThat(dto.getContactInfo().getName(), is(original.getAuthor().getContactInfo().getName()));
        assertThat(dto.getContactInfo().getEmail(), is(original.getAuthor().getContactInfo().getEmail()));
        assertThat(dto.getExtraInfo(), is(original.getComment()));
        assertThat(dto.getName(), is(original.getName()));
        assertThat(dto.getProposal(), is(original.getProposal()));
        assertThat(dto.getState(), is(original.getState()));
        ReflectionTestUtils.assertNoNullFields(dto);

    }
}
