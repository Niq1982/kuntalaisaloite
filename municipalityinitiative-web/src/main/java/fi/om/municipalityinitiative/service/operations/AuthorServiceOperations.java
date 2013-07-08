package fi.om.municipalityinitiative.service.operations;

import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.NormalAuthor;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.ui.AuthorInvitationUICreateDto;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
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
    public ContactInfo doDeleteAuthor(Long initiativeId, Long authorId) {
        List<NormalAuthor> authors = authorDao.findNormalAuthors(initiativeId);
        if (!hasAuthor(authorId, authors)) {
            throw new NotFoundException("Author", "initiative: " + initiativeId + ", author: " + authorId);
        }
        else if (authors.size() < 2) {
            throw new OperationNotAllowedException("Unable to delete author. Initiative has only " + authors.size() +" author(s)");
        }
        else {
            ContactInfo deletedAuthorContactInfo = authorDao.getAuthor(authorId).getContactInfo();
            authorDao.deleteAuthor(authorId);
            return deletedAuthorContactInfo;
        }
    }

    private static boolean hasAuthor(Long authorId, List<NormalAuthor> authors) {
        for (Author author : authors) {
            if (author.getId().toLong() == authorId.longValue()) {
                return true;
            }
        }
        return false;
    }
}
