package fi.om.municipalityinitiative.service;

import com.google.common.base.Optional;
import fi.om.municipalityinitiative.exceptions.NotCollectableException;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.service.InitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.newdto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.newdto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.util.ParticipatingUnallowedException;
import fi.om.municipalityinitiative.util.ReflectionUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;


public class InitiativeService {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    private ParticipantDao participantDao;

    public List<InitiativeListInfo> findMunicipalityInitiatives(InitiativeSearch search) {
        return initiativeDao.findNewestFirst(search);
    }

    @Transactional(readOnly = false)
    public Long createMunicipalityInitiative(InitiativeUICreateDto createDto, boolean isCollectable) {

        InitiativeCreateDto initiativeCreateDto = parse(createDto);
        if (isCollectable) {
            initiativeCreateDto.managementHash = Optional.of("0000000000111111111122222222223333333333");
        }
        else {
            initiativeCreateDto.managementHash = Optional.absent();
        }

        Long municipalityInitiativeId = initiativeDao.create(initiativeCreateDto);
        Long participantId = participantDao.create(parse(createDto, municipalityInitiativeId));
        initiativeDao.assignAuthor(municipalityInitiativeId, participantId);

        return municipalityInitiativeId;
    }

    @Transactional(readOnly = false)
    public void sendToMunicipality(Long initiativeId, String hashCode) {

        InitiativeViewInfo initiativeInfo = initiativeDao.getById(initiativeId);

        checkAllowedToSendToMunicipality(initiativeInfo);
        checkHashCode(hashCode, initiativeInfo);

        // TODO: Send the email.
        initiativeDao.markAsSended(initiativeId);

    }

    private void checkAllowedToSendToMunicipality(InitiativeViewInfo initiativeViewInfo) {
        if (!initiativeViewInfo.isCollectable()) {
            throw new NotCollectableException("Initiative is not collectable");
        }
        if (initiativeViewInfo.getSentTime().isPresent()) {
            throw new NotCollectableException("Initiative already sent");
        }
    }

    private void checkHashCode(String hashCode, InitiativeViewInfo initiativeInfo) {
        if (!initiativeInfo.getManagementHash().equals(hashCode)) {
            throw new AccessDeniedException("Invalid initiative verifier");
        }
    }

    @Transactional(readOnly = false)
    public Long createParticipant(ParticipantUICreateDto participant, Long initiativeId) {

        checkAllowedToParticipate(initiativeId);

        ParticipantCreateDto participantCreateDto = new ParticipantCreateDto();
        ReflectionUtils.copyFieldValuesToChild(participant, participantCreateDto);
        participantCreateDto.setMunicipalityInitiativeId(initiativeId);
        return participantDao.create(participantCreateDto);
    }

    private void checkAllowedToParticipate(Long initiativeId) {
        InitiativeViewInfo initiative = initiativeDao.getById(initiativeId);

        if (!initiative.isCollectable()) {
            throw new ParticipatingUnallowedException("Initiative not collectable: " + initiativeId);
        }
        else if (initiative.getSentTime().isPresent()) {
            throw new ParticipatingUnallowedException("Initiative already sent: " + initiativeId);
        }

    }

    static InitiativeCreateDto parse(InitiativeUICreateDto source) {

        InitiativeCreateDto initiativeCreateDto = new InitiativeCreateDto();

        initiativeCreateDto.name = source.getName();
        initiativeCreateDto.proposal = source.getProposal();
        initiativeCreateDto.municipalityId = source.getMunicipality();
        initiativeCreateDto.contactName = source.getContactInfo().getName();
        initiativeCreateDto.contactPhone= source.getContactInfo().getPhone();
        initiativeCreateDto.contactAddress = source.getContactInfo().getAddress();
        initiativeCreateDto.contactEmail = source.getContactInfo().getEmail();

        return initiativeCreateDto;
    }

    static ParticipantCreateDto parse(InitiativeUICreateDto source, Long municipalityInitiativeId) {
        ParticipantCreateDto participantCreateDto = new ParticipantCreateDto();

        participantCreateDto.setMunicipalityInitiativeId(municipalityInitiativeId); // TODO: Fix null possibilities after valdiations are complete
        participantCreateDto.setFranchise(source.getFranchise() == null ? false : source.getFranchise());

        participantCreateDto.setShowName(source.getShowName() == null ? false : source.getShowName());
        participantCreateDto.setParticipantName(source.getContactInfo().getName());
        participantCreateDto.setHomeMunicipality(source.getHomeMunicipality());
        return participantCreateDto;
    }

    public InitiativeViewInfo getMunicipalityInitiative(Long initiativeId) {
        return initiativeDao.getById(initiativeId);
    }
}
