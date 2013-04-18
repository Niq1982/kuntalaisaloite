package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.LoginUserHolder;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.ManagementSettings;
import fi.om.municipalityinitiative.newdto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.newdto.ui.*;
import fi.om.municipalityinitiative.util.*;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;
import java.util.Locale;


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
        Long participantId = participantDao.prepareParticipant(initiativeId, createDto.getHomeMunicipality(), createDto.getParticipantEmail(), false); // XXX: Remove franchise?
        String managementHash = RandomHashGenerator.randomString(40);
        initiativeDao.assignAuthor(initiativeId, participantId, managementHash);

        emailService.sendPrepareCreatedEmail(initiativeDao.getByIdWithOriginalAuthor(initiativeId), createDto.getParticipantEmail(), locale);

        return initiativeId;
    }

    public InitiativeViewInfo getMunicipalityInitiative(Long initiativeId) {
        return InitiativeViewInfo.parse(initiativeDao.getByIdWithOriginalAuthor(initiativeId));
    }

    public InitiativeCounts getInitiativeCounts(Maybe<Long> municipality) {
        return initiativeDao.getInitiativeCounts(municipality);
    }

    public InitiativeViewInfo getMunicipalityInitiative(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        return InitiativeViewInfo.parse(initiativeDao.getById(initiativeId, loginUserHolder.getInitiative().get().getManagementHash().get()));
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
        updateDto.setExtraInfo(initiative.getComment());

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
    public void sendReview(Long initiativeId, LoginUserHolder loginUserHolder, boolean sendToMunicipalityRightAfterAcceptance, Locale locale) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Send review", getManagementSettings(initiativeId).isAllowSendToReview());

        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.REVIEW);
        
        Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);
        emailService.sendNotificationToModerator(initiative, locale);

        if (sendToMunicipalityRightAfterAcceptance) {
            initiativeDao.updateInitiativeType(initiativeId, InitiativeType.SINGLE);
        }
    }

    @Transactional(readOnly = false)
    public void publishInitiative(Long initiativeId, boolean isCollobrative, LoginUserHolder loginUserHolder, Locale locale) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Publish initiative", getManagementSettings(initiativeId).isAllowPublish());

        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.PUBLISHED);
        if (isCollobrative) {
            initiativeDao.updateInitiativeType(initiativeId, InitiativeType.COLLABORATIVE);
            Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);
            emailService.sendStatusEmail(initiative,initiative.getAuthor().getContactInfo().getEmail(), EmailMessageType.PUBLISHED_COLLECTING, locale);
        }
        else {
            initiativeDao.updateInitiativeType(initiativeId, InitiativeType.SINGLE);
            initiativeDao.markInitiativeAsSent(initiativeId);
            Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);
            emailService.sendStatusEmail(initiative,initiative.getAuthor().getContactInfo().getEmail(), EmailMessageType.SENT_TO_MUNICIPALITY, locale);
            emailService.sendSingleToMunicipality(initiative, municipalityDao.getMunicipalityEmail(initiative.getMunicipality().getId()), locale);
        }
    }

    @Transactional(readOnly = false)
    public Long confirmParticipation(Long participantId, String confirmationCode) {
        Long initiativeId = participantDao.getInitiativeIdByParticipant(participantId);
        assertAllowance("Confirm participation", getManagementSettings(initiativeId).isAllowParticipate());

        participantDao.confirmParticipation(participantId, confirmationCode);

        return initiativeId;
    }

    private static void assertAllowance(String s, boolean allowed) {
        if (!allowed) {
            throw new OperationNotAllowedException("Operation not allowed: " + s);
        }
    }
}
