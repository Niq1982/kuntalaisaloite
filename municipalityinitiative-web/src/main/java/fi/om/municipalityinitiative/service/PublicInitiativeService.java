package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.LoginUserHolder;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.ManagementSettings;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.newdto.ui.*;
import fi.om.municipalityinitiative.util.*;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;
import java.util.Locale;

import static fi.om.municipalityinitiative.util.SecurityUtil.assertAllowance;


public class PublicInitiativeService {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    ParticipantDao participantDao;

    @Resource
    EmailService emailService;

    @Resource
    MunicipalityDao municipalityDao;

    public List<InitiativeListInfo> findMunicipalityInitiatives(InitiativeSearch search) {
        return initiativeDao.find(search);
    }

    @Transactional(readOnly = true)
    public ManagementSettings getManagementSettings(Long initiativeId) {
        return ManagementSettings.of(initiativeDao.getByIdWithOriginalAuthor(initiativeId));
    }

    @Transactional(readOnly = false)
    public Long createParticipant(ParticipantUICreateDto participant, Long initiativeId, Locale locale) {

        assertAllowance("Allowed to participate", getManagementSettings(initiativeId).isAllowParticipate());

        ParticipantCreateDto participantCreateDto = ParticipantCreateDto.parse(participant, initiativeId);
        participantCreateDto.setMunicipalityInitiativeId(initiativeId);

        String confirmationCode = RandomHashGenerator.randomString(20);
        Long participantId = participantDao.create(participantCreateDto, confirmationCode);

        emailService.sendParticipationConfirmation(
                initiativeDao.getByIdWithOriginalAuthor(initiativeId),
                participant.getParticipantEmail(),
                participantId,
                confirmationCode,
                locale
        );

        return participantId;
    }

    @Transactional(readOnly = false)
    public Long prepareInitiative(PrepareInitiativeUICreateDto createDto, Locale locale) {

        Long initiativeId = initiativeDao.prepareInitiative(createDto.getMunicipality());
        Long participantId = participantDao.prepareParticipant(initiativeId,
                createDto.getHomeMunicipality(),
                createDto.getParticipantEmail(),
                createDto.hasMunicipalMembership() ? createDto.getMunicipalMembership() : Membership.none,
                false); // XXX: Remove franchise?
                        // XXX: Create dto?
        String managementHash = RandomHashGenerator.randomString(40);
        Long authorId = initiativeDao.createAuthor(initiativeId, participantId, managementHash);
        initiativeDao.assignAuthor(initiativeId, authorId);

        emailService.sendPrepareCreatedEmail(initiativeDao.getByIdWithOriginalAuthor(initiativeId), createDto.getParticipantEmail(), locale);

        return initiativeId;
    }

    @Transactional(readOnly = true)
    public InitiativeViewInfo getMunicipalityInitiative(Long initiativeId) {
        return InitiativeViewInfo.parse(initiativeDao.getByIdWithOriginalAuthor(initiativeId));
    }

    @Transactional(readOnly = true)
    public InitiativeCounts getInitiativeCounts(Maybe<Long> municipality) {
        return initiativeDao.getInitiativeCounts(municipality);
    }

    @Transactional(readOnly = true)
    public InitiativeDraftUIEditDto getInitiativeDraftForEdit(Long initiativeId) {
        assertAllowance("Edit initiative", getManagementSettings(initiativeId).isAllowEdit());
        return InitiativeDraftUIEditDto.parse(initiativeDao.getByIdWithOriginalAuthor(initiativeId));
    }

    @Transactional(readOnly = false)
    public void editInitiativeDraft(Long initiativeId, LoginUserHolder loginUserHolder, InitiativeDraftUIEditDto editDto) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Edit initiative", getManagementSettings(initiativeId).isAllowEdit());
        initiativeDao.editInitiativeDraft(initiativeId, editDto);
    }

    @Transactional(readOnly = true)
    public InitiativeUIUpdateDto getInitiativeForUpdate(Long initiativeId, LoginUserHolder loginUserHolder) {

        assertAllowance("Update initiative", getManagementSettings(initiativeId).isAllowUpdate());
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        // TODO: Remove managementHash in the future
        String managementHash = loginUserHolder.getInitiative().get().getManagementHash().get();

        Initiative initiative = initiativeDao.getById(initiativeId, managementHash);

        InitiativeUIUpdateDto updateDto = new InitiativeUIUpdateDto();
        updateDto.setContactInfo(initiative.getAuthor().getContactInfo());
        updateDto.setShowName(initiative.getShowName());
        updateDto.setExtraInfo(initiative.getExtraInfo());

        return updateDto;
    }

    @Transactional(readOnly = false)
    public void updateInitiative(Long initiativeId, LoginUserHolder loginUserHolder, InitiativeUIUpdateDto updateDto) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Update initiative", getManagementSettings(initiativeId).isAllowUpdate());
        initiativeDao.updateAcceptedInitiative(initiativeId, loginUserHolder.getInitiative().get().getManagementHash().get(), updateDto);
    }

    @Transactional(readOnly = true)
    public Author getAuthorInformation(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        return initiativeDao.getAuthorInformation(initiativeId, loginUserHolder.getInitiative().get().getManagementHash().get());
    }

    @Transactional(readOnly = false)
    public void sendReviewAndStraightToMunicipality(Long initiativeId, LoginUserHolder loginUserHolder, String sentComment, Locale locale) {
        markAsReviewAndSendEmail(initiativeId, loginUserHolder, locale);
        initiativeDao.updateInitiativeType(initiativeId, InitiativeType.SINGLE);
        initiativeDao.updateSentComment(initiativeId, sentComment);
    }

    @Transactional(readOnly = false)
    public void sendReviewOnlyForAcceptance(Long initiativeId, LoginUserHolder loginUserHolder, Locale locale) {
        markAsReviewAndSendEmail(initiativeId, loginUserHolder, locale);
    }

    private void markAsReviewAndSendEmail(Long initiativeId, LoginUserHolder loginUserHolder, Locale locale) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Send review", getManagementSettings(initiativeId).isAllowSendToReview());

        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.REVIEW);
        Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);
        emailService.sendNotificationToModerator(initiative, locale);
    }

    @Transactional(readOnly = false)
    public void publishAndStartCollecting(Long initiativeId, LoginUserHolder loginUserHolder, Locale locale) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Publish initiative", getManagementSettings(initiativeId).isAllowPublish());

        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.PUBLISHED);
        initiativeDao.updateInitiativeType(initiativeId, InitiativeType.COLLABORATIVE);
        Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);
        emailService.sendStatusEmail(initiative,initiative.getAuthor().getContactInfo().getEmail(), municipalityEmail(initiative), EmailMessageType.PUBLISHED_COLLECTING, locale);
    }

    private String municipalityEmail(Initiative initiative) {
        return municipalityDao.getMunicipalityEmail(initiative.getMunicipality().getId());
    }

    @Transactional(readOnly = false)
    void publishAndSendToMunicipality(Long initiativeId, LoginUserHolder loginUserHolder, String sentComment, Locale locale){
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Publish initiative", getManagementSettings(initiativeId).isAllowPublish());

        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.PUBLISHED);
        initiativeDao.updateInitiativeType(initiativeId, InitiativeType.SINGLE);
        initiativeDao.markInitiativeAsSent(initiativeId);
        initiativeDao.updateSentComment(initiativeId, sentComment);
        Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);
        emailService.sendStatusEmail(initiative,initiative.getAuthor().getContactInfo().getEmail(), municipalityEmail(initiative), EmailMessageType.SENT_TO_MUNICIPALITY, locale);
        emailService.sendSingleToMunicipality(initiative, municipalityDao.getMunicipalityEmail(initiative.getMunicipality().getId()), locale);
    }

    @Transactional(readOnly = false)
    public Long confirmParticipation(Long participantId, String confirmationCode) {
        Long initiativeId = participantDao.getInitiativeIdByParticipant(participantId);
        assertAllowance("Confirm participation", getManagementSettings(initiativeId).isAllowParticipate());

        participantDao.confirmParticipation(participantId, confirmationCode);

        return initiativeId;
    }

    @Transactional(readOnly = false)
    void sendCollaborativeToMunicipality(Long initiativeId, LoginUserHolder loginUserHolder, String sentComment, Locale locale) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Send collaborative to municipality", getManagementSettings(initiativeId).isAllowSendToMunicipality());

        initiativeDao.markInitiativeAsSent(initiativeId);
        initiativeDao.updateSentComment(initiativeId, sentComment);
        Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);
        List<Participant> participants = participantDao.findAllParticipants(initiativeId);
        String municipalityEmail = municipalityEmail(initiative);
        emailService.sendCollaborativeToMunicipality(initiative, participants, municipalityEmail, locale);
        emailService.sendStatusEmail(initiative, initiative.getAuthor().getContactInfo().getEmail(), municipalityEmail, EmailMessageType.SENT_TO_MUNICIPALITY, locale);
    }

    @Transactional(readOnly = false)
    public void sendToMunicipality(Long initiativeId, LoginUserHolder requiredLoginUserHolder, String sentComment, Locale locale) {
        Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);

        if (initiative.getType().isCollectable()) {
            sendCollaborativeToMunicipality(initiativeId, requiredLoginUserHolder, sentComment, locale);
        }
        else {
            publishAndSendToMunicipality(initiativeId, requiredLoginUserHolder, sentComment, locale);
        }
    }


}
