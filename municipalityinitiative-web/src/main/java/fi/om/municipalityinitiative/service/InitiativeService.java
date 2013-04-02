package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.exceptions.NotCollectableException;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.email.CollectableInitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.email.InitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.ManagementSettings;
import fi.om.municipalityinitiative.newdto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.newdto.ui.*;
import fi.om.municipalityinitiative.util.*;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;
import java.util.Locale;


public class InitiativeService {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    private ParticipantDao participantDao;

    @Resource
    EmailService emailService;

    @Resource
    MunicipalityDao municipalityDao;

    public List<InitiativeListInfo> findMunicipalityInitiatives(InitiativeSearch search) {
        return initiativeDao.find(search);
    }

    @Transactional(readOnly = true)
    public ManagementSettings managementSettings(Long initiativeId) {
        return new ManagementSettings(initiativeDao.getById(initiativeId));
    }

    @Transactional(readOnly = false)
    public Long createParticipant(ParticipantUICreateDto participant, Long initiativeId) {

        checkAllowedToParticipate(initiativeId);

        ParticipantCreateDto participantCreateDto = ParticipantCreateDto.parse(participant, initiativeId);
        participantCreateDto.setMunicipalityInitiativeId(initiativeId);
        return participantDao.create(participantCreateDto);
    }

    private void checkAllowedToParticipate(Long initiativeId) {
        Initiative initiative = initiativeDao.getById(initiativeId);

        if (initiative.getState() != InitiativeState.ACCEPTED) {
            throw new ParticipatingUnallowedException("Initiative not accepted by om: " + initiativeId);
        }
        if (initiative.getSentTime().isPresent()) {
            throw new ParticipatingUnallowedException("Initiative already sent: " + initiativeId);
        }

    }

    @Transactional(readOnly = false)
    public Long prepareInitiative(PrepareInitiativeDto createDto, Locale locale) {

        String managementHash = RandomHashGenerator.randomString(40);
        Long initiativeId = initiativeDao.prepareInitiative(createDto.getMunicipality(), createDto.getAuthorEmail(), managementHash);
        Long participantId = participantDao.prepareParticipant(initiativeId, createDto.getHomeMunicipality(), false); // XXX: Franchise?
        initiativeDao.assignAuthor(initiativeId, participantId, createDto.getAuthorEmail(), managementHash);

        return initiativeId;
    }

    public InitiativeViewInfo getMunicipalityInitiative(Long initiativeId, Locale locale) {
        return InitiativeViewInfo.parse(initiativeDao.getById(initiativeId), locale);
    }

    public ContactInfo getContactInfo(Long initiativeId) {
        return initiativeDao.getContactInfo(initiativeId);
    }

    public InitiativeCounts getInitiativeCounts(Maybe<Long> municipality) {
        return initiativeDao.getInitiativeCounts(municipality);
    }

    public InitiativeViewInfo getMunicipalityInitiative(Long initiativeId, String givenManagementHash, Locale locale) {
        return InitiativeViewInfo.parse(initiativeDao.getById(initiativeId, givenManagementHash), locale);
    }

    public InitiativeUIEditDto getInitiativeForEdit(Long initiativeId, String managementHash) {
        // TODO: IsAllowed
        InitiativeUIEditDto initiativeForEdit = initiativeDao.getInitiativeForEdit(initiativeId);
        if (!initiativeForEdit.getManagementHash().equals(managementHash)) {
            throw new AccessDeniedException("Invalid management hash");
        }
        return initiativeForEdit;
    }

    public void updateInitiativeDraft(Long initiativeId, InitiativeUIEditDto editDto) {
        // TODO: IsAllowed
        if (!initiativeDao.getInitiativeForEdit(initiativeId).getManagementHash().equals(editDto.getManagementHash())) {
            throw new AccessDeniedException("Invalid management hash");
        }

        initiativeDao.updateInitiativeDraft(initiativeId, editDto);
    }

    public Author getAuthorInformation(Long initiativeId, String managementHash) {
        return initiativeDao.getAuthorInformation(initiativeId, managementHash);
    }

    public void sendReview(Long initiativeId, String managementHash, InitiativeType type) {
        // TODO: IsAllowed
        if (initiativeDao.getById(initiativeId).getManagementHash().get().equals(managementHash)) {
            initiativeDao.setInitiativeAsReview(initiativeId, type);
        }
        else {
            throw new AccessDeniedException("Invalid management hash");
        }
    }
    
    // TODO: User is logged in with moderation rights
    // TODO: IsAllowed
    public void accept(Long initiativeId, String managementHash) {
        if (initiativeDao.getById(initiativeId).getManagementHash().get().equals(managementHash)) {
            initiativeDao.acceptInitiativeByOm(initiativeId);
        }
        else {
            throw new AccessDeniedException("Invalid management hash");
        }
    }
    
 // TODO: User is logged in with moderation rights
 // TODO: IsAllowed
    public void reject(Long initiativeId, String managementHash) {
        if (initiativeDao.getById(initiativeId).getManagementHash().get().equals(managementHash)) {
            initiativeDao.rejectInitiativeByOm(initiativeId);
        }
        else {
            throw new AccessDeniedException("Invalid management hash");
        }
    }
}
