package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Location;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
public class InitiativeDraftUIEditDtoTest {

    @Resource
    TestHelper testHelper;

    @Test
    public void parse_from_initiative() {
        Initiative originalInitiative = ReflectionTestUtils.modifyAllFields(new Initiative());
        originalInitiative.setVideoUrl("videourl");
        ContactInfo originalContactInfo = ReflectionTestUtils.modifyAllFields(new ContactInfo());

        List<Location> locations = new ArrayList<>();
        locations.add(ReflectionTestUtils.modifyAllFields(new Location()));

        InitiativeDraftUIEditDto dto = InitiativeDraftUIEditDto.parse(originalInitiative, originalContactInfo, locations);

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

        testHelper.assertLocations(dto.getLocations(), locations);

//        assertThat(dto.getState(), is(originalInitiative.getState()));
        ReflectionTestUtils.assertNoNullFields(dto);

    }
}
