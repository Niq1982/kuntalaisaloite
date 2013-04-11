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
    public ManagementSettings managementSettings(Long initiativeId) {
        return new ManagementSettings(initiativeDao.getByIdWithOriginalAuthor(initiativeId));
    }

    @Transactional(readOnly = false)
    public Long createParticipant(ParticipantUICreateDto participant, Long initiativeId) {

        checkAllowedToParticipate(initiativeId);

        ParticipantCreateDto participantCreateDto = ParticipantCreateDto.parse(participant, initiativeId);
        participantCreateDto.setMunicipalityInitiativeId(initiativeId);
        return participantDao.create(participantCreateDto);
    }

    private void checkAllowedToParticipate(Long initiativeId) {
        Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);

        if (initiative.getState() != InitiativeState.PUBLISHED) {
            throw new ParticipatingUnallowedException("Initiative not accepted by om: " + initiativeId);
        }
        if (initiative.getSentTime().isPresent()) {
            throw new ParticipatingUnallowedException("Initiative already sent: " + initiativeId);
        }

    }

    @Transactional(readOnly = false)
    public Long prepareInitiative(PrepareInitiativeDto createDto, Locale locale) {

        Long initiativeId = initiativeDao.prepareInitiative(createDto.getMunicipality(), createDto.getAuthorEmail());
        Long participantId = participantDao.prepareParticipant(initiativeId, createDto.getHomeMunicipality(), false); // XXX: Franchise?
        String managementHash = RandomHashGenerator.randomString(40);
        initiativeDao.assignAuthor(initiativeId, participantId, createDto.getAuthorEmail(), managementHash);

        emailService.sendPrepareCreatedEmail(initiativeDao.getByIdWithOriginalAuthor(initiativeId), createDto.getAuthorEmail(), locale);

        return initiativeId;
    }

    public InitiativeViewInfo getMunicipalityInitiative(Long initiativeId) {
        return InitiativeViewInfo.parse(initiativeDao.getByIdWithOriginalAuthor(initiativeId));
    }

    public ContactInfo getContactInfo(Long initiativeId) {
        return initiativeDao.getContactInfo(initiativeId);
    }

    public InitiativeCounts getInitiativeCounts(Maybe<Long> municipality) {
        return initiativeDao.getInitiativeCounts(municipality);
    }

    public InitiativeViewInfo getMunicipalityInitiative(Long initiativeId, String givenManagementHash, Locale locale) {
        return InitiativeViewInfo.parse(initiativeDao.getById(initiativeId, givenManagementHash));
    }

    @Transactional(readOnly = true)
    public InitiativeDraftUIEditDto getInitiativeDraftForEdit(Long initiativeId) {
        assertAllowance("Edit initiative", managementSettings(initiativeId).isAllowEdit());
        InitiativeDraftUIEditDto initiativeForEdit = initiativeDao.getInitiativeForEdit(initiativeId); // TODO: Parse this with InitiativeDraftUiEditDto
        return initiativeForEdit;
    }

    @Transactional(readOnly = false)
    public void editInitiativeDraft(Long initiativeId, InitiativeDraftUIEditDto editDto) {

        assertAllowance("Edit initiative", managementSettings(initiativeId).isAllowEdit());
        if (!initiativeDao.getInitiativeForEdit(initiativeId).getManagementHash().equals(editDto.getManagementHash())) {
            throw new AccessDeniedException("Invalid management hash");
        }

        initiativeDao.updateInitiativeDraft(initiativeId, editDto);
    }

    @Transactional(readOnly = true)
    public InitiativeUIUpdateDto getInitiativeForUpdate(Long initiativeId, String managementHash) {

        assertAllowance("Update initiative", managementSettings(initiativeId).isAllowUpdate());

        Initiative initiative = initiativeDao.getById(initiativeId, managementHash);
        Author authorInformation = getAuthorInformation(initiativeId, managementHash);

        InitiativeUIUpdateDto updateDto = new InitiativeUIUpdateDto();
        updateDto.setContactInfo(authorInformation.getContactInfo());
        updateDto.setShowName(initiative.getShowName());
        updateDto.setManagementHash(managementHash);
        updateDto.setExtraInfo(initiative.getComment());

        return updateDto;
    }

    @Transactional(readOnly = false)
    public void updateInitiative(Long initiativeId, InitiativeUIUpdateDto updateDto) {
        assertAllowance("Update initiative", managementSettings(initiativeId).isAllowUpdate());
        // TODO: Check managementHash from updateDto (is somehow checked when trying to update at dao layer)
        initiativeDao.updateInitiative(initiativeId, updateDto);
    }

    @Transactional(readOnly = true)
    public Author getAuthorInformation(Long initiativeId, String managementHash) {
        return initiativeDao.getAuthorInformation(initiativeId, managementHash);
    }

    @Transactional(readOnly = true)
    public Author getAuthorInformation(Long initiativeId) {
        return initiativeDao.getByIdWithOriginalAuthor(initiativeId).getAuthor();
    }

    @Transactional(readOnly = false)
    public void sendReview(Long initiativeId, LoginUserHolder loginUserHolder, boolean sendToMunicipalityRightAfterAcceptance, Locale locale) {
        loginUserHolder.requireManagementRightsForInitiative(initiativeId);
        assertAllowance("Send review", managementSettings(initiativeId).isAllowSendToReview());

        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.REVIEW);
        
        Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);
        emailService.sendNotificationToModerator(initiative, locale);

        if (sendToMunicipalityRightAfterAcceptance) {
            initiativeDao.updateInitiativeType(initiativeId, InitiativeType.SINGLE);
        }
    }

    @Transactional(readOnly = false)
    public void publishInitiative(Long initiativeId, boolean isCollobrative, Locale locale) {
        assertAllowance("Publish initiative", managementSettings(initiativeId).isAllowPublish());

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
            emailService.sendNotCollectableToMunicipality(initiative, municipalityDao.getMunicipalityEmail(initiative.getMunicipality().getId()), locale);
        }
    }

    private static void assertAllowance(String s, boolean allowed) {
        if (!allowed) {
            throw new OperationNotAllowedException("Operation not allowed: " + s);
        }
    }

}
