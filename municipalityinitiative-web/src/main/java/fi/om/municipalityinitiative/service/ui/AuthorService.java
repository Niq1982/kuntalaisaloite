package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.ParticipantDao;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.NormalAuthor;
import fi.om.municipalityinitiative.dto.VerifiedAuthor;
import fi.om.municipalityinitiative.dto.json.PublicApiAuthors;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
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
    public void deleteAuthor(Long initiativeId, LoginUserHolder loginUserHolder, Long authorId, boolean targetAuthorIsVerified) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        Initiative initiative = initiativeDao.get(initiativeId);

        assertAllowance("Invite authors", ManagementSettings.of(initiative).isAllowInviteAuthors());

        User user = loginUserHolder.getUser();
        if ((user.isVerifiedUser() && targetAuthorIsVerified && ((VerifiedUser) user).getAuthorId().toLong() == authorId)
                || (!user.isVerifiedUser() && !targetAuthorIsVerified && ((NormalLoginUser) user).getAuthorId().toLong() == authorId)) {
            throw new OperationNotAllowedException("Removing yourself from authors is not allowed");
        }

        if (authorDao.findAllAuthors(initiativeId).size() == 1) {
            throw new OperationNotAllowedException("Unable to delete the final author.");
        }

        ContactInfo deletedAuthorContactInfo;

        if (targetAuthorIsVerified) {
            deletedAuthorContactInfo = deleteVerifiedAuthor(initiativeId, new VerifiedUserId(authorId));
        }
        else {
            deletedAuthorContactInfo = deleteNormalAuthor(initiativeId, new NormalAuthorId(authorId));
        }

        initiativeDao.denormalizeParticipantCounts(initiativeId);

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
                String managementHash = createAuthorAndParticipant(initiative, confirmDto);
                emailService.sendAuthorConfirmedInvitation(initiativeId, invitation.getEmail(), managementHash, locale);
                participantDao.increaseParticipantCountFor(
                        initiativeId,
                        confirmDto.getContactInfo().isShowName(),
                        initiative.getMunicipality().getId().equals(confirmDto.getHomeMunicipality())
                );
                return managementHash;
            }
        }
        throw new NotFoundException("Invitation with ", "initiative: " + initiativeId + ", invitation: " + confirmDto.getConfirmCode());
    }

    private String createAuthorAndParticipant(Initiative initiative, AuthorInvitationUIConfirmDto confirmDto) {

        MunicipalMembershipSolver municipalMembershipSolver = new MunicipalMembershipSolver(User.anonym(), initiative.getMunicipality().getId(), confirmDto);

        municipalMembershipSolver.assertMunicipalityOrMembershipForNormalInitiative();

        String managementHash = RandomHashGenerator.longHash();
        Long participantId = participantDao.prepareConfirmedParticipant(
                initiative.getId(),
                municipalMembershipSolver.getHomeMunicipality(),
                confirmDto.getContactInfo().getEmail(),
                municipalMembershipSolver.getMunicipalMembership(),
                confirmDto.getContactInfo().isShowName());
        NormalAuthorId authorId = authorDao.createAuthor(initiative.getId(), participantId, managementHash);
        authorDao.updateAuthorInformation(authorId, confirmDto.getContactInfo());
        return managementHash;
    }

    @Transactional(readOnly = true)
    public AuthorInvitationConfirmViewData getAuthorInvitationConfirmData(Long initiativeId, String confirmCode, LoginUserHolder loginUserHolder) {

        Initiative initiative = initiativeDao.get(initiativeId);

        AuthorInvitation authorInvitation = authorDao.getAuthorInvitation(initiativeId, confirmCode);
        authorInvitation.assertNotRejectedOrExpired();

        AuthorInvitationUIConfirmDto confirmDto = new AuthorInvitationUIConfirmDto();
        confirmDto.assignInitiativeMunicipality(initiative.getMunicipality().getId());
        confirmDto.setConfirmCode(authorInvitation.getConfirmationCode());

        if (loginUserHolder.isVerifiedUser()) {
            confirmDto.setContactInfo(loginUserHolder.getVerifiedUser().getContactInfo());
        }
        else {
            confirmDto.setContactInfo(new ContactInfo());
            confirmDto.getContactInfo().setName(authorInvitation.getName());
        }

        // No matter if we had the users email (in case verified user), prefill the email from the invitation
        confirmDto.getContactInfo().setEmail(authorInvitation.getEmail());

        AuthorInvitationConfirmViewData data = new AuthorInvitationConfirmViewData();
        data.authorInvitationUIConfirmDto = confirmDto;
        data.initiativeViewInfo = InitiativeViewInfo.parse(initiative);
        return data;
    }

    private List<? extends Author> findAuthors(Long initiativeId) {
        return authorDao.findAllAuthors(initiativeId);
    }

    public static class AuthorInvitationConfirmViewData {
        public AuthorInvitationUIConfirmDto authorInvitationUIConfirmDto;
        public InitiativeViewInfo initiativeViewInfo;
    }

    @Transactional(readOnly = true)
    public PublicAuthors findPublicAuthors(Long initiativeId) {
        return new PublicAuthors(findAuthors(initiativeId));
    }

    @Transactional(readOnly = true)
    public PublicApiAuthors findPublicApiAuthors(Long initiativeId) {
        return new PublicApiAuthors(findAuthors(initiativeId));
    }

    @Transactional(readOnly = false)
    public void rejectInvitation(Long initiativeId, String confirmCode) {
        authorDao.rejectAuthorInvitation(initiativeId, confirmCode);
    }

    private ContactInfo deleteVerifiedAuthor(Long initiativeId, VerifiedUserId authorToDelete) {

        VerifiedAuthor verifiedAuthor = authorDao.getVerifiedAuthor(initiativeId, authorToDelete);
        if (verifiedAuthor == null) {
            throw new NotFoundException("Author", "initiative: " + initiativeId + ", author: " + authorToDelete);
        }
        ContactInfo contactInfo = verifiedAuthor.getContactInfo();
        authorDao.deleteAuthorAndParticipant(initiativeId, authorToDelete);
        initiativeDao.denormalizeParticipantCounts(initiativeId);
        return contactInfo;
    }

    private ContactInfo deleteNormalAuthor(Long initiativeId, NormalAuthorId authorToDelete) {

        List<NormalAuthor> authors = authorDao.findNormalAuthors(initiativeId);

        if (!hasAuthor(authorToDelete, authors)) {
            throw new NotFoundException("Author", "initiative: " + initiativeId + ", author: " + authorToDelete);
        }
        else {
            ContactInfo deletedAuthorContactInfo = authorDao.getNormalAuthor(authorToDelete).getContactInfo();
            authorDao.deleteAuthorAndParticipant(initiativeId, authorToDelete);
            initiativeDao.denormalizeParticipantCounts(initiativeId);
            return deletedAuthorContactInfo;
        }
    }

    private static boolean hasAuthor(NormalAuthorId authorId, List<NormalAuthor> authors) {

        // Such lambdas! http://www.bafe.fi/meem/fiii.gif
        return authors.stream().anyMatch(a -> a.getId().equals(authorId));

    }

}
