package fi.om.municipalityinitiative.service.operations;

import fi.om.municipalityinitiative.dao.*;
import fi.om.municipalityinitiative.dto.service.PrepareSafeInitiativeCreateDto;
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
    public Long doPrepareSafeInitiative(PrepareSafeInitiativeCreateDto createDto) {
        // TODO: In progress
        assertMunicipalityIsActive(createDto.getMunicipality());

        Long initiativeId = initiativeDao.prepareSafeInitiative(createDto.getMunicipality(), createDto.getInitiativeType());
        VerifiedUserId verifiedUserId = getVerifiedUserIdAndCreateIfNecessary(createDto);

        participantDao.addVerifiedParticipant(initiativeId, verifiedUserId);
        authorDao.addVerifiedAuthor(initiativeId, verifiedUserId);

        return initiativeId;

    }

    private VerifiedUserId getVerifiedUserIdAndCreateIfNecessary(PrepareSafeInitiativeCreateDto createDto) {
        Maybe<VerifiedUserId> verifiedUserId = userDao.getVerifiedUserId(createDto.getHash());
        if (verifiedUserId.isNotPresent()) {
            verifiedUserId = Maybe.of(userDao.addVerifiedUser(createDto.getHash(), createDto.getContactInfo()));
        }
        return verifiedUserId.get();
    }

    private void assertMunicipalityIsActive(Long municipality) {
        if (!municipalityDao.getMunicipality(municipality).isActive()) {
            throw new AccessDeniedException("Municipality is not active for initiatives: " + municipality);
        }
    }
}
