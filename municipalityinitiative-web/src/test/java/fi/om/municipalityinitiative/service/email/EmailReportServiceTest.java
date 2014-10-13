package fi.om.municipalityinitiative.service.email;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import fi.om.municipalityinitiative.web.Urls;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestFakeEmailConfiguration.class})
public class EmailReportServiceTest {

    @Resource
    private EmailReportService emailReportService;

    @Resource
    private TestHelper testHelper;

    private Long testMunicipality;

    private Urls urls;

    @Before
    public void setup() {
        testHelper.dbCleanup();
        urls = Urls.get(Locales.LOCALE_FI);
        testMunicipality = testHelper.createTestMunicipality("Some municipality");
    }

    @Test
    public void sends_accepted_but_not_published_emails_once() {

        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality)
                .withState(InitiativeState.ACCEPTED)
                .withStateTime(new DateTime().minusDays(15))
                .applyAuthor()
                .withParticipantEmail("author@example.com")
                .toInitiativeDraft());

        emailReportService.sendReportEmailsForInitiativesAcceptedButNotPublished();
        EmailDto singleQueuedEmail = testHelper.getSingleQueuedEmail();
        assertThat(singleQueuedEmail.getSubject(), containsString("Aloitteesi odottaa vielä julkaisua"));
        assertThat(singleQueuedEmail.getBodyHtml(), containsString(urls.loginAuthor(RandomHashGenerator.getPrevious())));
        assertThat(singleQueuedEmail.getRecipientsAsString(), is("author@example.com"));

        emailReportService.sendReportEmailsForInitiativesAcceptedButNotPublished();
        assertThat(testHelper.findQueuedEmails(), hasSize(1)); // Is not sent again
    }

    @Test
    public void sends_accepted_but_not_published_for_verified_initiatives() {
        Long verifiedInitiative = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality)
                .withState(InitiativeState.ACCEPTED)
                .withStateTime(new DateTime().minusDays(15))
                .withType(InitiativeType.COLLABORATIVE_COUNCIL)
                .applyAuthor()
                .withParticipantEmail("author@example.com")
                .toInitiativeDraft());

        emailReportService.sendReportEmailsForInitiativesAcceptedButNotPublished();
        EmailDto singleQueuedEmail = testHelper.getSingleQueuedEmail();
        assertThat(singleQueuedEmail.getRecipientsAsString(), is("author@example.com"));
        assertThat(singleQueuedEmail.getSubject(), containsString("Aloitteesi odottaa vielä julkaisua"));
        assertThat(singleQueuedEmail.getBodyHtml(), containsString(urls.loginToManagement(verifiedInitiative)));

        emailReportService.sendReportEmailsForInitiativesAcceptedButNotPublished();
        assertThat(testHelper.findQueuedEmails(), hasSize(1)); // Is not sent again
    }
}
