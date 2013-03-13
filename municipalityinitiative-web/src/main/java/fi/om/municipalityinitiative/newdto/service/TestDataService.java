package fi.om.municipalityinitiative.newdto.service;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
import fi.om.municipalityinitiative.newdto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.service.EmailService;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.RandomHashGenerator;

public class TestDataService {
    
    @Resource
    InitiativeDao initiativeDao;

    @Resource
    private ParticipantDao participantDao;

    @Resource
    MunicipalityDao municipalityDao;


    @Transactional(readOnly = false)
    public Long createTestMunicipalityInitiative(InitiativeUICreateDto createDto) {
    
        Maybe<String> managementHash;
        if (createDto.isCollectable()) {
            managementHash = Maybe.of("0000000000111111111122222222223333333333");
        }
        else {
            managementHash = Maybe.absent();
        }

        InitiativeCreateDto initiativeCreateDto = InitiativeCreateDto.parse(createDto, managementHash);

        Long initiativeId = initiativeDao.create(initiativeCreateDto);
        Long participantId = participantDao.create(ParticipantCreateDto.parse(createDto, initiativeId));
        initiativeDao.assignAuthor(initiativeId, participantId);
        return initiativeId;

    }
    
    @Transactional(readOnly = false)
    public void createTestParticipant(Long initiativeId, ParticipantUICreateDto createDto, int amount) {
        for (int i = 0; i < amount; ++i) {
            participantDao.create(ParticipantCreateDto.parse(createDto, initiativeId));
        }
    }
    
}