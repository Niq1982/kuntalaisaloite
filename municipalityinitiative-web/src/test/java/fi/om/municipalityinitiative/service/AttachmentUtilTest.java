package fi.om.municipalityinitiative.service;


import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AttachmentUtilTest extends  DecisionServiceIntegrationTest  {

    public static final long ATTACHMENT_ID = 1L;
    public static final String FILE_TYPE = "pdf";
    public static final String ATTACHMENT_DIR = "C:/dummypath";



    @Test
    public void get_file_path_for_attachment(){
        assertThat(AttachmentUtil.getFilePath(ATTACHMENT_ID, FILE_TYPE, ATTACHMENT_DIR), is("C:/dummypath/1.pdf"));
    }


    @Test
    public void get_file_thumbnail_path_for_attachment(){
        assertThat(AttachmentUtil.getThumbnailPath(ATTACHMENT_ID, FILE_TYPE, ATTACHMENT_DIR), is("C:/dummypath/1_thumbnail.pdf"));
    }

    @Test
    public void get_file_path_for_municipality_decision_attachment(){
        assertThat(AttachmentUtil.getFilePathForMunicipalityAttachment(ATTACHMENT_ID, FILE_TYPE, ATTACHMENT_DIR), is("C:/dummypath/decision_1.pdf"));
    }

    @Test
    public void get_file_thumbnail_path_for_municipality_decision_attachment(){
        assertThat(AttachmentUtil.getThumbnailFilePathForMunicipalityAttachment(ATTACHMENT_ID, FILE_TYPE, ATTACHMENT_DIR), is("C:/dummypath/decision_1_thumbnail.pdf"));
    }

    /*@Test
    public void get_attachment_file(){
        Long initiativeId = createVerifiedInitiativeWithAuthor();

        try {
            createDefaultMunicipalityDecisionWithAttachment(initiativeId);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (InvalidAttachmentException e) {
            e.printStackTrace();
        } finally {

            AttachmentUtil.Attachments attachments = decisionService.getDecisionAttachments(initiativeId);
            assertThat(attachments.getAll().size(), is(1));
            AttachmentFileBase attachment = attachments.getAll().get(0);
            AttachmentFile attachmentFile = null;
            try {
                attachmentFile = AttachmentUtil.getAttachmentFile(attachment.getFileName(), attachment, AttachmentUtil.getFilePathForMunicipalityAttachment(attachment.getAttachmentId(), attachment.getFileType(), decisionAttachmentFileDir));
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                assertThat(attachmentFile, notNullValue());
                assertThat(attachmentFile.getFileName(), is(attachment.getFileName()));
                // TODO assert file contents
            }

        }
    }*/



}
