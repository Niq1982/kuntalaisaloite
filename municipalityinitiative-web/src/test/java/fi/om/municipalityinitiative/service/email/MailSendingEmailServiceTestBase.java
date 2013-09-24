package fi.om.municipalityinitiative.service.email;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.util.InitiativeState;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestFakeEmailConfiguration.class})
public abstract class MailSendingEmailServiceTestBase {

    public static final String INITIATIVE_NAME = "Some name whatever";
    public static final String INITIATIVE_PROPOSAL = "Some proposal whatever";
    public static final String INITIATIVE_MUNICIPALITY = "Some municipality";
    public static final String AUTHOR_PHONE = "Phone number";
    public static final String AUTHOR_EMAIL = "sender.email@example.com";
    public static final String AUTHOR_NAME = "Sender Name";
    public static final String AUTHOR_ADDRESS = "Sender address";
    public static final String MUNICIPALITY_EMAIL = INITIATIVE_MUNICIPALITY.replace(" ","_")+"@example.com"; // @see TestHelper.createTestMunicipality
    public static final String EXTRA_INFO = "Some state comment";
    public static final String MODERATOR_COMMENT = "Some moderator comment";
    public static final String SENT_COMMENT = "Some sent comment";

    @Resource
    protected EmailService emailService;

    @Resource
    protected TestHelper testHelper;

    @Resource
    private MessageSource messageSource;

    private Long testMunicipality;

    @Before
    public void setup() {

        testHelper.dbCleanup();
        testMunicipality = testHelper.createTestMunicipality(INITIATIVE_MUNICIPALITY);
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality)
                .withName(INITIATIVE_NAME)
                .withState(InitiativeState.PUBLISHED)
                .withProposal(INITIATIVE_PROPOSAL)
                .withExtraInfo(EXTRA_INFO)
                .withSentComment(SENT_COMMENT)
                .withModeratorComment(MODERATOR_COMMENT)
                .applyAuthor()
                .withAuthorAddress(AUTHOR_ADDRESS)
                .withAuthorPhone(AUTHOR_PHONE)
                .withParticipantEmail(AUTHOR_EMAIL)
                .withParticipantName(AUTHOR_NAME)
                .toInitiativeDraft());


    }

    public static ContactInfo contactInfo() {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setAddress(AUTHOR_ADDRESS);
        contactInfo.setName(AUTHOR_NAME);
        contactInfo.setEmail(AUTHOR_EMAIL);
        contactInfo.setPhone(AUTHOR_PHONE);
        return contactInfo;
    }


    protected Long authorId() {
        return testHelper.getLastNormalAuthorId().toLong();
    }

    protected Long initiativeId() {
        return testHelper.getLastInitiativeId();
    }

    protected Long getMunicipalityId() {
        return testMunicipality;
    }

    protected String managementHash() {
        return testHelper.getPreviousTestManagementHash();
    }

}
