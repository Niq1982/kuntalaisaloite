package fi.om.municipalityinitiative.dto.service;

import fi.om.municipalityinitiative.dto.ui.AuthorUIMessage;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AuthorMessageTest {

    @Test
    public void constructor_fills_all_fields() {
        AuthorUIMessage authorUIMessage = ReflectionTestUtils.modifyAllFields(new AuthorUIMessage());

        String confirmationCode = "confirmationCode";
        AuthorMessage authorMessage = new AuthorMessage(authorUIMessage, confirmationCode);

        assertThat(authorMessage.getContactEmail(), is(authorUIMessage.getContactEmail()));
        assertThat(authorMessage.getContactName(), is(authorUIMessage.getContactName()));
        assertThat(authorMessage.getMessage(), is(authorUIMessage.getMessage()));
        assertThat(authorMessage.getInitiativeId(), is(authorUIMessage.getInitiativeId()));
        assertThat(authorMessage.getConfirmationCode(), is(confirmationCode));

        ReflectionTestUtils.assertNoNullFields(authorMessage);
    }
}
