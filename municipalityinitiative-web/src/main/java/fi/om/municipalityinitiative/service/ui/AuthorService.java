package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.ParticipantDao;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.NormalAuthor;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.dto.ui.*;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.NormalLoginUser;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.service.email.EmailService;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.hash.RandomHashGenerator;
import org.joda.time.DateTime;
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

    @Transactional(readOnly = false)
    public void createAuthorInvitation(Long initiativeId, LoginUserHolder loginUserHolder, AuthorInvitationUICreateDto uiCreateDto) {

        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        Initiative initiative = initiativeDao.get(initiativeId);
        assertAllowance("Invite authors", ManagementSettings.of(initiative).isAllowInviteAuthors());

        AuthorInvitation authorInvitation = new AuthorInvitation();
        authorInvitation.setInitiativeId(initiativeId);
        authorInvitation.setEmail(uiCreateDto.getAuthorEmail());
        authorInvitation.setName(uiCreateDto.getAuthorName());
        authorInvitation.setConfirmationCode(RandomHashGenerator.shortHash());
        authorInvitation.setInvitationTime(new DateTime());

        authorDao.addAuthorInvitation(authorInvitation);
        emailService.sendAuthorInvitation(initiativeId, authorInvitation);

    }

    @Transactional(readOnly = false)
    public void resendInvitation(Long initiativeId, LoginUserHolder loginUserHolder, String confirmationCode) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        Initiative initiative = initiativeDao.get(initiativeId);
        assertAllowance("Invite authors", ManagementSettings.of(initiative).isAllowInviteAuthors());

        AuthorInvitation authorInvitation = authorDao.getAuthorInvitation(initiativeId, confirmationCode);
        authorDao.deleteAuthorInvitation(initiativeId, confirmationCode);

        authorInvitation.setInvitationTime(DateTime.now());
        authorDao.addAuthorInvitation(authorInvitation);
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

    @Transactional(readOnly = false)
    public void deleteAuthor(Long initiativeId, LoginUserHolder loginUserHolder, Long authorId) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        Initiative initiative = initiativeDao.get(initiativeId);

        assertAllowance("Invite authors", ManagementSettings.of(initiative).isAllowInviteAuthors());

        User user = loginUserHolder.getUser();
        ContactInfo deletedAuthorContactInfo;

        if (initiative.getType().isVerifiable()) {
            VerifiedUser currentUser = (VerifiedUser) user;
            VerifiedUserId authorToDelete = new VerifiedUserId(authorId);

            if (currentUser.getAuthorId().toLong() == authorId) {
                throw new OperationNotAllowedException("Removing yourself from authors is not allowed");
            }
            deletedAuthorContactInfo = deleteVerifiedAuthor(initiativeId, authorToDelete);
        }

        else {
            NormalLoginUser currentUser = (NormalLoginUser) user;
            NormalAuthorId authorToDelete = new NormalAuthorId(authorId);

            if (currentUser.getAuthorId().toLong() == authorId) {
                throw new OperationNotAllowedException("Removing yourself from authors is not allowed");
            }
            deletedAuthorContactInfo = deleteNormalAuthor(initiativeId, authorToDelete);
        }

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
        Long participantId = participantDao.prepareConfirmedParticipant(initiativeId, confirmDto.getHomeMunicipality(), participantCreateDto.getEmail(), participantCreateDto.getMunicipalMembership(), confirmDto.getContactInfo().isShowName());
        NormalAuthorId authorId = authorDao.createAuthor(initiativeId, participantId, managementHash);
        authorDao.updateAuthorInformation(authorId, confirmDto.getContactInfo());
        return managementHash;
    }

    @Transactional(readOnly = true)
    public AuthorInvitationConfirmViewData getAuthorInvitationConfirmData(Long initiativeId, String confirmCode, LoginUserHolder unknownLoginUserHolder) {

        Initiative initiative = initiativeDao.get(initiativeId);

        AuthorInvitation authorInvitation = authorDao.getAuthorInvitation(initiativeId, confirmCode);
        authorInvitation.assertNotRejectedOrExpired();

        AuthorInvitationUIConfirmDto confirmDto = new AuthorInvitationUIConfirmDto();
        confirmDto.assignInitiativeMunicipality(initiative.getMunicipality().getId());
        confirmDto.setConfirmCode(authorInvitation.getConfirmationCode());

        if (!initiative.getType().isVerifiable()) {
            confirmDto.setContactInfo(new ContactInfo());
            confirmDto.getContactInfo().setName(authorInvitation.getName());
            confirmDto.getContactInfo().setEmail(authorInvitation.getEmail());

        }
        else {
            if (unknownLoginUserHolder.isVerifiedUser()) {
                confirmDto.setContactInfo(unknownLoginUserHolder.getVerifiedUser().getContactInfo());
            }
            else { // If user is not verified, acceptance dialog is hidden at UI and we do not need any user info
                confirmDto.setContactInfo(new ContactInfo());
            }
        }

        AuthorInvitationConfirmViewData data = new AuthorInvitationConfirmViewData();
        data.authorInvitationUIConfirmDto = confirmDto;
        data.initiativeViewInfo = InitiativeViewInfo.parse(initiative);
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

    private ContactInfo deleteVerifiedAuthor(Long initiativeId, VerifiedUserId authorToDelete) {

        ContactInfo contactInfo = authorDao.getVerifiedAuthor(initiativeId, authorToDelete).getContactInfo();
        authorDao.deleteAuthorAndParticipant(initiativeId, authorToDelete);
        initiativeDao.denormalizeParticipantCountForVerifiedInitiative(initiativeId);
        return contactInfo;
    }

    private ContactInfo deleteNormalAuthor(Long initiativeId, NormalAuthorId authorToDelete) {

        List<NormalAuthor> authors = authorDao.findNormalAuthors(initiativeId);

        if (!hasAuthor(authorToDelete, authors)) {
            throw new NotFoundException("Author", "initiative: " + initiativeId + ", author: " + authorToDelete);
        }
        else if (authors.size() < 2) {
            throw new OperationNotAllowedException("Unable to delete author. Initiative has only " + authors.size() +" author(s)");
        }
        else {
            ContactInfo deletedAuthorContactInfo = authorDao.getNormalAuthor(authorToDelete).getContactInfo();
            authorDao.deleteAuthorAndParticipant(authorToDelete);
            initiativeDao.denormalizeParticipantCountForNormalInitiative(initiativeId);
            return deletedAuthorContactInfo;
        }
    }

    private static boolean hasAuthor(NormalAuthorId authorId, List<NormalAuthor> authors) {

        // Such lambdas! http://www.bafe.fi/meem/fiii.gif
        return authors.stream().anyMatch(a -> a.getId().equals(authorId));

    }

}
