package fi.om.municipalityinitiative.service.email;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dao.MunicipalityUserDao;
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

    public static final String INITIATIVE_NAME = "Koiria paljon helsinkiin";
    public static final String INITIATIVE_PROPOSAL = "Niita olisi saatava nyt joka paikkaan";
    public static final String INITIATIVE_MUNICIPALITY = "Helsinki";
    public static final String AUTHOR_PHONE = "04040404";
    public static final String AUTHOR_EMAIL = "sender.email@example.com";
    public static final String AUTHOR_NAME = "Pakkeli Kakkeli";
    public static final String AUTHOR_ADDRESS = "Joku katu 4C3";
    public static final String MUNICIPALITY_EMAIL = INITIATIVE_MUNICIPALITY.replace(" ","_")+"@example.com"; // @see TestHelper.createTestMunicipality
    public static final String EXTRA_INFO = "www.lisatiedot.comt";
    public static final String MODERATOR_COMMENT = "Ruma on aloite";
    public static final String SENT_COMMENT = "Terveisia kunnalle";

    @Resource
    protected EmailService emailService;

    @Resource
    protected TestHelper testHelper;

    @Resource
    private MessageSource messageSource;

    @Resource
    protected MunicipalityUserDao municipalityUserDao;

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
