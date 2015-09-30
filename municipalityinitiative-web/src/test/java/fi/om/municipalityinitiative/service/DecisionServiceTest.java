package fi.om.municipalityinitiative.service;


import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.DecisionAttachmentFile;
import fi.om.municipalityinitiative.dto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.dto.ui.MunicipalityDecisionDto;
import fi.om.municipalityinitiative.dto.user.MunicipalityUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.exceptions.FileUploadException;
import fi.om.municipalityinitiative.exceptions.InvalidAttachmentException;
import fi.om.municipalityinitiative.service.ui.NormalInitiativeService;
import org.aspectj.util.FileUtil;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DecisionServiceTest extends ServiceIntegrationTestBase  {



    @Resource
    private DecisionService decisionService;

    @Resource
    private NormalInitiativeService normalInitiativeService;

    private Long testMunicipalityId;

    private String DECISION_DESCRIPTION = "Kunnalla ei ole rahaa.";

    public static final File TEST_PDF_FILE = new File(System.getProperty("user.dir") + "/src/test/resources/testi.pdf");

    private String TESTI_PDF = "test.pdf";

    private String CONTENT_TYPE = "application/pdf";

    private String FILE_TYPE = "pdf";

    @Override
    protected void childSetup()  {
        testMunicipalityId = testHelper.createTestMunicipality("Some municipality");
    }

    @Test
    public void save_decision_and_get_decision() {
        Long initiativeId = createVerifiedInitiativeWithAuthor();

        MunicipalityDecisionDto decision;

        try {

            decision = createDefaultMunicipalityDecisionWithAttachment(initiativeId);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            AttachmentUtil.Attachments decisionAttachments = decisionService.getDecisionAttachments(initiativeId);

            assertThat(decisionAttachments.getAll().size(), is(1));

            DecisionAttachmentFile fileInfo = (DecisionAttachmentFile)decisionAttachments.getAll().get(0);

            assertThat(fileInfo.getFileType(), is(FILE_TYPE));

            assertThat(fileInfo.getContentType(), is(CONTENT_TYPE));

            assertThat(fileInfo.getFileName(), is(TESTI_PDF));

            assertThat(fileInfo.getInitiativeId(), is(initiativeId));

            assertThat(fileInfo.getFileName(), is(TESTI_PDF));

            InitiativeViewInfo initiative = normalInitiativeService.getInitiative(initiativeId, new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId)));

            assertThat(initiative.getDecisionText().isPresent(), is(true));

            assertThat(initiative.getDecisionText().getValue(), is(DECISION_DESCRIPTION));
        }

    }
    @Test
    public void remove_attachment_from_decision() {

        Long initiativeId = createVerifiedInitiativeWithAuthor();

        MunicipalityDecisionDto decision;

        try {

            decision = createDefaultMunicipalityDecisionWithAttachment(initiativeId);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            AttachmentUtil.Attachments decisionAttachments = decisionService.getDecisionAttachments(initiativeId);

            assertThat(decisionAttachments.getAll().size(), is(1));

            DecisionAttachmentFile fileInfo = (DecisionAttachmentFile) decisionAttachments.getAll().get(0);

            decisionService.removeAttachmentFromDecision(fileInfo.getAttachmentId(), initiativeId);

            decisionAttachments = decisionService.getDecisionAttachments(initiativeId);

            assertThat(decisionAttachments.getAll().size(), is(0));


        }
    }

    private MunicipalityDecisionDto createDefaultMunicipalityDecisionWithAttachment(Long initiativeId) throws IOException, InvalidAttachmentException, FileUploadException {
        MunicipalityDecisionDto decision = new MunicipalityDecisionDto();

        List<MultipartFile> files = new ArrayList<MultipartFile>();

        files.add(multiPartFileMock(
                TESTI_PDF, CONTENT_TYPE, TEST_PDF_FILE));

        decision.setFiles(files);

        decision.setDescription(DECISION_DESCRIPTION);

        decisionService.setDecision(decision, initiativeId);

        return decision;
    }



    private Long createVerifiedInitiativeWithAuthor() {
        return testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).applyAuthor().toInitiativeDraft());
    }

    private static MultipartFile multiPartFileMock(String fileName, String contentType, final File file) throws IOException {

        return new MockMultipartFile(fileName, fileName, contentType, FileUtil.readAsByteArray(file));

    }

}
