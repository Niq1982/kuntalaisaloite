package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.ComposerDao;
import fi.om.municipalityinitiative.newdao.MunicipalityInitiativeDao;
import fi.om.municipalityinitiative.newdto.ComposerCreateDto;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeInfo;
import fi.om.municipalityinitiative.newweb.MunicipalityInitiativeSearch;
import fi.om.municipalityinitiative.newweb.MunicipalityInitiativeUICreateDto;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

public class MunicipalityInitiativeService {

    @Resource
    private MunicipalityInitiativeDao municipalityInitiativeDao;

    @Resource
    private ComposerDao composerDao;

    public List<MunicipalityInitiativeInfo> findMunicipalityInitiatives(MunicipalityInitiativeSearch search) {
        return municipalityInitiativeDao.findNewestFirst(search);
    }

    @Transactional(readOnly = false)
    public void addMunicipalityInitiative(MunicipalityInitiativeUICreateDto createDto) {
        // TODO: Validate ?

        Long municipalityInitiativeId = municipalityInitiativeDao.create(parse(createDto));
        composerDao.add(parse(createDto, municipalityInitiativeId));
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

    static ComposerCreateDto parse(MunicipalityInitiativeUICreateDto source, Long municipalityInitiativeId) {
        ComposerCreateDto composerCreateDto = new ComposerCreateDto();

        composerCreateDto.municipalityInitiativeId = municipalityInitiativeId; // TODO: Fix null possibilities after valdiations are complete
        composerCreateDto.right_of_voting = source.getFranchise() == null ? false : source.getFranchise();

        composerCreateDto.showName = source.getShowName() == null ? false : source.getShowName();
        composerCreateDto.name = source.getContactName();
        composerCreateDto.municipalityId = source.getHomeMunicipality();
        return composerCreateDto;
    }

    public MunicipalityInitiativeInfo getMunicipalityInitiative(Long initiativeId) {
        return municipalityInitiativeDao.getById(initiativeId);
    }
}
