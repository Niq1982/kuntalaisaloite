package fi.om.municipalityinitiative.newdto.email;

import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class InitiativeEmailInfoTest {

    @Test
    public void parse_email_info() throws Exception {

        InitiativeViewInfo initiativeViewInfo = ReflectionTestUtils.modifyAllFields(new InitiativeViewInfo());
        initiativeViewInfo.setSentTime(Maybe.of(new DateTime(1010, 1, 1, 0, 0))); // TODO: Fix modifyAllFields able to set optional values
        ContactInfo contactInfo = ReflectionTestUtils.modifyAllFields(new ContactInfo());

        String url = "https://url.to.initiative";
        InitiativeEmailInfo emailInfo = InitiativeEmailInfo.parse(contactInfo, initiativeViewInfo, url);
        assertThat(emailInfo.getContactInfo().getName(), is(contactInfo.getName()));
        assertThat(emailInfo.getContactInfo().getPhone(), is(contactInfo.getPhone()));
        assertThat(emailInfo.getContactInfo().getAddress(), is(contactInfo.getAddress()));
        assertThat(emailInfo.getContactInfo().getEmail(), is(contactInfo.getEmail()));
        assertThat(emailInfo.getCreateTime(), is(initiativeViewInfo.getCreateTime()));
        assertThat(emailInfo.getMunicipalityName(), is(initiativeViewInfo.getMunicipalityName()));
        assertThat(emailInfo.getName(), is(initiativeViewInfo.getName()));
        assertThat(emailInfo.getProposal(), is(initiativeViewInfo.getProposal()));
        assertThat(emailInfo.getSentTime(), is(initiativeViewInfo.getSentTime().get()));
        assertThat(emailInfo.getUrl(), is(url));

        ReflectionTestUtils.assertNoNullFields(emailInfo);

    }
}
