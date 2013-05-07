package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.InvitationNotValidException;
import fi.om.municipalityinitiative.dao.NotFoundException;
import fi.om.municipalityinitiative.newdao.AuthorDao;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.LoginUserHolder;
import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.ManagementSettings;
import fi.om.municipalityinitiative.newdto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.newdto.ui.AuthorInvitationUIConfirmDto;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newweb.AuthorInvitationUICreateDto;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import fi.om.municipalityinitiative.util.SecurityUtil;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

public class AuthorService {

    @Resource
    AuthorDao authorDao;

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    ParticipantDao participantDao;

    @Resource
    EmailService emailService;

    @Transactional(readOnly = false)
    public void createAuthorInvitation(Long initiativeId, LoginUserHolder loginUserHolder, AuthorInvitationUICreateDto uiCreateDto) {

        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);
        SecurityUtil.assertAllowance("Invite authors", ManagementSettings.of(initiative).isAllowInviteAuthors());

        AuthorInvitation authorInvitation = new AuthorInvitation();
        authorInvitation.setInitiativeId(initiativeId);
        authorInvitation.setEmail(uiCreateDto.getAuthorEmail());
        authorInvitation.setName(uiCreateDto.getAuthorName());
        authorInvitation.setConfirmationCode(RandomHashGenerator.randomString(20));
        authorInvitation.setInvitationTime(new DateTime());

        authorDao.addAuthorInvitation(authorInvitation);
        emailService.sendAuthorInvitation(initiative, authorInvitation);

    }

    @Transactional(readOnly = true)
    public List<AuthorInvitation> findAuthorInvitations(Long initiativeId, LoginUserHolder loginUserHolder) {

        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        return authorDao.findInvitations(initiativeId);
    }

    @Transactional(readOnly = true)
    public List<Author> findAuthors(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        return authorDao.findAuthors(initiativeId);
    }

    @Transactional(readOnly = false)
    public void confirmAuthorInvitation(Long initiativeId, AuthorInvitationUIConfirmDto confirmDto) {

        ManagementSettings managementSettings = ManagementSettings.of(initiativeDao.getByIdWithOriginalAuthor(initiativeId));
        SecurityUtil.assertAllowance("Accept invitation", managementSettings.isAllowInviteAuthors());

        for (AuthorInvitation invitation : authorDao.findInvitations(initiativeId)) {

            if (invitation.getConfirmationCode().equals(confirmDto.getConfirmCode())) {

                assertNotRejectedOrExpired(invitation);

                ParticipantCreateDto participantCreateDto = ParticipantCreateDto.parse(confirmDto, initiativeId);
                String someConfirmationCode = "-";
                Long participantId = participantDao.create(participantCreateDto, someConfirmationCode);
                participantDao.confirmParticipation(participantId, someConfirmationCode);
                Long authorId = authorDao.createAuthor(initiativeId, participantId, RandomHashGenerator.randomString(40));
                authorDao.updateAuthorInformation(authorId, confirmDto.getContactInfo());
                authorDao.deleteAuthorInvitation(initiativeId, confirmDto.getConfirmCode());
                return;

            }
        }
        throw new NotFoundException("Invitation with ", "initiative: " + initiativeId + ", invitation: " + confirmDto.getConfirmCode());
    }

    public AuthorInvitationUIConfirmDto getPrefilledAuthorInvitationConfirmDto(Long initiativeId, String confirmCode) {
        AuthorInvitation authorInvitation = authorDao.getAuthorInvitation(initiativeId, confirmCode);

        assertNotRejectedOrExpired(authorInvitation);

        AuthorInvitationUIConfirmDto confirmDto = new AuthorInvitationUIConfirmDto();
        confirmDto.setInitiativeMunicipality(initiativeDao.getByIdWithOriginalAuthor(initiativeId).getMunicipality().getId());
        confirmDto.setContactInfo(new ContactInfo());
        confirmDto.getContactInfo().setName(authorInvitation.getName());
        confirmDto.getContactInfo().setEmail(authorInvitation.getEmail());
        confirmDto.setConfirmCode(authorInvitation.getConfirmationCode());
        return confirmDto;
    }


    private static void assertNotRejectedOrExpired(AuthorInvitation invitation) {
        if (invitation.isExpired()) {
            throw new InvitationNotValidException("Invitation is expired");
        }
        if (invitation.isRejected()) {
            throw new InvitationNotValidException("Invitation is rejected");
        }
    }
}
