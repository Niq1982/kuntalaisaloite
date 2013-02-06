package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dto.SendToMunicipalityDto;
import fi.om.municipalityinitiative.exceptions.NotCollectableException;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.email.InitiativeEmailInfo;
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
    private EmailService emailService;

    @Resource
    private MunicipalityDao municipalityDao;

    public List<InitiativeListInfo> findMunicipalityInitiatives(InitiativeSearch search) {
        return initiativeDao.findNewestFirst(search);
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

        InitiativeViewInfo initiative = initiativeDao.getById(initiativeId);
        ContactInfo contactInfo = initiativeDao.getContactInfo(initiativeId);
        String url = Urls.get(Locales.LOCALE_FI).view(initiativeId);

        InitiativeEmailInfo emailInfo = InitiativeEmailInfo.parse(contactInfo, initiative, url);

        emailService.sendNotCollectableToMunicipality(emailInfo, municipalityDao.getMunicipalityEmail(initiative.getMunicipalityId()), locale);
        emailService.sendNotCollectableToAuthor(emailInfo, locale);
    }

    @Transactional(readOnly = false)
    public void sendToMunicipality(Long initiativeId, String hashCode) {

        InitiativeViewInfo initiativeInfo = initiativeDao.getById(initiativeId);

        checkAllowedToSendToMunicipality(initiativeInfo);
        checkHashCode(hashCode, initiativeInfo);

        // TODO: Send the email.
        initiativeDao.markAsSended(initiativeId);

    }

    private void checkAllowedToSendToMunicipality(InitiativeViewInfo initiativeViewInfo) {
        if (!initiativeViewInfo.isCollectable()) {
            throw new NotCollectableException("Initiative is not collectable");
        }
        if (initiativeViewInfo.getSentTime().isPresent()) {
            throw new NotCollectableException("Initiative already sent");
        }
    }

    private void checkHashCode(String hashCode, InitiativeViewInfo initiativeInfo) {
        if (!hashCode.equals(initiativeInfo.getManagementHash().get())) {
            throw new AccessDeniedException("Invalid initiative verifier");
        }
    }

    @Transactional(readOnly = false)
    public Long createParticipant(ParticipantUICreateDto participant, Long initiativeId) {

        checkAllowedToParticipate(initiativeId);

        ParticipantCreateDto participantCreateDto = ParticipantCreateDto.parse(participant,  initiativeId);
        participantCreateDto.setMunicipalityInitiativeId(initiativeId);
        return participantDao.create(participantCreateDto);
    }

    private void checkAllowedToParticipate(Long initiativeId) {
        InitiativeViewInfo initiative = initiativeDao.getById(initiativeId);

        if (!initiative.isCollectable()) {
            throw new ParticipatingUnallowedException("Initiative not collectable: " + initiativeId);
        }
        else if (initiative.getSentTime().isPresent()) {
            throw new ParticipatingUnallowedException("Initiative already sent: " + initiativeId);
        }

    }

    public InitiativeViewInfo getMunicipalityInitiative(Long initiativeId) {
        return initiativeDao.getById(initiativeId);
    }

    public SendToMunicipalityDto getSendToMunicipalityData(Long initiativeId) {
        SendToMunicipalityDto sendToMunicipalityDto = new SendToMunicipalityDto();
        sendToMunicipalityDto.setContactInfo(initiativeDao.getContactInfo(initiativeId));
        return sendToMunicipalityDto;
    }
}
