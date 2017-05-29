package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dao.*;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.*;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.service.email.EmailService;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Membership;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Locale;
import java.util.Optional;

import static fi.om.municipalityinitiative.util.SecurityUtil.assertAllowance;

public class VerifiedInitiativeService {

    @Resource
    private MunicipalityDao municipalityDao;

    @Resource
    private ParticipantDao participantDao;

    @Resource
    private EmailService emailService;

    @Resource
    private InitiativeDao initiativeDao;

    @Resource
    private UserDao userDao;

    @Resource
    private AuthorDao authorDao;

    @Transactional(readOnly = false)
    public long prepareVerifiedInitiative(VerifiedUser verifiedUser, PrepareVerifiedInitiativeUICreateDto uiCreateDto) {

        MunicipalMembershipSolver municipalMembershipSolver = new MunicipalMembershipSolver(verifiedUser, uiCreateDto.getMunicipality(), uiCreateDto);

        municipalMembershipSolver.assertMunicipalityForVerifiedInitiative();

        assertMunicipalityIsActive(uiCreateDto.getMunicipality());

        Long initiativeId = initiativeDao.prepareInitiative(uiCreateDto.getMunicipality(), uiCreateDto.getInitiativeType());
        VerifiedUserId verifiedUserId = getVerifiedUserIdAndCreateIfNecessary(verifiedUser.getHash(), verifiedUser.getContactInfo(), verifiedUser.getHomeMunicipality());

        boolean showName = true;
        participantDao.addVerifiedParticipant(
                initiativeId,
                verifiedUserId,
                verifiedUser.getContactInfo().getName(), showName,
                municipalMembershipSolver.isHomeMunicipalityVerified(),
                uiCreateDto.getMunicipality(), // Participants municipality is always initiatives municipality
                Membership.none);

        authorDao.addVerifiedAuthor(initiativeId, verifiedUserId);
        participantDao.increaseParticipantCountFor(initiativeId, showName, true);

        return initiativeId;

    }

    @Transactional(readOnly = false)
    public Long prepareNormalInitiative(VerifiedUser verifiedUser, PrepareInitiativeUICreateDto uiCreateDto) {

        assertMunicipalityIsActive(uiCreateDto.getMunicipality());

        MunicipalMembershipSolver municipalMembershipSolver = new MunicipalMembershipSolver(verifiedUser, uiCreateDto.getMunicipality(), uiCreateDto);

        municipalMembershipSolver.assertMunicipalityOrMembershipForNormalInitiative();

        Long initiativeId = initiativeDao.prepareInitiative(uiCreateDto.getMunicipality(), InitiativeType.UNDEFINED);
        VerifiedUserId verifiedUserId = getVerifiedUserIdAndCreateIfNecessary(verifiedUser.getHash(), verifiedUser.getContactInfo(), verifiedUser.getHomeMunicipality());

        boolean showName = true;
        participantDao.addVerifiedParticipant(initiativeId,
                verifiedUserId,
                verifiedUser.getContactInfo().getName(),
                showName,
                municipalMembershipSolver.isHomeMunicipalityVerified(),
                municipalMembershipSolver.getHomeMunicipality(),
                municipalMembershipSolver.getMunicipalMembership());


        participantDao.increaseParticipantCountFor(initiativeId, showName, municipalMembershipSolver.homeMunicipalityMatches());
        authorDao.addVerifiedAuthor(initiativeId, verifiedUserId);

        return initiativeId;

    }

    @Transactional(readOnly = false)
    public void confirmVerifiedAuthorInvitation(VerifiedUser verifiedUser, Long initiativeId, AuthorInvitationUIConfirmDto confirmDto, Locale locale) {
        Initiative initiative = initiativeDao.get(initiativeId);

        MunicipalMembershipSolver municipalMembershipSolver = new MunicipalMembershipSolver(verifiedUser, initiative.getMunicipality().getId(), confirmDto);

        if (initiative.getType().isVerifiable()) {
            municipalMembershipSolver.assertMunicipalityForVerifiedInitiative();
        }
        else {
            municipalMembershipSolver.assertMunicipalityOrMembershipForNormalInitiative();
        }

        assertAllowance("Accept invitation", ManagementSettings.of(initiative).isAllowInviteAuthors());

        for (AuthorInvitation invitation : authorDao.findInvitations(initiativeId)) {

            if (invitation.getConfirmationCode().equals(confirmDto.getConfirmCode())) {
                invitation.assertNotRejectedOrExpired();

                VerifiedUserId verifiedUserId = getVerifiedUserIdAndCreateIfNecessary(verifiedUser.getHash(), verifiedUser.getContactInfo(), verifiedUser.getHomeMunicipality());
                userDao.updateUserInformation(verifiedUser.getHash(), confirmDto.getContactInfo());

                // TODO: Improve
                if (!userDao.getVerifiedUser(verifiedUser.getHash()).get().getInitiativesWithParticipation().contains(initiativeId)) {
                    participantDao.addVerifiedParticipant(
                            initiativeId,
                            verifiedUserId,
                            verifiedUser.getContactInfo().getName(), confirmDto.getContactInfo().isShowName(),
                            verifiedUser.getHomeMunicipality().isPresent(),
                            municipalMembershipSolver.getHomeMunicipality(),
                            municipalMembershipSolver.getMunicipalMembership()
                            );
                }
                participantDao.increaseParticipantCountFor(initiativeId, confirmDto.getContactInfo().isShowName(), municipalMembershipSolver.homeMunicipalityMatches());
                authorDao.addVerifiedAuthor(initiativeId, verifiedUserId);
                authorDao.deleteAuthorInvitation(initiativeId, confirmDto.getConfirmCode());
                emailService.sendAuthorConfirmedInvitation(initiativeId, invitation.getEmail(), null, locale);
                return;
            }

        }

        throw new NotFoundException(AuthorInvitation.class.getName(), "initiative: " + initiativeId + ", invitation: " + confirmDto.getConfirmCode());

    }

    @Transactional(readOnly = false)
    public void createParticipant(ParticipantUICreateDto createDto, Long initiativeId, VerifiedUser verifiedUser) {

        Initiative initiative = initiativeDao.get(initiativeId);

        MunicipalMembershipSolver municipalMembershipSolver = new MunicipalMembershipSolver(verifiedUser, initiative.getMunicipality().getId(), createDto);

        if (initiative.getType().isVerifiable()) {
            municipalMembershipSolver.assertMunicipalityForVerifiedInitiative();
        }
        else {
            municipalMembershipSolver.assertMunicipalityOrMembershipForNormalInitiative();
        }

        assertAllowance("Participate to initiative", ManagementSettings.of(initiative).isAllowParticipation());

        VerifiedUserId verifiedUserId = getVerifiedUserIdAndCreateIfNecessary(verifiedUser.getHash(), verifiedUser.getContactInfo(), verifiedUser.getHomeMunicipality());
        participantDao.addVerifiedParticipant(initiativeId,
                verifiedUserId,
                verifiedUser.getContactInfo().getName(), createDto.getShowName(),
                municipalMembershipSolver.isHomeMunicipalityVerified(),
                municipalMembershipSolver.getHomeMunicipality(),
                createDto.getMunicipalMembership()
        );
        participantDao.increaseParticipantCountFor(initiativeId, createDto.getShowName(), municipalMembershipSolver.homeMunicipalityMatches());
    }


    private VerifiedUserId getVerifiedUserIdAndCreateIfNecessary(String hash, ContactInfo contactInfo, Optional<Municipality> homeMunicipality) {

        Optional<VerifiedUserId> verifiedUserId = userDao.getVerifiedUserId(hash);
        if (!verifiedUserId.isPresent()) {
            verifiedUserId = Optional.of(userDao.addVerifiedUser(hash, contactInfo, homeMunicipality));
        }
        return verifiedUserId.get();
    }

    private void assertMunicipalityIsActive(Long municipality) {
        if (!municipalityDao.getMunicipality(municipality).isActive()) {
            throw new AccessDeniedException("Municipality is not active for initiatives: " + municipality);
        }
    }


}
