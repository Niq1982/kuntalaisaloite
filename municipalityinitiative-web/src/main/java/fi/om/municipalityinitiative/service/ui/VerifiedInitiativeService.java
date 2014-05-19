package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dao.*;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.AuthorInvitationUIConfirmDto;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.dto.ui.PrepareSafeInitiativeUICreateDto;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.exceptions.InvalidHomeMunicipalityException;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.service.email.EmailService;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Locale;

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
    public long prepareVerifiedInitiative(LoginUserHolder loginUserHolder, PrepareSafeInitiativeUICreateDto uiCreateDto) {

        VerifiedUser verifiedUser = loginUserHolder.getVerifiedUser();

        if (municipalityMismatch(uiCreateDto.getMunicipality(), uiCreateDto.getUserGivenHomeMunicipality(), verifiedUser.getHomeMunicipality())) {
            return municipalityException(uiCreateDto.getMunicipality());
        }

        assertMunicipalityIsActive(uiCreateDto.getMunicipality());

        Long initiativeId = initiativeDao.prepareVerifiedInitiative(uiCreateDto.getMunicipality(), uiCreateDto.getInitiativeType());
        VerifiedUserId verifiedUserId = getVerifiedUserIdAndCreateIfNecessary(verifiedUser.getHash(), verifiedUser.getContactInfo(), verifiedUser.getHomeMunicipality());

        boolean showName = true;
        participantDao.addVerifiedParticipant(initiativeId, verifiedUserId, showName, verifiedUser.getHomeMunicipality().isPresent());
        authorDao.addVerifiedAuthor(initiativeId, verifiedUserId);

        return initiativeId;

    }

    private static long municipalityException(Long municipality) {
        throw new InvalidHomeMunicipalityException("Unable to create initiative for municipality with id " + municipality);
    }

    @Transactional(readOnly = false)
    public void confirmVerifiedAuthorInvitation(LoginUserHolder loginUserHolder, Long initiativeId, AuthorInvitationUIConfirmDto confirmDto, Locale locale) {
        VerifiedUser verifiedUser = loginUserHolder.getVerifiedUser();
        if (municipalityMismatch(confirmDto.getMunicipality(), confirmDto.getHomeMunicipality(), verifiedUser.getHomeMunicipality())) {
            municipalityException(confirmDto.getMunicipality());
        }

        Initiative initiative = initiativeDao.get(initiativeId);
        assertAllowance("Accept invitation", ManagementSettings.of(initiative).isAllowInviteAuthors());
        assertAllowance("Accept verifiable invitation", InitiativeType.isVerifiable(initiative.getType()));

        for (AuthorInvitation invitation : authorDao.findInvitations(initiativeId)) {

            if (invitation.getConfirmationCode().equals(confirmDto.getConfirmCode())) {
                invitation.assertNotRejectedOrExpired();

                VerifiedUserId verifiedUserId = getVerifiedUserIdAndCreateIfNecessary(verifiedUser.getHash(), verifiedUser.getContactInfo(), verifiedUser.getHomeMunicipality());
                userDao.updateUserInformation(verifiedUser.getHash(), confirmDto.getContactInfo());

                // TODO: Improve
                if (!userDao.getVerifiedUser(verifiedUser.getHash()).get().getInitiativesWithParticipation().contains(initiativeId)) {
                    participantDao.addVerifiedParticipant(initiativeId, verifiedUserId, confirmDto.getContactInfo().isShowName(), verifiedUser.getHomeMunicipality().isPresent());
                }

                authorDao.addVerifiedAuthor(initiativeId, verifiedUserId);
                authorDao.deleteAuthorInvitation(initiativeId, confirmDto.getConfirmCode());
                emailService.sendAuthorConfirmedInvitation(initiativeId, invitation.getEmail(), null, locale);
                return;
            }

        }

        throw new NotFoundException(AuthorInvitation.class.getName(), "initiative: " + initiativeId + ", invitation: " + confirmDto.getConfirmCode());

    }

    @Transactional(readOnly = false)
    public void createParticipant(ParticipantUICreateDto createDto, Long initiativeId, LoginUserHolder loginUserHolder) {
        VerifiedUser verifiedUser = loginUserHolder.getVerifiedUser();

        if (municipalityMismatch(createDto.getMunicipality(), createDto.getHomeMunicipality(), verifiedUser.getHomeMunicipality())) {
            municipalityException(createDto.getMunicipality());
        }
        assertAllowance("Participate to initiative", ManagementSettings.of(initiativeDao.get(initiativeId)).isAllowParticipation());

        VerifiedUserId verifiedUserId = getVerifiedUserIdAndCreateIfNecessary(verifiedUser.getHash(), verifiedUser.getContactInfo(), verifiedUser.getHomeMunicipality());
        participantDao.addVerifiedParticipant(initiativeId, verifiedUserId, createDto.getShowName(), verifiedUser.getHomeMunicipality().isPresent());
    }

    private static boolean municipalityMismatch(Long initiativeMunicipality, Long userGivenHomeMunicipality, Maybe<Municipality> vetumaMunicipality) {
        return vetumaMunicipalityReceivedAndMismatches(vetumaMunicipality, initiativeMunicipality)
                || vetumaMunicipalityNotReceivedAndUserGivenMismatches(vetumaMunicipality, initiativeMunicipality, userGivenHomeMunicipality);
    }


    private static boolean vetumaMunicipalityNotReceivedAndUserGivenMismatches(Maybe<Municipality> vetumaMunicipality, Long initiativeMunicipality, Long userGivenHomeMunicipality) {
        return vetumaMunicipality.isNotPresent()
                && !initiativeMunicipality.equals(userGivenHomeMunicipality);
    }

    private static boolean vetumaMunicipalityReceivedAndMismatches(Maybe<Municipality> vetumaMunicipality, Long initiativeMunicipality) {
        return vetumaMunicipality.isPresent()
                && !initiativeMunicipality.equals(vetumaMunicipality.get().getId());
    }

    private VerifiedUserId getVerifiedUserIdAndCreateIfNecessary(String hash, ContactInfo contactInfo, Maybe<Municipality> homeMunicipality) {

        Maybe<VerifiedUserId> verifiedUserId = userDao.getVerifiedUserId(hash);
        if (verifiedUserId.isNotPresent()) {
            verifiedUserId = Maybe.of(userDao.addVerifiedUser(hash, contactInfo, homeMunicipality));
        }
        return verifiedUserId.get();
    }

    private void assertMunicipalityIsActive(Long municipality) {
        if (!municipalityDao.getMunicipality(municipality).isActive()) {
            throw new AccessDeniedException("Municipality is not active for initiatives: " + municipality);
        }
    }
}
