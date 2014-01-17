package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dto.ui.InitiativePageInfo;
import fi.om.municipalityinitiative.dto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.service.AttachmentService;
import fi.om.municipalityinitiative.service.MunicipalityService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class PublicInitiativeService {

    @Resource
    private NormalInitiativeService normalInitiativeService;

    @Resource
    private MunicipalityService municipalityService;

    @Resource
    private AuthorService authorService;

    @Resource
    private AttachmentService attachmentService;

    @Transactional(readOnly = true)
    public InitiativePageInfo getInitiativePageInfo(Long initiativeId) {
        return new InitiativePageInfo(
                normalInitiativeService.getPublicInitiative(initiativeId),
                authorService.findPublicAuthors(initiativeId),
                attachmentService.findAcceptedAttachments(initiativeId)
        );
    }

    @Transactional(readOnly = true)
    public InitiativePageInfo getInitiativePageDto(Long initiativeId, LoginUserHolder loginUserHolder) {
        InitiativeViewInfo initiative = normalInitiativeService.getInitiative(initiativeId, loginUserHolder);

        return new InitiativePageInfo(
                initiative,
                authorService.findPublicAuthors(initiativeId),
                attachmentService.findAttachments(initiativeId, loginUserHolder)
        );
    }
}
