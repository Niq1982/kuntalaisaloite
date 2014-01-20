package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dto.ui.InitiativePageInfo;
import fi.om.municipalityinitiative.dto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.dto.ui.ParticipantsPageInfo;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.service.AttachmentService;
import fi.om.municipalityinitiative.service.ParticipantService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional(readOnly = true)
public class PublicInitiativeService {

    @Resource
    private NormalInitiativeService normalInitiativeService;

    @Resource
    private AuthorService authorService;

    @Resource
    private AttachmentService attachmentService;

    @Resource
    private ParticipantService participantService;

    public InitiativePageInfo getInitiativePageInfo(Long initiativeId) {
        return new InitiativePageInfo(
                normalInitiativeService.getPublicInitiative(initiativeId),
                authorService.findPublicAuthors(initiativeId),
                attachmentService.findAcceptedAttachments(initiativeId)
        );
    }

    public InitiativePageInfo getInitiativePageDto(Long initiativeId, LoginUserHolder loginUserHolder) {
        InitiativeViewInfo initiative = normalInitiativeService.getInitiative(initiativeId, loginUserHolder);

        return new InitiativePageInfo(
                initiative,
                authorService.findPublicAuthors(initiativeId),
                attachmentService.findAttachments(initiativeId, loginUserHolder)
        );
    }

    public ParticipantsPageInfo getInitiativePageInfoWithParticipants(
            Long initiativeId,
            LoginUserHolder<User> loginUserHolder,
            int participantListOffset) {

        return new ParticipantsPageInfo(
                normalInitiativeService.getInitiative(initiativeId, loginUserHolder),
                participantService.findPublicParticipants(participantListOffset, initiativeId)
        );
    }
}
