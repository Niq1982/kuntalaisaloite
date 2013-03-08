package fi.om.municipalityinitiative.newdto.service;

import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
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
//    public Long createTestMunicipalityInitiative(InitiativeUICreateDto createDto, Locale locale) {
    public void createTestMunicipalityInitiative(List<InitiativeUICreateDto> initiatives, Locale locale) {
        if (initiatives.size() == 0) {
              return; // nothing to create
        }
//
//        Maybe<String> managementHash;
//        if (createDto.isCollectable()) {
//            managementHash = Maybe.of(RandomHashGenerator.randomString(40));
//        }
//        else {
//            managementHash = Maybe.absent();
//        }
//
//        InitiativeCreateDto initiativeCreateDto = InitiativeCreateDto.parse(createDto, managementHash);
//
//        Long initiativeId = initiativeDao.create(initiativeCreateDto);
//        Long participantId = participantDao.create(ParticipantCreateDto.parse(createDto, initiativeId));
//        initiativeDao.assignAuthor(initiativeId, participantId);
        
//        for (InitiativeUICreateDto initiative : initiatives) {
//            createTestInitiativeFromTemplate(initiative);
//        }

//        return initiativeId;
    }
    
//    private Long createTestInitiativeFromTemplate(InitiativeUICreateDto initiative) {
//        initiative.assignEndDate(initiative.getStartDate(), initiativeSettings.getVotingDuration());
//        Long id = initiativeDao.create(initiative);
//
//        InitiativeManagement createdInitiative = initiativeDao.get(id);

//        queryFactory.update(QInitiative.initiative)
//          .set(QInitiative.initiative.supportcount, initiative.getSupportCount())
//          .where(QInitiative.initiative.id.eq(createdInitiative.getId()))
//          .execute();

//        return id;
//    }
    
}