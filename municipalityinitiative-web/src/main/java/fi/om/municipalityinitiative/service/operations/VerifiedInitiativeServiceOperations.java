package fi.om.municipalityinitiative.service.operations;

import fi.om.municipalityinitiative.dao.*;
import fi.om.municipalityinitiative.dto.service.PrepareSafeInitiativeCreateDto;
import fi.om.municipalityinitiative.dto.ui.AuthorInvitationUIConfirmDto;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.ui.PrepareSafeInitiativeUICreateDto;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.Maybe;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class VerifiedInitiativeServiceOperations {


    @Resource
    private MunicipalityDao municipalityDao;

    @Resource
    private ParticipantDao participantDao;

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
        VerifiedUserId verifiedUserId = getVerifiedUserIdAndCreateIfNecessary(verifiedUser.getHash(), verifiedUser.getContactInfo());

        participantDao.addVerifiedParticipant(initiativeId, verifiedUserId);
        authorDao.addVerifiedAuthor(initiativeId, verifiedUserId);

        return initiativeId;

    }

    @Transactional(readOnly = false)
    public void doConfirmInvitation(VerifiedUser verifiedUser, Long initiativeId, AuthorInvitationUIConfirmDto confirmDto) {

        confirmDto.getConfirmCode(); // TODO: use this plz

        VerifiedUserId verifiedUserId = getVerifiedUserIdAndCreateIfNecessary(verifiedUser.getHash(), verifiedUser.getContactInfo());
        userDao.updateUserInformation(verifiedUser.getHash(), confirmDto.getContactInfo());

        participantDao.addVerifiedParticipant(initiativeId, verifiedUserId);
        authorDao.addVerifiedAuthor(initiativeId, verifiedUserId);

    }

    private VerifiedUserId getVerifiedUserIdAndCreateIfNecessary(String hash, ContactInfo contactInfo) {

        Maybe<VerifiedUserId> verifiedUserId = userDao.getVerifiedUserId(hash);
        if (verifiedUserId.isNotPresent()) {
            verifiedUserId = Maybe.of(userDao.addVerifiedUser(hash, contactInfo));
        }
        return verifiedUserId.get();
    }

    private void assertMunicipalityIsActive(Long municipality) {
        if (!municipalityDao.getMunicipality(municipality).isActive()) {
            throw new AccessDeniedException("Municipality is not active for initiatives: " + municipality);
        }
    }
}
