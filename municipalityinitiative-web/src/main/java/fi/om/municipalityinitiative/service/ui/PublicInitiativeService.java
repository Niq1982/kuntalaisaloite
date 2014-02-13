package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.ui.InitiativeListPageInfo;
import fi.om.municipalityinitiative.dto.ui.InitiativePageInfo;
import fi.om.municipalityinitiative.dto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.dto.ui.ParticipantsPageInfo;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.service.AttachmentService;
import fi.om.municipalityinitiative.service.MunicipalityService;
import fi.om.municipalityinitiative.service.ParticipantService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * Separated some methods for public pages showing data.
 * Helps mapping separate db-readonly calls to single transaction to improve
 * efficiency and decrease amount of transaction pools.
 */
public class PublicInitiativeService {

    @Resource
    private NormalInitiativeService normalInitiativeService;

    @Resource
    private AuthorService authorService;

    @Resource
    private AttachmentService attachmentService;

    @Resource
    private ParticipantService participantService;

    @Resource
    private MunicipalityService municipalityService;

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

    @Transactional(readOnly = true)
    public ParticipantsPageInfo getInitiativePageInfoWithParticipants(
            Long initiativeId,
            LoginUserHolder<User> loginUserHolder,
            int participantListOffset) {

        return new ParticipantsPageInfo(
                normalInitiativeService.getInitiative(initiativeId, loginUserHolder),
                participantService.findPublicParticipants(participantListOffset, initiativeId)
        );
    }

    @Transactional(readOnly = true)
    public InitiativeListPageInfo getInitiativeListPageInfo(InitiativeSearch search, LoginUserHolder loginUserHolder, Locale locale) {

        return new InitiativeListPageInfo(
                normalInitiativeService.findMunicipalityInitiatives(search, loginUserHolder),
                normalInitiativeService.getInitiativeCounts(search, loginUserHolder),
                municipalityService.findAllMunicipalities(locale)
                );
    }


}
