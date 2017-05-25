package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dao.*;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.*;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.exceptions.InvalidHomeMunicipalityException;
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
    public long prepareVerifiedInitiative(VerifiedUser verifiedUser, PrepareSafeInitiativeUICreateDto uiCreateDto) {

        requireCorrectHomeMunicipality(verifiedUser, uiCreateDto.getMunicipality(), uiCreateDto.getUserGivenHomeMunicipality());
        assertMunicipalityIsActive(uiCreateDto.getMunicipality());

        Long initiativeId = initiativeDao.prepareInitiative(uiCreateDto.getMunicipality(), uiCreateDto.getInitiativeType());
        VerifiedUserId verifiedUserId = getVerifiedUserIdAndCreateIfNecessary(verifiedUser.getHash(), verifiedUser.getContactInfo(), verifiedUser.getHomeMunicipality());

        boolean showName = true;
        participantDao.addVerifiedParticipant(
                initiativeId,
                verifiedUserId,
                showName,
                verifiedUser.getHomeMunicipality().isPresent(),
                uiCreateDto.getMunicipality(),
                Membership.none);

        authorDao.addVerifiedAuthor(initiativeId, verifiedUserId);
        participantDao.increaseParticipantCountFor(initiativeId, showName, true);

        return initiativeId;

    }

    @Transactional(readOnly = false)
    public Long prepareNormalInitiative(LoginUserHolder loginUserHolder, PrepareInitiativeUICreateDto uiCreateDto) {
        VerifiedUser verifiedUser = loginUserHolder.getVerifiedUser();

        // TODO Make this accept municipalMembership if everything ok
//        if (municipalityMismatch(uiCreateDto.getMunicipality(), uiCreateDto.getHomeMunicipality(), verifiedUser.getHomeMunicipality())) {
//            return municipalityException(uiCreateDto.getMunicipality());
//        }

        assertMunicipalityIsActive(uiCreateDto.getMunicipality());

        Long initiativeId = initiativeDao.prepareInitiative(uiCreateDto.getMunicipality(), InitiativeType.UNDEFINED);
        VerifiedUserId verifiedUserId = getVerifiedUserIdAndCreateIfNecessary(verifiedUser.getHash(), verifiedUser.getContactInfo(), verifiedUser.getHomeMunicipality());

        boolean showName = true;
        Long homeMunicipality = verifiedUser.getHomeMunicipality().isPresent() ? verifiedUser.getHomeMunicipality().get().getId() : uiCreateDto.getHomeMunicipality();
        participantDao.addVerifiedParticipant(initiativeId,
                verifiedUserId,
                showName,
                verifiedUser.getHomeMunicipality().isPresent(),
                homeMunicipality,
                uiCreateDto.getMunicipalMembership());


        participantDao.increaseParticipantCountFor(initiativeId, showName, homeMunicipality.equals(uiCreateDto.getMunicipality()));
        authorDao.addVerifiedAuthor(initiativeId, verifiedUserId);

        return initiativeId;

    }

    private static long municipalityException(Long municipality) {
        throw new InvalidHomeMunicipalityException("Unable to create initiative for municipality with id " + municipality);
    }

    @Transactional(readOnly = false)
    public void confirmVerifiedAuthorInvitation(LoginUserHolder loginUserHolder, Long initiativeId, AuthorInvitationUIConfirmDto confirmDto, Locale locale) {
        VerifiedUser verifiedUser = loginUserHolder.getVerifiedUser();
        Initiative initiative = initiativeDao.get(initiativeId);

        if (initiative.getType().isVerifiable()) {
            requireCorrectHomeMunicipality(verifiedUser, confirmDto.getMunicipality(), confirmDto.getHomeMunicipality());
        }

        Long homeMunicipalityId = loginUserHolder.getVerifiedUser().getHomeMunicipality().isPresent() ? loginUserHolder.getVerifiedUser().getHomeMunicipality().get().getId() : confirmDto.getHomeMunicipality();

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
                            confirmDto.getContactInfo().isShowName(),
                            verifiedUser.getHomeMunicipality().isPresent(),
                            homeMunicipalityId,
                            Membership.none // FIXME: MEMBERSHIP ?
                            );
                }
                participantDao.increaseParticipantCountFor(initiativeId, confirmDto.getContactInfo().isShowName(), homeMunicipalityId.equals(initiative.getMunicipality().getId()));
                authorDao.addVerifiedAuthor(initiativeId, verifiedUserId);
                authorDao.deleteAuthorInvitation(initiativeId, confirmDto.getConfirmCode());
                emailService.sendAuthorConfirmedInvitation(initiativeId, invitation.getEmail(), null, locale);
                return;
            }

        }

        throw new NotFoundException(AuthorInvitation.class.getName(), "initiative: " + initiativeId + ", invitation: " + confirmDto.getConfirmCode());

    }

    private static void requireCorrectHomeMunicipality(VerifiedUser verifiedUser, Long municipality, Long homeMunicipality) {
        if (municipalityMismatch(municipality, homeMunicipality, verifiedUser.getHomeMunicipality())) {
            municipalityException(municipality);
        }
    }

    @Transactional(readOnly = false)
    public void createParticipant(ParticipantUICreateDto createDto, Long initiativeId, LoginUserHolder loginUserHolder) {
        VerifiedUser verifiedUser = loginUserHolder.getVerifiedUser();

        Long homeMunicipalityId = verifiedUser.getHomeMunicipality().isPresent() ? verifiedUser.getHomeMunicipality().get().getId() : createDto.getHomeMunicipality();

        Initiative initiative = initiativeDao.get(initiativeId);
        if (initiative.getType().isVerifiable()) {
            requireCorrectHomeMunicipality(verifiedUser, createDto.getMunicipality(), homeMunicipalityId);
        }

        assertAllowance("Participate to initiative", ManagementSettings.of(initiative).isAllowParticipation());

        VerifiedUserId verifiedUserId = getVerifiedUserIdAndCreateIfNecessary(verifiedUser.getHash(), verifiedUser.getContactInfo(), verifiedUser.getHomeMunicipality());
        participantDao.addVerifiedParticipant(initiativeId, verifiedUserId, createDto.getShowName(), verifiedUser.getHomeMunicipality().isPresent(), homeMunicipalityId, createDto.getMunicipalMembership());
        participantDao.increaseParticipantCountFor(initiativeId, createDto.getShowName(), homeMunicipalityId.equals(initiative.getMunicipality().getId()));
    }

    private static boolean municipalityMismatch(Long initiativeMunicipality, Long userGivenHomeMunicipality, Optional<Municipality> vetumaMunicipality) {
        return vetumaMunicipalityReceivedAndMismatches(vetumaMunicipality, initiativeMunicipality)
                || vetumaMunicipalityNotReceivedAndUserGivenMismatches(vetumaMunicipality, initiativeMunicipality, userGivenHomeMunicipality);
    }


    private static boolean vetumaMunicipalityNotReceivedAndUserGivenMismatches(Optional<Municipality> vetumaMunicipality, Long initiativeMunicipality, Long userGivenHomeMunicipality) {
        return !vetumaMunicipality.isPresent()
                && !initiativeMunicipality.equals(userGivenHomeMunicipality);
    }

    private static boolean vetumaMunicipalityReceivedAndMismatches(Optional<Municipality> vetumaMunicipality, Long initiativeMunicipality) {
        return vetumaMunicipality.isPresent()
                && !initiativeMunicipality.equals(vetumaMunicipality.get().getId());
    }

    public VerifiedUserId getVerifiedUserIdAndCreateIfNecessary(String hash, ContactInfo contactInfo, Optional<Municipality> homeMunicipality) {

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
