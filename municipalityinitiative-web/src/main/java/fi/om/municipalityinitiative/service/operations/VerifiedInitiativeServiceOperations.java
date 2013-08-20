package fi.om.municipalityinitiative.service.operations;

import java.util.Locale;

import fi.om.municipalityinitiative.dao.*;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.AuthorInvitationUIConfirmDto;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.ui.PrepareSafeInitiativeUICreateDto;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.service.email.EmailService;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static fi.om.municipalityinitiative.util.SecurityUtil.assertAllowance;

public class VerifiedInitiativeServiceOperations {


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
    public Long doPrepareSafeInitiative(VerifiedUser verifiedUser, PrepareSafeInitiativeUICreateDto createDto) {
        assertMunicipalityIsActive(createDto.getMunicipality());

        Long initiativeId = initiativeDao.prepareSafeInitiative(createDto.getMunicipality(), createDto.getInitiativeType());
        VerifiedUserId verifiedUserId = getVerifiedUserIdAndCreateIfNecessary(verifiedUser.getHash(), verifiedUser.getContactInfo(), verifiedUser.getHomeMunicipality());

        boolean showName = true;
        participantDao.addVerifiedParticipant(initiativeId, verifiedUserId, showName, verifiedUser.getHomeMunicipality().isPresent());
        authorDao.addVerifiedAuthor(initiativeId, verifiedUserId);

        return initiativeId;

    }

    @Transactional(readOnly = false)
    public VerifiedUser doConfirmInvitation(VerifiedUser verifiedUser, Long initiativeId, AuthorInvitationUIConfirmDto confirmDto, Locale locale) {

        Initiative initiative = initiativeDao.get(initiativeId);
        assertAllowance("Accept invitation", ManagementSettings.of(initiative).isAllowInviteAuthors());
        assertAllowance("Accept verifiable invitation", InitiativeType.isVerifiable(initiative.getType()));

        for (AuthorInvitation invitation : authorDao.findInvitations(initiativeId)) {

            if (invitation.getConfirmationCode().equals(confirmDto.getConfirmCode())) {
                invitation.assertNotRejectedOrExpired();

                VerifiedUserId verifiedUserId = getVerifiedUserIdAndCreateIfNecessary(verifiedUser.getHash(), verifiedUser.getContactInfo(), verifiedUser.getHomeMunicipality());
                userDao.updateUserInformation(verifiedUser.getHash(), confirmDto.getContactInfo());

                participantDao.addVerifiedParticipant(initiativeId, verifiedUserId, confirmDto.getContactInfo().isShowName(), verifiedUser.getHomeMunicipality().isPresent());
                authorDao.addVerifiedAuthor(initiativeId, verifiedUserId);

                authorDao.deleteAuthorInvitation(initiativeId, confirmDto.getConfirmCode());
                
                emailService.sendAuthorConfirmedInvitation(initiativeId, invitation.getEmail(), null, locale);
                
                return verifiedUser;

            }

        }

        throw new NotFoundException(AuthorInvitation.class.getName(), "initiative: " + initiativeId + ", invitation: " + confirmDto.getConfirmCode());
    }

    @Transactional(readOnly = false)
    public void doCreateParticipant(VerifiedUser verifiedUser, Long initiativeId, Boolean showName) {

        assertAllowance("Participate to initiative", ManagementSettings.of(initiativeDao.get(initiativeId)).isAllowParticipate());

        VerifiedUserId verifiedUserId = getVerifiedUserIdAndCreateIfNecessary(verifiedUser.getHash(), verifiedUser.getContactInfo(), verifiedUser.getHomeMunicipality());
        participantDao.addVerifiedParticipant(initiativeId, verifiedUserId, showName, verifiedUser.getHomeMunicipality().isPresent());
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
