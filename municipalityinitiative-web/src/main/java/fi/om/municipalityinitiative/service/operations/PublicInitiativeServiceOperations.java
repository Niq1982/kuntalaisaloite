package fi.om.municipalityinitiative.service.operations;

import fi.om.municipalityinitiative.dao.*;
import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.service.AuthorMessage;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.dto.ui.AuthorUIMessage;
import fi.om.municipalityinitiative.dto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.dto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.dto.ui.PrepareInitiativeUICreateDto;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.util.Membership;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static fi.om.municipalityinitiative.util.SecurityUtil.assertAllowance;

public class PublicInitiativeServiceOperations {

    @Resource
    private InitiativeDao initiativeDao;

    @Resource
    private AuthorDao authorDao;

    @Resource
    private ParticipantDao participantDao;

    @Resource
    private MunicipalityDao municipalityDao;

    @Resource
    private AuthorMessageDao authorMessageDao;

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

    @Transactional(readOnly = false)
    public ParticipantCreatedData doCreateParticipant(ParticipantUICreateDto participant, Long initiativeId) {

        assertAllowance("Allowed to participate", getManagementSettings(initiativeId).isAllowParticipate());

        ParticipantCreateDto participantCreateDto = ParticipantCreateDto.parse(participant, initiativeId);
        participantCreateDto.setMunicipalityInitiativeId(initiativeId);

        ParticipantCreatedData participantCreatedData = new ParticipantCreatedData();

        participantCreatedData.confirmationCode = RandomHashGenerator.shortHash();
        participantCreatedData.participantId = participantDao.create(participantCreateDto, participantCreatedData.confirmationCode);
        return participantCreatedData;
    }

    @Transactional(readOnly = false)
    public AuthorMessage doAddAuthorMessage(AuthorUIMessage authorUIMessage) {
            String confirmationCode = RandomHashGenerator.shortHash();
            AuthorMessage authorMessage = new AuthorMessage(authorUIMessage, confirmationCode);
            authorMessageDao.put(authorMessage);
            return authorMessage;
    }

    @Transactional(readOnly = false)
    public AuthorMessage doConfirmAuthorMessage(String confirmationCode) {
        return authorMessageDao.pop(confirmationCode);
    }

    @Transactional(readOnly = false)
    public Long doConfirmParticipation(Long participantId, String confirmationCode) {
        Long initiativeId = participantDao.getInitiativeIdByParticipant(participantId);
        assertAllowance("Confirm participation", getManagementSettings(initiativeId).isAllowParticipate());

        participantDao.confirmParticipation(participantId, confirmationCode);

        return initiativeId;
    }

    private ManagementSettings getManagementSettings(Long initiativeId) {
        return ManagementSettings.of(initiativeDao.get(initiativeId));
    }

    @Transactional(readOnly = true)
    public List<InitiativeListInfo> findInitiatives(InitiativeSearch search) {
        return initiativeDao.find(search);
    }

    public static class PreparedInitiativeData {

        public Long initiativeId;
        public String managementHash;
        public Long authorId;
    }

    public static class ParticipantCreatedData {

        public String confirmationCode;
        public Long participantId;
    }
}
