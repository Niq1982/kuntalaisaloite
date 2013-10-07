package fi.om.municipalityinitiative.dto.service;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AttachmentFileInfoTest {

    @Test
    public void replaces_not_alphapetic_characters() {
        AttachmentFileInfo attachmentFileInfo = new AttachmentFileInfo();

        attachmentFileInfo.setFileType("jpg");

        attachmentFileInfo.setDescription("tässä joku 787% hassu");

        assertThat(attachmentFileInfo.getFileName(), is("t_ss__joku_787__hassu.jpg"));
    }

}
