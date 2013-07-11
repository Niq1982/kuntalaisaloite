package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.ParticipantDao;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.dto.ui.*;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.service.email.EmailService;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import fi.om.municipalityinitiative.service.operations.AuthorServiceOperations;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;
import java.util.Locale;

import static fi.om.municipalityinitiative.util.SecurityUtil.assertAllowance;

public class AuthorService {

    @Resource
    private AuthorDao authorDao;

    @Resource
    private InitiativeDao initiativeDao;

    @Resource
    private ParticipantDao participantDao;

    @Resource
    private EmailService emailService;

    @Resource
    private AuthorServiceOperations operations;

    public void createAuthorInvitation(Long initiativeId, LoginUserHolder loginUserHolder, AuthorInvitationUICreateDto uiCreateDto) {

        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        AuthorInvitation authorInvitation = operations.doCreateAuthorInvitation(initiativeId, uiCreateDto);
        emailService.sendAuthorInvitation(initiativeId, authorInvitation);

    }

    public void resendInvitation(Long initiativeId, LoginUserHolder loginUserHolder, String confirmationCode) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        AuthorInvitation authorInvitation = operations.doResendInvitation(initiativeId, confirmationCode);
        emailService.sendAuthorInvitation(initiativeId, authorInvitation);
    }

    @Transactional(readOnly = true)
    public List<AuthorInvitation> findAuthorInvitations(Long initiativeId, LoginUserHolder loginUserHolder) {

        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        return authorDao.findInvitations(initiativeId);
    }

    @Transactional(readOnly = true)
    public List<? extends Author> findAuthors(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        return findAuthors(initiativeId);
    }

    public void deleteAuthor(Long initiativeId, LoginUserHolder loginUserHolder, Long authorId) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        ContactInfo deletedAuthorContactInfo = operations.doDeleteAuthor(initiativeId, authorId, loginUserHolder.getUser());

        emailService.sendAuthorDeletedEmailToOtherAuthors(initiativeId, deletedAuthorContactInfo);
        emailService.sendAuthorDeletedEmailToDeletedAuthor(initiativeId, deletedAuthorContactInfo.getEmail());

    }

    @Transactional(readOnly = false)
    public String confirmAuthorInvitation(Long initiativeId, AuthorInvitationUIConfirmDto confirmDto, Locale locale) {

        Initiative initiative = initiativeDao.get(initiativeId);

        assertAllowance("Accept invitation", ManagementSettings.of(initiative).isAllowInviteAuthors());
        assertAllowance("Accept normal invitation", InitiativeType.isNotVerifiable(initiative.getType()));

        for (AuthorInvitation invitation : authorDao.findInvitations(initiativeId)) {

            if (invitation.getConfirmationCode().equals(confirmDto.getConfirmCode())) {

                invitation.assertNotRejectedOrExpired();

                // TODO: Get emails out of transaction?

                authorDao.deleteAuthorInvitation(initiativeId, confirmDto.getConfirmCode());
                String managementHash = createAuthorAndParticipant(initiativeId, confirmDto);
                emailService.sendAuthorConfirmedInvitation(initiativeId, invitation.getEmail(), managementHash, locale);
                return managementHash;
            }
        }
        throw new NotFoundException("Invitation with ", "initiative: " + initiativeId + ", invitation: " + confirmDto.getConfirmCode());
    }

    private String createAuthorAndParticipant(Long initiativeId, AuthorInvitationUIConfirmDto confirmDto) {
        ParticipantCreateDto participantCreateDto = ParticipantCreateDto.parse(confirmDto, initiativeId);
        String managementHash = RandomHashGenerator.longHash();
        Long participantId = participantDao.prepareParticipant(initiativeId, confirmDto.getHomeMunicipality(), participantCreateDto.getEmail(), participantCreateDto.getMunicipalMembership());
        NormalAuthorId authorId = authorDao.createAuthor(initiativeId, participantId, managementHash);
        authorDao.updateAuthorInformation(authorId, confirmDto.getContactInfo());
        return managementHash;
    }

    @Transactional(readOnly = false)
    public AuthorInvitationConfirmViewData getAuthorInvitationConfirmData(Long initiativeId, String confirmCode, LoginUserHolder unknownLoginUserHolder) {

        boolean isVerifiableInitiative = initiativeDao.isVerifiableInitiative(initiativeId);
        if (isVerifiableInitiative) {
            unknownLoginUserHolder.getVerifiedUser(); // Throws exception if not verified
        }

        AuthorInvitation authorInvitation = authorDao.getAuthorInvitation(initiativeId, confirmCode);
        authorInvitation.assertNotRejectedOrExpired();

        AuthorInvitationUIConfirmDto confirmDto = new AuthorInvitationUIConfirmDto();
        confirmDto.assignInitiativeMunicipality(initiativeDao.get(initiativeId).getMunicipality().getId());
        confirmDto.setConfirmCode(authorInvitation.getConfirmationCode());

        if (!isVerifiableInitiative) {
            confirmDto.setContactInfo(new ContactInfo());
            confirmDto.getContactInfo().setName(authorInvitation.getName());
            confirmDto.getContactInfo().setEmail(authorInvitation.getEmail());
        }
        else {
            confirmDto.setContactInfo(unknownLoginUserHolder.getVerifiedUser().getContactInfo());
        }

        AuthorInvitationConfirmViewData data = new AuthorInvitationConfirmViewData();
        data.authorInvitationUIConfirmDto = confirmDto;
        data.initiativeViewInfo = InitiativeViewInfo.parse(initiativeDao.get(initiativeId));
        return data;
    }

    private List<? extends Author> findAuthors(Long initiativeId) {
        if (initiativeDao.isVerifiableInitiative(initiativeId)) {
            return authorDao.findVerifiedAuthors(initiativeId);
        }
        else {
            return authorDao.findNormalAuthors(initiativeId);
        }
    }

    public static class AuthorInvitationConfirmViewData {
        public AuthorInvitationUIConfirmDto authorInvitationUIConfirmDto;
        public InitiativeViewInfo initiativeViewInfo;
    }

    @Transactional(readOnly = true)
    public PublicAuthors findPublicAuthors(Long initiativeId) {
        return new PublicAuthors(findAuthors(initiativeId));
    }

    @Transactional(readOnly = false)
    public void rejectInvitation(Long initiativeId, String confirmCode) {
        authorDao.rejectAuthorInvitation(initiativeId, confirmCode);
    }

}
