package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.ParticipantCount;

import javax.annotation.Resource;

public class ParticipantService {

    @Resource
    private ParticipantDao participantDao;

    public ParticipantCount getParticipantCount(Long initiativeId) {
        return participantDao.getParticipantCount(initiativeId);
    }
}
