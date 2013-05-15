package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.NotFoundException;
import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.newdao.AuthorDao;
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
    AuthorDao authorDao;

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

        assertMunicipalityIsActive(createDto.getMunicipality());

        Long initiativeId = initiativeDao.prepareInitiative(createDto.getMunicipality());
        Long participantId = participantDao.prepareParticipant(initiativeId,
                createDto.getHomeMunicipality(),
                createDto.getParticipantEmail(),
                createDto.hasMunicipalMembership() ? createDto.getMunicipalMembership() : Membership.none
        ); // XXX: Remove franchise?
        // XXX: Create dto?
        String managementHash = RandomHashGenerator.randomString(40);
        Long authorId = authorDao.createAuthor(initiativeId, participantId, managementHash);

        emailService.sendPrepareCreatedEmail(initiativeDao.getByIdWithOriginalAuthor(initiativeId), authorId, managementHash, createDto.getParticipantEmail(), locale);

        return initiativeId;
    }

    private void assertMunicipalityIsActive(Long municipality) {
        if (!municipalityDao.getMunicipality(municipality).isActive()) {
            throw new AccessDeniedException("Municipality is not active for initiatives: " + municipality);
        }
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
    public InitiativeDraftUIEditDto getInitiativeDraftForEdit(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Edit initiative", getManagementSettings(initiativeId).isAllowEdit());
        return InitiativeDraftUIEditDto.parse(
                initiativeDao.getByIdWithOriginalAuthor(initiativeId),
                authorDao.getAuthor(loginUserHolder.getAuthorId()).getContactInfo()
        );
    }

    @Transactional(readOnly = false)
    public void editInitiativeDraft(Long initiativeId, LoginUserHolder loginUserHolder, InitiativeDraftUIEditDto editDto) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        assertAllowance("Edit initiative", getManagementSettings(initiativeId).isAllowEdit());
        initiativeDao.editInitiativeDraft(initiativeId, editDto);
        authorDao.updateAuthorInformation(loginUserHolder.getAuthorId(), editDto.getContactInfo());
    }

    @Transactional(readOnly = true)
    public InitiativeUIUpdateDto getInitiativeForUpdate(Long initiativeId, LoginUserHolder loginUserHolder) {

        assertAllowance("Update initiative", getManagementSettings(initiativeId).isAllowUpdate());
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);
        List<Author> authors = authorDao.findAuthors(initiativeId);
        ContactInfo contactInfo = null;
        for (Author author : authors) {
            if (author.getId().equals(loginUserHolder.getAuthorId())) {
                contactInfo = author.getContactInfo();
                break;
            }
        }

        if (contactInfo == null) {
            throw new RuntimeException("FIX THIS to something nicer");
        }

        InitiativeUIUpdateDto updateDto = new InitiativeUIUpdateDto();
        updateDto.setContactInfo(contactInfo);
        updateDto.setExtraInfo(initiative.getExtraInfo());

        return updateDto;
    }

    @Transactional(readOnly = false)
    public void updateInitiative(Long initiativeId, LoginUserHolder loginUserHolder, InitiativeUIUpdateDto updateDto) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Update initiative", getManagementSettings(initiativeId).isAllowUpdate());

        initiativeDao.updateExtraInfo(initiativeId, updateDto.getExtraInfo());
        authorDao.updateAuthorInformation(loginUserHolder.getAuthorId(), updateDto.getContactInfo());
    }

    @Transactional(readOnly = true)
    public Author getAuthorInformation(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        for (Author author : authorDao.findAuthors(initiativeId)) {
            if (author.getId().equals(loginUserHolder.getAuthorId())) {
                return author;
            }
        }
        // TODO: Hmm.. We still get the municipality information and stuff from participant table...
        throw new NotFoundException("Author", initiativeId);
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

        String TEMPORARILY_REPLACING_OM_EMAIL = authorDao.getAuthorEmails(initiativeId).get(0);

        emailService.sendNotificationToModerator(initiative, authorDao.findAuthors(initiativeId), locale, TEMPORARILY_REPLACING_OM_EMAIL);
    }

    @Transactional(readOnly = false)
    public void publishAndStartCollecting(Long initiativeId, LoginUserHolder loginUserHolder, Locale locale) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Publish initiative", getManagementSettings(initiativeId).isAllowPublish());

        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.PUBLISHED);
        initiativeDao.updateInitiativeType(initiativeId, InitiativeType.COLLABORATIVE);
        Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);
        emailService.sendStatusEmail(initiative, authorDao.getAuthorEmails(initiativeId), municipalityEmail(initiative), EmailMessageType.PUBLISHED_COLLECTING);
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
        emailService.sendStatusEmail(initiative,authorDao.getAuthorEmails(initiativeId), municipalityEmail(initiative), EmailMessageType.SENT_TO_MUNICIPALITY);
        // TODO: String municipalityEmail = municipalityDao.getMunicipalityEmail(initiative.getMunicipality().getId());
        String municipalityEmail = authorDao.getAuthorEmails(initiativeId).get(0);
        emailService.sendSingleToMunicipality(initiative, authorDao.findAuthors(initiativeId), municipalityEmail, locale);
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
        // TODO: String municipalityEmail = municipalityEmail(initiative);
        String municipalityEmail = authorDao.getAuthorEmails(initiativeId).get(0);
        emailService.sendCollaborativeToMunicipality(initiative, authorDao.findAuthors(initiativeId), participants, municipalityEmail, locale);
        emailService.sendStatusEmail(initiative, authorDao.getAuthorEmails(initiativeId), municipalityEmail, EmailMessageType.SENT_TO_MUNICIPALITY);
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
