package fi.om.municipalityinitiative.newdto.email;

import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.joda.time.DateTime;
import org.junit.Test;


public class InitiativeEmailInfoTest {

    @Test
    public void parse_email_info() throws Exception {

        InitiativeViewInfo initiativeViewInfo = ReflectionTestUtils.modifyAllFields(new InitiativeViewInfo());
        initiativeViewInfo.setSentTime(Maybe.of(new DateTime(1010, 1, 1, 0, 0))); // TODO: Fix modifyAllFields able to set optional values
        ContactInfo contactInfo = ReflectionTestUtils.modifyAllFields(new ContactInfo());

        InitiativeEmailInfo emailInfo = InitiativeEmailInfo.parse(contactInfo, initiativeViewInfo, "https://url.to.initiative");

        ReflectionTestUtils.assertNoNullFields(emailInfo);

    }
}
