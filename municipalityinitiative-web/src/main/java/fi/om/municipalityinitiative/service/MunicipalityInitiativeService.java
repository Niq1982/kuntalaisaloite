package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.ComposerDao;
import fi.om.municipalityinitiative.newdao.MunicipalityInitiativeDao;
import fi.om.municipalityinitiative.newdto.ComposerCreateDto;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeInfo;
import fi.om.municipalityinitiative.newweb.MunicipalityInitiativeUICreateDto;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

public class MunicipalityInitiativeService {

    @Resource
    private MunicipalityInitiativeDao municipalityInitiativeDao;

    @Resource
    private ComposerDao composerDao;

    public List<MunicipalityInitiativeInfo> findAllMunicipalityInitiatives() {
        return municipalityInitiativeDao.findAllNewestFirst();
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
        municipalityInitiativeCreateDto.contactName = null; // TODO: Contact info
        municipalityInitiativeCreateDto.contactPhone= null;
        municipalityInitiativeCreateDto.contactAddress = null;
        municipalityInitiativeCreateDto.contactEmail = null;

        return municipalityInitiativeCreateDto;
    }

    static ComposerCreateDto parse(MunicipalityInitiativeUICreateDto source, Long municipalityInitiativeId) {
        ComposerCreateDto composerCreateDto = new ComposerCreateDto();

        composerCreateDto.municipalityInitiativeId = municipalityInitiativeId;
        composerCreateDto.right_of_voting = source.isFranchise();

        composerCreateDto.showName = false; // TODO: Contact info
        composerCreateDto.name = null;
        composerCreateDto.municipalityId = source.getHomeMunicipality();
        return composerCreateDto;
    }

}
