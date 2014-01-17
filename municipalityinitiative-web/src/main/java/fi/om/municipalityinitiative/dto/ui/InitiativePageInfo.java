package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.service.AttachmentService;

public class InitiativePageInfo {

        public final InitiativeViewInfo initiative;
        public final PublicAuthors authors;
        public final AttachmentService.Attachments attachments;

        public InitiativePageInfo(InitiativeViewInfo initiative, PublicAuthors authors, AttachmentService.Attachments attachments) {
            this.initiative = initiative;
            this.authors = authors;
            this.attachments = attachments;
        }

        public boolean isCollaborative() {
            return initiative.isCollaborative();
        }

    public boolean isVerifiable() {
        return initiative.isVerifiable();
    }
}

