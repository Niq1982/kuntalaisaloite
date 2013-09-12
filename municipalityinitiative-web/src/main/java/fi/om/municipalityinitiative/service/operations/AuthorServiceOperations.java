package fi.om.municipalityinitiative.service.operations;

import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dto.NormalAuthor;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.ui.AuthorInvitationUICreateDto;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.user.NormalLoginUser;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import fi.om.municipalityinitiative.util.SecurityUtil;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

public class AuthorServiceOperations {

    @Resource
    private InitiativeDao initiativeDao;

    @Resource
    private AuthorDao authorDao;


    @Transactional(readOnly = false)
    public AuthorInvitation doCreateAuthorInvitation(Long initiativeId, AuthorInvitationUICreateDto uiCreateDto) {
        Initiative initiative = initiativeDao.get(initiativeId);
        SecurityUtil.assertAllowance("Invite authors", ManagementSettings.of(initiative).isAllowInviteAuthors());

        AuthorInvitation authorInvitation = new AuthorInvitation();
        authorInvitation.setInitiativeId(initiativeId);
        authorInvitation.setEmail(uiCreateDto.getAuthorEmail());
        authorInvitation.setName(uiCreateDto.getAuthorName());
        authorInvitation.setConfirmationCode(RandomHashGenerator.shortHash());
        authorInvitation.setInvitationTime(new DateTime());

        authorDao.addAuthorInvitation(authorInvitation);
        return authorInvitation;
    }

    @Transactional(readOnly = false)
    public AuthorInvitation doResendInvitation(Long initiativeId, String confirmationCode) {

        Initiative initiative = initiativeDao.get(initiativeId);
        SecurityUtil.assertAllowance("Invite authors", ManagementSettings.of(initiative).isAllowInviteAuthors());

        AuthorInvitation authorInvitation = authorDao.getAuthorInvitation(initiativeId, confirmationCode);
        authorDao.deleteAuthorInvitation(initiativeId, confirmationCode);

        authorInvitation.setInvitationTime(DateTime.now());
        authorDao.addAuthorInvitation(authorInvitation);
        return authorInvitation;
    }

    @Transactional(readOnly = false)
    public ContactInfo doDeleteAuthor(Long initiativeId, Long authorId, User user) {

        if (initiativeDao.isVerifiableInitiative(initiativeId)) {
            VerifiedUser currentUser = (VerifiedUser) user;
            VerifiedUserId authorToDelete = new VerifiedUserId(authorId);

            if (currentUser.getAuthorId().toLong() == authorId) {
                throw new OperationNotAllowedException("Removing yourself from authors is not allowed");
            }

            return deleteVerifiedAuthor(initiativeId, authorToDelete);

        }

        else {
            NormalLoginUser currentUser = (NormalLoginUser) user;
            NormalAuthorId authorToDelete = new NormalAuthorId(authorId);

            if (currentUser.getAuthorId().toLong() == authorId) {
                throw new OperationNotAllowedException("Removing yourself from authors is not allowed");
            }

            return deleteNormalAuthor(initiativeId, authorToDelete);
        }
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
        for (NormalAuthor author : authors) {
            if (author.getId().equals(authorId)) {
                return true;
            }
        }
        return false;
    }
}
