package fi.om.municipalityinitiative.service.operations;

import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.MunicipalityDao;
import fi.om.municipalityinitiative.dao.ParticipantDao;
import fi.om.municipalityinitiative.dto.ui.PrepareInitiativeUICreateDto;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.service.email.EmailService;
import fi.om.municipalityinitiative.util.Membership;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class PublicInitiativeServiceOperations {

    @Resource
    private InitiativeDao initiativeDao;

    @Resource
    private AuthorDao authorDao;

    @Resource
    private ParticipantDao participantDao;

    @Resource
    private MunicipalityDao municipalityDao;

    @Transactional(readOnly = false)
    public PreparedInitiativeData doPrepareInitiative(PrepareInitiativeUICreateDto createDto) {
        assertMunicipalityIsActive(createDto.getMunicipality());

        PreparedInitiativeData preparedInitiativeData = new PreparedInitiativeData();

        preparedInitiativeData.initiativeId = initiativeDao.prepareInitiative(createDto.getMunicipality());
        Long participantId = participantDao.prepareParticipant(preparedInitiativeData.initiativeId,
                createDto.getHomeMunicipality(),
                createDto.getParticipantEmail(),
                createDto.hasMunicipalMembership() ? createDto.getMunicipalMembership() : Membership.none
        );
        preparedInitiativeData.managementHash = RandomHashGenerator.longHash();
        preparedInitiativeData.authorId = authorDao.createAuthor(preparedInitiativeData.initiativeId, participantId, preparedInitiativeData.managementHash);
        return preparedInitiativeData;
    }

    private void assertMunicipalityIsActive(Long municipality) {
        if (!municipalityDao.getMunicipality(municipality).isActive()) {
            throw new AccessDeniedException("Municipality is not active for initiatives: " + municipality);
        }
    }

    public static class PreparedInitiativeData {

        public Long initiativeId;
        public String managementHash;
        public Long authorId;
    }
}
