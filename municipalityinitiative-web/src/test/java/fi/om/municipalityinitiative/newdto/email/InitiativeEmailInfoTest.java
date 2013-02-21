package fi.om.municipalityinitiative.newdto.email;

import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.joda.time.LocalDate;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class InitiativeEmailInfoTest {

    @Test
    public void parse_email_info() throws Exception {

        InitiativeViewInfo initiativeViewInfo = ReflectionTestUtils.modifyAllFields(new InitiativeViewInfo());
        initiativeViewInfo.setSentTime(Maybe.of(new LocalDate(1010, 1, 1))); // TODO: Fix modifyAllFields able to set optional values
        ContactInfo contactInfo = ReflectionTestUtils.modifyAllFields(new ContactInfo());

        String url = "https://url.to.initiative";
        InitiativeEmailInfo emailInfo = InitiativeEmailInfo.parse(contactInfo, initiativeViewInfo, url);
        assertThat(emailInfo.getContactInfo().getName(), is(contactInfo.getName()));
        assertThat(emailInfo.getContactInfo().getPhone(), is(contactInfo.getPhone()));
        assertThat(emailInfo.getContactInfo().getAddress(), is(contactInfo.getAddress()));
        assertThat(emailInfo.getContactInfo().getEmail(), is(contactInfo.getEmail()));
        assertThat(emailInfo.getCreateTime(), is(initiativeViewInfo.getCreateTime()));
        assertThat(emailInfo.getMunicipalityName(), is(initiativeViewInfo.getMunicipality().getName()));
        assertThat(emailInfo.getId(), is(initiativeViewInfo.getId()));
        assertThat(emailInfo.getName(), is(initiativeViewInfo.getName()));
        assertThat(emailInfo.getProposal(), is(initiativeViewInfo.getProposal()));
        assertThat(emailInfo.getSentTime(), is(initiativeViewInfo.getSentTime().get()));
        assertThat(emailInfo.getUrl(), is(url));

        ReflectionTestUtils.assertNoNullFields(emailInfo);

    }

    @Test
    public void constuctor_fill_all_fields() {
        InitiativeEmailInfo original = ReflectionTestUtils.modifyAllFields(new InitiativeEmailInfo());
        InitiativeEmailInfo copy = new InitiativeEmailInfo(original);
        ReflectionTestUtils.assertNoNullFields(copy);
        ReflectionTestUtils.assertReflectionEquals(copy, original);
    }
}
