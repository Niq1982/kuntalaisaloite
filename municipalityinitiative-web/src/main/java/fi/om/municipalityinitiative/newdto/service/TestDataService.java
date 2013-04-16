package fi.om.municipalityinitiative.newdto.service;

import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
import fi.om.municipalityinitiative.newdto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.util.Maybe;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class TestDataService {
    
    @Resource
    InitiativeDao initiativeDao;

    @Resource
    private ParticipantDao participantDao;

    @Resource
    MunicipalityDao municipalityDao;


    @Transactional(readOnly = false)
    public Long createTestMunicipalityInitiative(InitiativeUICreateDto createDto) {
        throw new RuntimeException("New implementation needs rewriting this function");
    
//        Maybe<String> managementHash;
//        if (createDto.isCollectable()) {
//            managementHash = Maybe.of("0000000000111111111122222222223333333333");
//        }
//        else {
//            managementHash = Maybe.absent();
//        }
//
//        InitiativeCreateDto initiativeCreateDto = InitiativeCreateDto.parse(createDto, managementHash);
//
//        Long initiativeId = initiativeDao.create(initiativeCreateDto);
//        Long participantId = participantDao.create(ParticipantCreateDto.parse(createDto, initiativeId));
//        initiativeDao.assignAuthor(initiativeId, participantId, createDto.getContactInfo().getEmail(), managementHash.get());
//        return initiativeId;

    }
    
    @Transactional(readOnly = false)
    public void createTestParticipant(Long initiativeId, ParticipantUICreateDto createDto, int amount) {
        for (int i = 0; i < amount; ++i) {
            participantDao.create(ParticipantCreateDto.parse(createDto, initiativeId), null);
        }
    }
    
}