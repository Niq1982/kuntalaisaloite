package fi.om.municipalityinitiative.service;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.AttachmentFileInfo;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.exceptions.InvalidAttachmentException;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

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
    public void find_attachments_finds_only_accepted_attachments() {
        testHelper.addAttachment(initiativeId, "kakka kuva", false);
        testHelper.addAttachment(initiativeId, "hyvä kuva", true);

        List<AttachmentFileInfo> attachments = attachmentService.findAttachments(initiativeId);
        assertThat(attachments, hasSize(1));
        assertThat(attachments.get(0).getDescription(), is("hyvä kuva"));
    }

    @Test
    public void find_all_attachments() {
        testHelper.addAttachment(initiativeId, "kakka kuva", false);
        testHelper.addAttachment(initiativeId, "hyvä kuva", true);

        List<AttachmentFileInfo> attachments = attachmentService.findAllAttachments(initiativeId, TestHelper.authorLoginUserHolder);
        assertThat(attachments, hasSize(2));
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

    private static MultipartFile multiPartFileMock(String fileName, String contentType) throws IOException {
        MultipartFile multiPartFileMock = mock(MultipartFile.class);
        stub(multiPartFileMock.getOriginalFilename()).toReturn(fileName);
        stub(multiPartFileMock.getContentType()).toReturn(contentType);
        stub(multiPartFileMock.getBytes()).toReturn(new byte[0]);
        return multiPartFileMock;
    }


}
