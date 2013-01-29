package fi.om.municipalityinitiative.service;

import com.google.common.base.Optional;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.*;
import fi.om.municipalityinitiative.util.ParticipatingUnallowedException;
import fi.om.municipalityinitiative.util.ReflectionUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;


public class InitiativeService {

    @Resource
    private InitiativeDao initiativeDao;

    @Resource
    private ParticipantDao participantDao;

    public List<InitiativeListInfo> findMunicipalityInitiatives(InitiativeSearch search) {
        return initiativeDao.findNewestFirst(search);
    }

    @Transactional(readOnly = false)
    public Long createMunicipalityInitiative(InitiativeUICreateDto createDto, boolean isCollectable) {
        // TODO: Validate ?

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

    @Transactional
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
        initiativeCreateDto.contactName = source.getContactName();
        initiativeCreateDto.contactPhone= source.getContactPhone();
        initiativeCreateDto.contactAddress = source.getContactAddress();
        initiativeCreateDto.contactEmail = source.getContactEmail();

        return initiativeCreateDto;
    }

    static ParticipantCreateDto parse(InitiativeUICreateDto source, Long municipalityInitiativeId) {
        ParticipantCreateDto participantCreateDto = new ParticipantCreateDto();

        participantCreateDto.setMunicipalityInitiativeId(municipalityInitiativeId); // TODO: Fix null possibilities after valdiations are complete
        participantCreateDto.setFranchise(source.getFranchise() == null ? false : source.getFranchise());

        participantCreateDto.setShowName(source.getShowName() == null ? false : source.getShowName());
        participantCreateDto.setParticipantName(source.getContactName());
        participantCreateDto.setHomeMunicipality(source.getHomeMunicipality());
        return participantCreateDto;
    }

    public InitiativeViewInfo getMunicipalityInitiative(Long initiativeId) {
        return initiativeDao.getById(initiativeId);
    }
}
