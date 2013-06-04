package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.util.JavaMailSenderFake;
import fi.om.municipalityinitiative.util.SentMailHanderUtil;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestFakeEmailConfiguration.class})
public abstract class MailSendingEmailServiceTestBase {

    public static final String INITIATIVE_NAME = "Some name whatever";
    public static final String INITIATIVE_PROPOSAL = "Some proposal whatever";
    public static final String INITIATIVE_MUNICIPALITY = "Some municipality";
    public static final Long INITIATIVE_ID = 1L;
    public static final long INITIATIVE_MUNICIPALITY_ID = 2L;
    public static final long AUTHOR_ID = 3L;
    public static final String AUTHOR_PHONE = "Phone number";
    public static final String AUTHOR_EMAIL = "sender.email@example.com";
    public static final List<String> AUTHOR_EMAILS = Collections.singletonList(AUTHOR_EMAIL);
    public static final String AUTHOR_NAME = "Sender Name";
    public static final String AUTHOR_ADDRESS = "Sender address";
    public static final String MUNICIPALITY_EMAIL = INITIATIVE_MUNICIPALITY.replace(" ","_")+"@example.com"; // @see TestHelper.createTestMunicipality
    public static final String EXTRA_INFO = "Some state comment";
    public static final String MODERATOR_COMMENT = "Some moderator comment";
    public static final String MANAGEMENT_HASH = "managementHash";
    public static final String SENT_COMMENT = "Some sent comment";

    @Resource
    protected MailSendingEmailService emailService;

    @Resource
    private TestHelper testHelper;

    // This replaces the JavaMailSender used by EmailService.
    // May be used for asserting "sent" emails.
    @Resource
    protected JavaMailSenderFake javaMailSenderFake;

    protected SentMailHanderUtil sentMailHanderUtil = new SentMailHanderUtil();

    private Long testMunicipality;

    @BeforeClass
    public static void beforeClass() throws InterruptedException {
        Thread.sleep(1000); // This is here to make sure old email-sending-tasks have sent their emails.
    }

    @Before
    public void setup() {
        javaMailSenderFake.clearSentMessages();

        testHelper.dbCleanup();
        testMunicipality = testHelper.createTestMunicipality(INITIATIVE_MUNICIPALITY);
        testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality)
                .withName(INITIATIVE_NAME)
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

    protected Initiative createDefaultInitiative() {
        Initiative initiative = new Initiative();
        initiative.setId(testHelper.getLastInitiativeId());
        initiative.setMunicipality(new Municipality(testMunicipality, INITIATIVE_MUNICIPALITY, INITIATIVE_MUNICIPALITY, true));

        initiative.setCreateTime(new LocalDate(2010, 1, 1));
        initiative.setProposal(INITIATIVE_PROPOSAL);
        initiative.setName(INITIATIVE_NAME);
        initiative.setExtraInfo(EXTRA_INFO);
        initiative.setModeratorComment(MODERATOR_COMMENT);
        initiative.setSentComment(SENT_COMMENT);

        return initiative;
    }

    protected List<Author> defaultAuthors() {
        ContactInfo contactInfo = contactInfo();
        Author author = new Author();
        author.setId(testHelper.getLastAuthorId());
        author.setContactInfo(contactInfo);
        author.setMunicipality(new Municipality(INITIATIVE_MUNICIPALITY_ID, INITIATIVE_MUNICIPALITY, INITIATIVE_MUNICIPALITY, true));
        return Collections.singletonList(author);
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
        return testHelper.getLastAuthorId();
    }

}
