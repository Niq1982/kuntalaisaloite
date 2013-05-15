package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.ui.ParticipantCount;
import fi.om.municipalityinitiative.newdto.ui.Participants;

import javax.annotation.Resource;

import java.util.List;

public class ParticipantService {

    @Resource
    private ParticipantDao participantDao;

    public ParticipantService() {
    }

    ParticipantService(ParticipantDao participantDao) {
        this.participantDao = participantDao;
    }

    public ParticipantCount getParticipantCount(Long initiativeId) {
        return participantDao.getParticipantCount(initiativeId);
    }

    public List<Participant> findPublicParticipants(Long initiativeId) {
        return participantDao.findPublicParticipants(initiativeId);
    }

}
