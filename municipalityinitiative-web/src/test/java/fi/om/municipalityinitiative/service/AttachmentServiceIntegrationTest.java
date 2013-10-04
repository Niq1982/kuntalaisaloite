package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.AttachmentFileInfo;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.exceptions.InvalidAttachmentException;
import org.aspectj.util.FileUtil;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class AttachmentServiceIntegrationTest extends ServiceIntegrationTestBase {

    @Resource
    TestHelper testHelper;

    @Resource
    AttachmentService attachmentService;

    private Long initiativeId;

    @Override
    protected void childSetup() {
        initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testHelper.createTestMunicipality("mun")).applyAuthor().toInitiativeDraft());
    }

    @Test
    public void find_all_attachments_is_ok_with_om_or_management_rights() {
        testHelper.addAttachment(initiativeId, "ok", false);
        assertThat(attachmentService.findAllAttachments(initiativeId, TestHelper.authorLoginUserHolder), hasSize(1));
        assertThat(attachmentService.findAllAttachments(initiativeId, TestHelper.omLoginUser), hasSize(1));
    }

    @Test(expected = AccessDeniedException.class)
    public void find_all_attachments_fails_if_normal_user() {
        testHelper.addAttachment(initiativeId, "ok", false);
        attachmentService.findAllAttachments(initiativeId, TestHelper.unknownLoginUserHolder);
    }

    @Test(expected = AccessDeniedException.class)
    public void saving_file_requires_management_rights() throws IOException {
        attachmentService.addAttachment(initiativeId, TestHelper.unknownLoginUserHolder, multiPartFileMock("", ""), "");
    }

    @Test
    public void saving_file_succeeds() throws IOException {
        precondition(attachmentService.findAllAttachments(initiativeId, TestHelper.authorLoginUserHolder), hasSize(0));
        attachmentService.addAttachment(initiativeId, TestHelper.authorLoginUserHolder, multiPartFileMock("anyfile.jpg", "image/jpeg"), "someDescription");
        assertThat(attachmentService.findAllAttachments(initiativeId, TestHelper.authorLoginUserHolder), hasSize(1));
    }

    @Test(expected = InvalidAttachmentException.class)
    public void saving_file_fails_if_invalid_filename() throws IOException {
        attachmentService.addAttachment(initiativeId, TestHelper.authorLoginUserHolder, multiPartFileMock("anyfile.gif", "image/jpeg"), "someDescription");
    }

    @Test(expected = InvalidAttachmentException.class)
    public void saving_file_fails_if_invalid_content_type() throws IOException {
        attachmentService.addAttachment(initiativeId, TestHelper.authorLoginUserHolder, multiPartFileMock("anyfile.jpg", "application/pdf"), "someDescription");
    }

    @Test(expected = AccessDeniedException.class)
    public void get_unaccepted_attachment_is_forbidden_if_unknown() throws IOException {
        Long attachmentId = testHelper.addAttachment(initiativeId, "asd", false);
        attachmentService.getAttachment(attachmentId, TestHelper.unknownLoginUserHolder);
    }

    @Test
    public void get_unaccepted_attachment_is_allowed_if_author_or_om() throws IOException {
        Long attachmentId = testHelper.addAttachment(initiativeId, "asd", false);

        createDummyTempAttachmentFile(attachmentId);

        attachmentService.getAttachment(attachmentId, TestHelper.omLoginUser);
        attachmentService.getAttachment(attachmentId, TestHelper.authorLoginUserHolder);
    }

    @Test
    public void get_accepted_attachment_is_allowed() throws IOException {
        Long attachmentId = testHelper.addAttachment(initiativeId, "asd", true);

        createDummyTempAttachmentFile(attachmentId);
        attachmentService.getAttachment(attachmentId, TestHelper.omLoginUser);
        attachmentService.getAttachment(attachmentId, TestHelper.authorLoginUserHolder);
        attachmentService.getAttachment(attachmentId, TestHelper.unknownLoginUserHolder);
    }

    @Test(expected = AccessDeniedException.class)
    public void get_unaccepted_thumbnail_is_forbidden_if_unknown() throws IOException {
        Long attachmentId = testHelper.addAttachment(initiativeId, "asd", false);
        attachmentService.getThumbnail(attachmentId, TestHelper.unknownLoginUserHolder);
    }

    @Test
    public void get_unaccepted_thumbnail_is_allowed_if_author_or_om() throws IOException {
        Long attachmentId = testHelper.addAttachment(initiativeId, "asd", false);

        createDummyTempAttachmentFile(attachmentId);

        attachmentService.getThumbnail(attachmentId, TestHelper.omLoginUser);
        attachmentService.getThumbnail(attachmentId, TestHelper.authorLoginUserHolder);
    }

    @Test
    public void get_accepted_thumbnail_is_allowed() throws IOException {
        Long attachmentId = testHelper.addAttachment(initiativeId, "asd", true);

        createDummyTempAttachmentFile(attachmentId);
        attachmentService.getThumbnail(attachmentId, TestHelper.omLoginUser);
        attachmentService.getThumbnail(attachmentId, TestHelper.authorLoginUserHolder);
        attachmentService.getThumbnail(attachmentId, TestHelper.unknownLoginUserHolder);
    }

    private void createDummyTempAttachmentFile(Long attachmentId) {
        FileUtil.writeAsString(new File(attachmentService.getAttachmentDir() + attachmentId), ""); // Just some dummy file which this test will find
        FileUtil.writeAsString(new File(attachmentService.getAttachmentDir() + attachmentId+"_thumbnail"), ""); // Just some dummy file which this test will find
    }

    private static MultipartFile multiPartFileMock(String fileName, String contentType) throws IOException {
        MultipartFile multiPartFileMock = mock(MultipartFile.class);
        stub(multiPartFileMock.getOriginalFilename()).toReturn(fileName);
        stub(multiPartFileMock.getContentType()).toReturn(contentType);
        stub(multiPartFileMock.getBytes()).toReturn(new byte[0]);
        return multiPartFileMock;
    }


}
