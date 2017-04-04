package fi.om.municipalityinitiative.service;


import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.MunicipalityUserDao;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.AttachmentFile;
import fi.om.municipalityinitiative.dto.service.AttachmentFileBase;
import fi.om.municipalityinitiative.dto.service.DecisionAttachmentFile;
import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.dto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.dto.ui.MunicipalityDecisionDto;
import fi.om.municipalityinitiative.dto.user.MunicipalityUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.exceptions.FileUploadException;
import fi.om.municipalityinitiative.exceptions.InvalidAttachmentException;
import fi.om.municipalityinitiative.service.ui.MunicipalityDecisionInfo;
import fi.om.municipalityinitiative.service.ui.NormalInitiativeService;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.hash.PreviousHashGetter;
import fi.om.municipalityinitiative.util.hash.RandomHashGenerator;
import org.apache.commons.io.FileUtils;
import org.aspectj.util.FileUtil;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class MunicipalityDecisionServiceIntegrationTest extends ServiceIntegrationTestBase  {


    public static final String MUNICIPALITY_NAME = "Some municipality";
    public static final String MUNICIPALITY_EMAIL = TestHelper.toEmail(MUNICIPALITY_NAME);

    @Resource
    protected MunicipalityDecisionService municipalityDecisionService;

    @Resource
    private NormalInitiativeService normalInitiativeService;

    @Resource
    private MunicipalityUserDao municipalityUserDao;

    protected Long testMunicipalityId;

    protected String DECISION_DESCRIPTION = "Kunnalla ei ole rahaa.";

    protected static String CUSTOM_ATTACHMENT_NAME_GIVEN_BY_USER = "Custom name given by user";

    protected static final File TEST_PDF_FILE = new File(System.getProperty("user.dir") + "/src/test/resources/testi.pdf");

    protected static String TESTI_PDF = "test.pdf";

    protected static String CONTENT_TYPE = "application/pdf";

    protected static String FILE_TYPE = "pdf";

    @Resource
    private InitiativeDao initiativeDao;


    @Override
    protected void childSetup()  {
        testMunicipalityId = testHelper.createTestMunicipality(MUNICIPALITY_NAME);
    }

    @Test
    public void save_decision_and_get_decision() {
        Long initiativeId = createSentVerifiedInitiativeWithAuthor();

        DecisionAttachmentFile fileInfo = null;
        try {

            MunicipalityDecisionDto decision = createDefaultMunicipalityDecisionWithAttachment(initiativeId);

            AttachmentUtil.Attachments decisionAttachments = municipalityDecisionService.getDecisionAttachments(initiativeId);

            assertThat(decisionAttachments.getAll().size(), is(1));

            fileInfo = (DecisionAttachmentFile)decisionAttachments.getAll().get(0);



        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            assertThat(fileInfo.getFileType(), is(FILE_TYPE));

            assertThat(fileInfo.getContentType(), is(CONTENT_TYPE));

            assertThat(fileInfo.getFileName(), is(CUSTOM_ATTACHMENT_NAME_GIVEN_BY_USER));

            assertThat(fileInfo.getInitiativeId(), is(initiativeId));

            InitiativeViewInfo initiative = normalInitiativeService.getInitiative(initiativeId, new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId)));

            assertThat(initiative.getDecisionText().isPresent(), is(true));

            assertThat(initiative.getDecisionDate().isPresent(), is(true));

            assertThat(initiative.getDecisionModifiedDate().isNotPresent(), is(true));

            assertThat(initiative.getDecisionText().getValue(), is(DECISION_DESCRIPTION));


        }

    }


    @Test
    public void save_decision_and_get_attachment() {
        Long initiativeId = createSentVerifiedInitiativeWithAuthor();

        DecisionAttachmentFile fileInfo = null;
        AttachmentFile attachmentFile = null;
        try {

            MunicipalityDecisionDto decision = createDefaultMunicipalityDecisionWithAttachment(initiativeId);

            AttachmentUtil.Attachments decisionAttachments = municipalityDecisionService.getDecisionAttachments(initiativeId);

            assertThat(decisionAttachments.getPdfs().size(), is(1));

            fileInfo = (DecisionAttachmentFile)decisionAttachments.getPdfs().get(0);

            attachmentFile = municipalityDecisionService.getAttachment(fileInfo.getAttachmentId(), CUSTOM_ATTACHMENT_NAME_GIVEN_BY_USER, new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId)));


        } catch (Exception e) {
            e.printStackTrace();


        } finally {
            assertThat(attachmentFile, notNullValue());
            assertThat(attachmentFile.getInitiativeId(), is(initiativeId));
            assertThat(attachmentFile.getAttachmentId(), is(fileInfo.getAttachmentId()));
            assertThat(attachmentFile.getFileName(), is(CUSTOM_ATTACHMENT_NAME_GIVEN_BY_USER));
            assertThat(attachmentFile.getContentType(), is(CONTENT_TYPE));



        }

    }
    @Test
    public void remove_attachment_from_decision() {

        Long initiativeId = createSentVerifiedInitiativeWithAuthor();

        MunicipalityDecisionDto decision;

        try {

            decision = createDefaultMunicipalityDecisionWithAttachment(initiativeId);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            AttachmentUtil.Attachments decisionAttachments = municipalityDecisionService.getDecisionAttachments(initiativeId);

            assertThat(decisionAttachments.getAll().size(), is(1));

            DecisionAttachmentFile fileInfo = (DecisionAttachmentFile) decisionAttachments.getAll().get(0);

            municipalityDecisionService.removeAttachmentFromDecision(fileInfo.getAttachmentId(), new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId)));

            decisionAttachments = municipalityDecisionService.getDecisionAttachments(initiativeId);

            assertThat(decisionAttachments.getAll().size(), is(0));

            InitiativeViewInfo initiative = normalInitiativeService.getInitiative(initiativeId, new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId)));

            assertThat(initiative.getDecisionModifiedDate().isPresent(), is(true));
        }
    }

    @Test
    public void cant_remove_attachment_from_decision_if_not_correct_municipality_user() {

        Long initiativeId = createSentVerifiedInitiativeWithAuthor();

        try {

            createDefaultMunicipalityDecisionWithAttachment(initiativeId);

            AttachmentUtil.Attachments decisionAttachments = municipalityDecisionService.getDecisionAttachments(initiativeId);

            assertThat(decisionAttachments.getAll().size(), is(1));

            DecisionAttachmentFile fileInfo = (DecisionAttachmentFile) decisionAttachments.getAll().get(0);

            municipalityDecisionService.removeAttachmentFromDecision(fileInfo.getAttachmentId(), new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId + 1)));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            AttachmentUtil.Attachments decisionAttachments = municipalityDecisionService.getDecisionAttachments(initiativeId);

            assertThat(decisionAttachments.getAll().size(), is(1));

            InitiativeViewInfo initiative = normalInitiativeService.getInitiative(initiativeId, new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId)));

            assertThat(initiative.getDecisionModifiedDate().isNotPresent(), is(true));
        }
    }
    @Test
    public void can_edit_decision() {

        Long initiativeId = createSentVerifiedInitiativeWithAuthor();

        try {

            createDefaultMunicipalityDecisionWithAttachment(initiativeId);

            MunicipalityDecisionDto editedDecision = MunicipalityDecisionDto.build(Maybe.of("Edited text"));

            municipalityDecisionService.setDecision(editedDecision, initiativeId, new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId)), new Locale("fi"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            InitiativeViewInfo initiative = normalInitiativeService.getInitiative(initiativeId, new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId)));

            assertThat(initiative.getDecisionText().getValue(), is("Edited text"));

            assertThat(initiative.getDecisionModifiedDate().isPresent(), is(true));

        }
    }

    @Test
    public void cant_edit_decision_if_no_access() {

        Long initiativeId = createSentVerifiedInitiativeWithAuthor();

        try {

            createDefaultMunicipalityDecisionWithAttachment(initiativeId);

            MunicipalityDecisionDto editedDecision = MunicipalityDecisionDto.build(Maybe.of("Edited text"));

            municipalityDecisionService.setDecision(editedDecision, initiativeId, new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId + 1)), new Locale("fi"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            InitiativeViewInfo initiative = normalInitiativeService.getInitiative(initiativeId, new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId)));

            assertThat(initiative.getDecisionText().getValue(), is(DECISION_DESCRIPTION));

            assertThat(initiative.getDecisionModifiedDate().isNotPresent(), is(true));
        }
    }
    @Test
    public void can_add_attachments_to_decision() {

        Long initiativeId = createSentVerifiedInitiativeWithAuthor();

        try {

            createDefaultMunicipalityDecisionWithAttachment(initiativeId);

            municipalityDecisionService.updateAttachments(initiativeId, getFileWithNames(), new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId)));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            InitiativeViewInfo initiative = normalInitiativeService.getInitiative(initiativeId, new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId)));

            assertThat(initiative.getDecisionText().getValue(), is(DECISION_DESCRIPTION));

            assertThat(initiative.getDecisionModifiedDate().isPresent(), is(true));

            AttachmentUtil.Attachments decisionAttachments = municipalityDecisionService.getDecisionAttachments(initiativeId);

            assertThat(decisionAttachments.getAll().size(), is(2));
        }
    }

    @Test
    public void cant_add_attachments_to_decision_if_no_access() {

        Long initiativeId = createSentVerifiedInitiativeWithAuthor();

        try {

            createDefaultMunicipalityDecisionWithAttachment(initiativeId);

            municipalityDecisionService.updateAttachments(initiativeId, getFileWithNames(), new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId + 1)));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            InitiativeViewInfo initiative = normalInitiativeService.getInitiative(initiativeId, new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId)));

            assertThat(initiative.getDecisionText().getValue(), is(DECISION_DESCRIPTION));

            assertThat(initiative.getDecisionModifiedDate().isNotPresent(), is(true));

            AttachmentUtil.Attachments decisionAttachments = municipalityDecisionService.getDecisionAttachments(initiativeId);

            assertThat(decisionAttachments.getAll().size(), is(1));
        }
    }

    @Test
    public void cant_add_attachments_to_decision_if_decision_doesnt_exist() {

        Long initiativeId = createSentVerifiedInitiativeWithAuthor();

        try {

            municipalityDecisionService.updateAttachments(initiativeId, getFileWithNames(), new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId)));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            InitiativeViewInfo initiative = normalInitiativeService.getInitiative(initiativeId, new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId)));

            assertThat(initiative.getDecisionModifiedDate().isNotPresent(), is(true));

            AttachmentUtil.Attachments decisionAttachments = municipalityDecisionService.getDecisionAttachments(initiativeId);

            assertThat(decisionAttachments.getAll().size(), is(0));
        }
    }


    @Test
    @Transactional
    public void decision_absent_if_decision_not_present() {
        Long initiativeId = createSentVerifiedInitiativeWithAuthor();

        Maybe<MunicipalityDecisionInfo> decisionDtoMaybe = municipalityDecisionService.getMunicipalityDecisionInfoMaybe(InitiativeViewInfo.parse(initiativeDao.get(initiativeId)));

        assertThat(decisionDtoMaybe.isNotPresent(), is(true));
    }

    @Test
    @Transactional
    public void get_decision() {
        Long initiativeId = createSentVerifiedInitiativeWithAuthor();

        try {

            MunicipalityDecisionDto decision = createDefaultMunicipalityDecisionWithAttachment(initiativeId);

        } catch (Exception e ) {
            Maybe<MunicipalityDecisionInfo> decisionDtoMaybe = municipalityDecisionService.getMunicipalityDecisionInfoMaybe(InitiativeViewInfo.parse(initiativeDao.get(initiativeId)));

            assertThat(decisionDtoMaybe.isPresent(), is(true));
        }
    }

    @Test
    @Transactional
    public void absent_if_decision_text_and_attachments_are_removed() {
        Long initiativeId = createSentVerifiedInitiativeWithAuthor();

        try {

            MunicipalityDecisionDto decision = createDefaultMunicipalityDecisionWithAttachment(initiativeId);

            MunicipalityDecisionDto emptyDecision = MunicipalityDecisionDto.build(Maybe.of(""));

            municipalityDecisionService.saveAttachments(emptyDecision.getFiles(), initiativeId);

            initiativeDao.updateInitiativeDecision(initiativeId, emptyDecision.getDescription());

        } catch (Exception e ) {
            Maybe<MunicipalityDecisionInfo> decisionDtoMaybe = municipalityDecisionService.getMunicipalityDecisionInfoMaybe(InitiativeViewInfo.parse(initiativeDao.get(initiativeId)));

            assertThat(decisionDtoMaybe.isNotPresent(), is(true));
        }
    }
    @Test
    @Transactional
    public void present_if_decision_contains_only_text() {
        Long initiativeId = createSentVerifiedInitiativeWithAuthor();

        try {

            initiativeDao.createInitiativeDecision(initiativeId, "some text");

        } catch (Exception e ) {
            Maybe<MunicipalityDecisionInfo> decisionDtoMaybe = municipalityDecisionService.getMunicipalityDecisionInfoMaybe(InitiativeViewInfo.parse(initiativeDao.get(initiativeId)));

            assertThat(decisionDtoMaybe.isPresent(), is(true));
        }
    }

    @Test
    @Transactional
    public void present_if_decision_contains_only_attachments() {
        Long initiativeId = createSentVerifiedInitiativeWithAuthor();

        try {
            MunicipalityDecisionDto decision = new MunicipalityDecisionDto();

            addAttachmentToDecision(decision);

            municipalityDecisionService.saveAttachments(decision.getFiles(), initiativeId);

            initiativeDao.updateInitiativeDecision(initiativeId, decision.getDescription());

        } catch (Exception e ) {
            Maybe<MunicipalityDecisionInfo> decisionDtoMaybe = municipalityDecisionService.getMunicipalityDecisionInfoMaybe(InitiativeViewInfo.parse(initiativeDao.get(initiativeId)));

            assertThat(decisionDtoMaybe.isPresent(), is(true));

            assertThat(decisionDtoMaybe.getValue().getDecisionText(), nullValue());
        }
    }

    @Test
    public void get_attachment_file() throws FileUploadException, InvalidAttachmentException, IOException {
        File ATTACHMENT_TEMP_DIR = new File("target/tmp/");
        FileUtils.forceMkdir(ATTACHMENT_TEMP_DIR);

        Long initiativeId = createSentVerifiedInitiativeWithAuthor();

        createDefaultMunicipalityDecisionWithAttachment(initiativeId);
        AttachmentUtil.Attachments attachments = municipalityDecisionService.getDecisionAttachments(initiativeId);
        Assert.assertThat(attachments.getAll().size(), is(1));
        AttachmentFileBase attachment = attachments.getAll().get(0);

        // Write some temporarly attachment file
        FileUtils.write(new File(AttachmentUtil.getFilePathForMunicipalityAttachment(attachment.getAttachmentId(), attachment.getFileType(), ATTACHMENT_TEMP_DIR.getAbsolutePath())), "lol");

        AttachmentFile attachmentFile = AttachmentUtil.getAttachmentFile(attachment.getFileName(), attachment, ATTACHMENT_TEMP_DIR.getAbsolutePath());

        Assert.assertThat(attachmentFile, notNullValue());
        Assert.assertThat(attachmentFile.getFileName(), is(attachment.getFileName()));

    }

    @Test
    @Transactional
    public void creates_a_working_municipality_login_hash_and_sends_it_via_emaiL() {

        Long initiativeId = createSentVerifiedInitiativeWithAuthor();
        String managementHash = randomAlphabetic(40);
        municipalityUserDao.assignMunicipalityUser(initiativeId, managementHash);

        municipalityDecisionService.createAndSendMunicipalityLoginLink(managementHash);
        String newLoginHash = PreviousHashGetter.get();

        assertThat(municipalityUserDao.getInitiativeId(managementHash, newLoginHash), is(initiativeId));

        EmailDto singleQueuedEmail = testHelper.getSingleQueuedEmail();
        assertThat(singleQueuedEmail.getSubject(), is("Kuntalaisaloitteeseen vastaaminen / SV Kuntalaisaloitteeseen vastaaminen"));
        assertThat(singleQueuedEmail.getRecipientsAsString(), is(MUNICIPALITY_EMAIL));

    }


    protected MunicipalityDecisionDto addAttachmentToDecision(MunicipalityDecisionDto decision) throws IOException {
        List<MunicipalityDecisionDto.FileWithName> files = getFileWithNames();

        decision.setFiles(files);

        return decision;
    }

    private List<MunicipalityDecisionDto.FileWithName> getFileWithNames() throws IOException {
        List<MunicipalityDecisionDto.FileWithName> files = new ArrayList<>();

        MunicipalityDecisionDto.FileWithName fileWithName = new MunicipalityDecisionDto.FileWithName();
        fileWithName.setFile(createDefaultFile());
        fileWithName.setName(CUSTOM_ATTACHMENT_NAME_GIVEN_BY_USER);
        files.add(fileWithName);
        return files;
    }

    protected MunicipalityDecisionDto createDefaultMunicipalityDecisionWithAttachment(Long initiativeId) throws IOException, InvalidAttachmentException, FileUploadException {
        MunicipalityDecisionDto decision = new MunicipalityDecisionDto();

        addAttachmentToDecision(decision);

        decision.setDescription(DECISION_DESCRIPTION);

        municipalityDecisionService.setDecision(decision, initiativeId, new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId)),  new Locale("fi"));

        return decision;
    }



    protected  Long createSentVerifiedInitiativeWithAuthor() {
        return testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId)
                .withState(InitiativeState.PUBLISHED)
                .withSent(DateTime.now()).applyAuthor().toInitiativeDraft());
    }

    protected static MultipartFile createDefaultFile() throws IOException {
        return multiPartFileMock(TESTI_PDF, CONTENT_TYPE, TEST_PDF_FILE);
    }

    protected static MultipartFile multiPartFileMock(String fileName, String contentType, final File file) throws IOException {

        return new MockMultipartFile(fileName, fileName, contentType, FileUtil.readAsByteArray(file));

    }

}
