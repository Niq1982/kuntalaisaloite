package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.service.AttachmentService;

import java.util.List;

public class InitiativePageSingleDto {
    public final InitiativeViewInfo initiative;
    public final List<Author> authors;
    public final AttachmentService.Attachments attachments;

    public InitiativePageSingleDto(InitiativeViewInfo initiative, List<Author> authors, AttachmentService.Attachments attachments) {
        this.initiative = initiative;
        this.authors = authors;
        this.attachments = attachments;
    }
}

