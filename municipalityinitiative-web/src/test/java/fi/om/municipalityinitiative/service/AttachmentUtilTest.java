package fi.om.municipalityinitiative.service;


import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AttachmentUtilTest  {

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

    @Test
    public void is_pdf_type() {
        assertThat(AttachmentUtil.isPdfContentType("application/pdf"), is(true));
    }


}
