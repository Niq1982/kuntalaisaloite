package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.json.InitiativeJson;
import fi.om.municipalityinitiative.newdto.ui.InitiativeListInfo;

import javax.annotation.Resource;

import java.util.List;

public class JsonDataService {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    ParticipantDao participantDao;

    public List<InitiativeListInfo> findJsonInitiatives(InitiativeSearch search) {
        return initiativeDao.find(search); // TODO: Implement own json-dto
    }

    public InitiativeJson getInitiative(Long id) {
        return InitiativeJson.from(
                initiativeDao.getById(id),
                participantDao.findPublicParticipants(id),
                participantDao.getParticipantCount(id));

    }
}
