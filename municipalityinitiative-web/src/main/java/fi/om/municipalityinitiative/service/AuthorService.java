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
import fi.om.municipalityinitiative.newdto.ui.PublicAuthors;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newweb.AuthorInvitationUICreateDto;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import fi.om.municipalityinitiative.util.SecurityUtil;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;
import java.util.Locale;

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

    @Transactional(readOnly = false)
    public void resendInvitation(Long initiativeId, LoginUserHolder loginUserHolder, String confirmationCode) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        AuthorInvitation authorInvitation = authorDao.getAuthorInvitation(initiativeId, confirmationCode);
        authorDao.deleteAuthorInvitation(initiativeId, confirmationCode);

        authorInvitation.setInvitationTime(DateTime.now());
        authorDao.addAuthorInvitation(authorInvitation);
        emailService.sendAuthorInvitation(initiativeDao.getByIdWithOriginalAuthor(initiativeId), authorInvitation);

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
    public String confirmAuthorInvitation(Long initiativeId, AuthorInvitationUIConfirmDto confirmDto, Locale locale) {

        ManagementSettings managementSettings = ManagementSettings.of(initiativeDao.getByIdWithOriginalAuthor(initiativeId));
        SecurityUtil.assertAllowance("Accept invitation", managementSettings.isAllowInviteAuthors());

        for (AuthorInvitation invitation : authorDao.findInvitations(initiativeId)) {

            if (invitation.getConfirmationCode().equals(confirmDto.getConfirmCode())) {

                assertNotRejectedOrExpired(invitation);

                String managementHash = createAuthorAndParticipant(initiativeId, confirmDto);
                authorDao.deleteAuthorInvitation(initiativeId, confirmDto.getConfirmCode());
                emailService.sendAuthorConfirmedInvitation(initiativeDao.getByIdWithOriginalAuthor(initiativeId), invitation.getEmail(), managementHash, locale);
                return managementHash;

            }
        }
        throw new NotFoundException("Invitation with ", "initiative: " + initiativeId + ", invitation: " + confirmDto.getConfirmCode());
    }

    private String createAuthorAndParticipant(Long initiativeId, AuthorInvitationUIConfirmDto confirmDto) {
        ParticipantCreateDto participantCreateDto = ParticipantCreateDto.parse(confirmDto, initiativeId);
        String managementHash = RandomHashGenerator.randomString(40);
        Long participantId = participantDao.prepareParticipant(initiativeId, confirmDto.getHomeMunicipality(), participantCreateDto.getEmail(), participantCreateDto.getMunicipalMembership(), true);
        Long authorId = authorDao.createAuthor(initiativeId, participantId, managementHash);
        authorDao.updateAuthorInformation(authorId, confirmDto.getContactInfo());
        return managementHash;
    }

    @Transactional(readOnly = false)
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

    @Transactional(readOnly = true)
    public PublicAuthors findPublicAuthors(Long initiativeId) {
        return new PublicAuthors(authorDao.findAuthors(initiativeId));
    }
    
    @Transactional(readOnly = false)
    public void rejectInvitation(Long initiativeId, String confirmCode) {
        authorDao.rejectAuthorInvitation(initiativeId, confirmCode);
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
