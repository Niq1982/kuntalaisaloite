package fi.om.municipalityinitiative.service;


import fi.om.municipalityinitiative.dao.DecisionAttachmentDao;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.ui.MunicipalityDecisionDto;
import fi.om.municipalityinitiative.service.ui.MunicipalityDecisionInfo;
import fi.om.municipalityinitiative.util.Maybe;
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

    public static final File TEST_PDF_FILE = new File(System.getProperty("user.dir") + "/src/test/resources/testi.pdf");


    @Resource
    private DecisionService decisionService;

    @Resource
    private DecisionAttachmentDao decisionAttachmentDao;

    private Long testMunicipalityId;

    private String DECISION_DESCRIPTION = "Kunnalla ei ole rahaa.";

    @Override
    protected void childSetup()  {
        testMunicipalityId = testHelper.createTestMunicipality("Some municipality");
    }

    @Test
    public void save_decision() {
        Long initiativeId = createVerifiedInitiativeWithAuthor();
        MunicipalityDecisionDto decision = new MunicipalityDecisionDto();
        try {
            List<MultipartFile> files = new ArrayList<MultipartFile>();
            files.add(multiPartFileMock("testi.pdf", "pdf", TEST_PDF_FILE));
            decision.setFiles(files);
            decision.setDescription(DECISION_DESCRIPTION);
            decisionService.setDecision(decision, initiativeId);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Maybe<MunicipalityDecisionInfo> savedDecision = decisionService.getDecision(initiativeId);

            assertThat(savedDecision.isPresent(), is(true));

            MunicipalityDecisionInfo decisionInfo = savedDecision.getValue();

            assertThat(decisionInfo.getDecisionText(), is(DECISION_DESCRIPTION));

            assertThat(decisionInfo.getAttachments().size(), is(1));
        }

    }

    private Long createVerifiedInitiativeWithAuthor() {
        return testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).applyAuthor().toInitiativeDraft());
    }

    private static MultipartFile multiPartFileMock(String fileName, String contentType, final File file) throws IOException {

        return new MockMultipartFile(fileName, fileName, contentType, FileUtil.readAsByteArray(file));

    }

}
