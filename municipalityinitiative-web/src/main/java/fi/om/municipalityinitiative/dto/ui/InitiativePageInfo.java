package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.service.Location;
import fi.om.municipalityinitiative.service.AttachmentUtil;

import java.util.List;

public class InitiativePageInfo {

        public final InitiativeViewInfo initiative;
        public final PublicAuthors authors;
        public final AttachmentUtil.Attachments attachments;
        public final List<Location> locations;

        public InitiativePageInfo(InitiativeViewInfo initiative, PublicAuthors authors, AttachmentUtil.Attachments attachments, List<Location> locations) {
            this.initiative = initiative;
            this.authors = authors;
            this.attachments = attachments;
            this.locations = locations;
        }

        public boolean isCollaborative() {
            return initiative.isCollaborative();
        }

    public boolean isVerifiable() {
        return initiative.isVerifiable();
    }
}

