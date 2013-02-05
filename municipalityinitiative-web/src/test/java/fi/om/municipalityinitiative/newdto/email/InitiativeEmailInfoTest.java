package fi.om.municipalityinitiative.newdto.email;

import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Test;


public class InitiativeEmailInfoTest {

    @Test
    public void parse_email_info() throws Exception {

        InitiativeViewInfo initiativeViewInfo = ReflectionTestUtils.modifyAllFields(new InitiativeViewInfo());
        ContactInfo contactInfo = ReflectionTestUtils.modifyAllFields(new ContactInfo());

        InitiativeEmailInfo emailInfo = InitiativeEmailInfo.parse(contactInfo, initiativeViewInfo, "https://url.to.initiative");

        ReflectionTestUtils.assertNoNullFields(emailInfo);

    }
}
