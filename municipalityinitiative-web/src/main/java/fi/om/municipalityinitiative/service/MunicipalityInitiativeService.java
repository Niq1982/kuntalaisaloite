package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.MunicipalityInitiativeDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeInfo;
import fi.om.municipalityinitiative.newdto.ParticipantCreateDto;
import fi.om.municipalityinitiative.newweb.MunicipalityInitiativeSearch;
import fi.om.municipalityinitiative.newweb.MunicipalityInitiativeUICreateDto;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

public class MunicipalityInitiativeService {

    @Resource
    private MunicipalityInitiativeDao municipalityInitiativeDao;

    @Resource
    private ParticipantDao participantDao;

    public List<MunicipalityInitiativeInfo> findMunicipalityInitiatives(MunicipalityInitiativeSearch search) {
        return municipalityInitiativeDao.findNewestFirst(search);
    }

    @Transactional(readOnly = false)
    public Long addMunicipalityInitiative(MunicipalityInitiativeUICreateDto createDto) {
        // TODO: Validate ?

        Long municipalityInitiativeId = municipalityInitiativeDao.create(parse(createDto));
        Long participantId = participantDao.add(parse(createDto, municipalityInitiativeId));
        municipalityInitiativeDao.assignAuthor(municipalityInitiativeId, participantId);

        return municipalityInitiativeId;
    }

    static MunicipalityInitiativeCreateDto parse(MunicipalityInitiativeUICreateDto source) {

        MunicipalityInitiativeCreateDto municipalityInitiativeCreateDto = new MunicipalityInitiativeCreateDto();

        municipalityInitiativeCreateDto.name = source.getName();
        municipalityInitiativeCreateDto.proposal = source.getProposal();
        municipalityInitiativeCreateDto.municipalityId = source.getMunicipality();
        municipalityInitiativeCreateDto.contactName = source.getContactName();
        municipalityInitiativeCreateDto.contactPhone= source.getContactPhone();
        municipalityInitiativeCreateDto.contactAddress = source.getContactAddress();
        municipalityInitiativeCreateDto.contactEmail = source.getContactEmail();

        return municipalityInitiativeCreateDto;
    }

    static ParticipantCreateDto parse(MunicipalityInitiativeUICreateDto source, Long municipalityInitiativeId) {
        ParticipantCreateDto participantCreateDto = new ParticipantCreateDto();

        participantCreateDto.municipalityInitiativeId = municipalityInitiativeId; // TODO: Fix null possibilities after valdiations are complete
        participantCreateDto.franchise = source.getFranchise() == null ? false : source.getFranchise();

        participantCreateDto.showName = source.getShowName() == null ? false : source.getShowName();
        participantCreateDto.name = source.getContactName();
        participantCreateDto.municipalityId = source.getHomeMunicipality();
        return participantCreateDto;
    }

    public MunicipalityInitiativeInfo getMunicipalityInitiative(Long initiativeId) {
        return municipalityInitiativeDao.getById(initiativeId);
    }
}
