package fi.om.municipalityinitiative.newdto.email;

import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.joda.time.LocalDate;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class InitiativeEmailInfoTest {

    @Test
    public void parse_email_info() throws Exception {

        Initiative initiative = ReflectionTestUtils.modifyAllFields(new Initiative());
        initiative.setSentTime(Maybe.of(new LocalDate(1010, 1, 1))); // TODO: Fix modifyAllFields able to set optional values
        ContactInfo contactInfo = ReflectionTestUtils.modifyAllFields(new ContactInfo());

        String url = "https://url.to.initiative";
        InitiativeEmailInfo emailInfo = InitiativeEmailInfo.parse(contactInfo, InitiativeViewInfo.parse(initiative), url);
        assertThat(emailInfo.getContactInfo().getName(), is(contactInfo.getName()));
        assertThat(emailInfo.getContactInfo().getPhone(), is(contactInfo.getPhone()));
        assertThat(emailInfo.getContactInfo().getAddress(), is(contactInfo.getAddress()));
        assertThat(emailInfo.getContactInfo().getEmail(), is(contactInfo.getEmail()));
        assertThat(emailInfo.getCreateTime(), is(initiative.getCreateTime()));
        assertThat(emailInfo.getMunicipality().getNameFi(), is(initiative.getMunicipality().getNameFi()));
        assertThat(emailInfo.getMunicipality().getId(), is(initiative.getMunicipality().getId()));
        assertThat(emailInfo.getId(), is(initiative.getId()));
        assertThat(emailInfo.getName(), is(initiative.getName()));
        assertThat(emailInfo.getProposal(), is(initiative.getProposal()));
        assertThat(emailInfo.getSentTime(), is(initiative.getSentTime().get()));
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
