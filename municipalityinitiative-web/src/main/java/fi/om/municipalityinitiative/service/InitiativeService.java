package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.exceptions.NotCollectableException;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.email.CollectableInitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.email.InitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.InitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.newdto.ui.*;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.ParticipatingUnallowedException;
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

    @Transactional(readOnly = false)
    public Long createMunicipalityInitiative(InitiativeUICreateDto createDto, Locale locale) {

        Maybe<String> managementHash;
        if (createDto.isCollectable()) {
            managementHash = Maybe.of("0000000000111111111122222222223333333333");
        }
        else {
            managementHash = Maybe.absent();
        }

        InitiativeCreateDto initiativeCreateDto = InitiativeCreateDto.parse(createDto, managementHash);

        Long initiativeId = initiativeDao.create(initiativeCreateDto);
        Long participantId = participantDao.create(ParticipantCreateDto.parse(createDto, initiativeId));
        initiativeDao.assignAuthor(initiativeId, participantId);

        if (!createDto.isCollectable()) {
            sendNotCollectableEmails(initiativeId, locale);
        }

        return initiativeId;
    }

    private void sendNotCollectableEmails(Long initiativeId, Locale locale) {

        Initiative initiative = initiativeDao.getById(initiativeId);
        ContactInfo contactInfo = initiativeDao.getContactInfo(initiativeId);
        String url = Urls.get(Locales.LOCALE_FI).view(initiativeId);

        InitiativeEmailInfo emailInfo = InitiativeEmailInfo.parse(contactInfo, initiative, url);

        emailService.sendNotCollectableToMunicipality(emailInfo, municipalityDao.getMunicipalityEmail(initiative.getMunicipality().getId()), locale);
        emailService.sendNotCollectableToAuthor(emailInfo, locale);
    }

    @Transactional(readOnly = false)
    public void sendToMunicipality(Long initiativeId, SendToMunicipalityDto sendToMunicipalityDto, String hashCode, Locale locale) {

        Initiative initiativeInfo = initiativeDao.getById(initiativeId);

        checkAllowedToSendToMunicipality(initiativeInfo);
        checkHashCode(hashCode, initiativeInfo);

        initiativeDao.markAsSendedAndUpdateContactInfo(initiativeId, sendToMunicipalityDto.getContactInfo());
        sendCollectedInitiativeEmails(initiativeId, locale, sendToMunicipalityDto.getComment());
    }

    private void checkAllowedToSendToMunicipality(Initiative initiative) {
        if (!initiative.isCollectable()) {
            throw new NotCollectableException("Initiative is not collectable");
        }
        if (initiative.getSentTime().isPresent()) {
            throw new NotCollectableException("Initiative already sent");
        }
    }

    private void checkHashCode(String hashCode, Initiative initiativeInfo) {
        if (!hashCode.equals(initiativeInfo.getManagementHash().get())) {
            throw new AccessDeniedException("Invalid initiative verifier");
        }
    }

    private void sendCollectedInitiativeEmails(Long initiativeId, Locale locale, String comment) {
        Initiative initiative = initiativeDao.getById(initiativeId);
        ContactInfo contactInfo = initiativeDao.getContactInfo(initiativeId);
        String url = Urls.get(Locales.LOCALE_FI).view(initiativeId);

        InitiativeEmailInfo emailInfo = InitiativeEmailInfo.parse(contactInfo, initiative, url);
        CollectableInitiativeEmailInfo collectableEmailInfo
                = CollectableInitiativeEmailInfo.parse(emailInfo, comment, participantDao.findAllParticipants(initiativeId));

        emailService.sendCollectableToMunicipality(collectableEmailInfo, municipalityDao.getMunicipalityEmail(initiative.getMunicipality().getId()), locale);
        emailService.sendCollectableToAuthor(collectableEmailInfo, locale);
    }

    @Transactional(readOnly = false)
    public Long createParticipant(ParticipantUICreateDto participant, Long initiativeId) {

        checkAllowedToParticipate(initiativeId);

        ParticipantCreateDto participantCreateDto = ParticipantCreateDto.parse(participant,  initiativeId);
        participantCreateDto.setMunicipalityInitiativeId(initiativeId);
        return participantDao.create(participantCreateDto);
    }

    private void checkAllowedToParticipate(Long initiativeId) {
        Initiative initiative = initiativeDao.getById(initiativeId);

        if (!initiative.isCollectable()) {
            throw new ParticipatingUnallowedException("Initiative not collectable: " + initiativeId);
        }
        else if (initiative.getSentTime().isPresent()) {
            throw new ParticipatingUnallowedException("Initiative already sent: " + initiativeId);
        }

    }

    public InitiativeViewInfo getMunicipalityInitiative(Long initiativeId, Locale locale) {
        return InitiativeViewInfo.parse(initiativeDao.getById(initiativeId), locale);
//        return initiativeDao.getById(initiativeId);
    }

    public SendToMunicipalityDto getSendToMunicipalityData(Long initiativeId) {
        SendToMunicipalityDto sendToMunicipalityDto = new SendToMunicipalityDto();
        sendToMunicipalityDto.setContactInfo(initiativeDao.getContactInfo(initiativeId));
        return sendToMunicipalityDto;
    }

    public InitiativeCounts getInitiativeCounts(Maybe<Long> municipality) {
        return initiativeDao.getInitiativeCounts(municipality);
    }

}
