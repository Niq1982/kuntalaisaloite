package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.ui.ParticipantCount;
import fi.om.municipalityinitiative.newdto.ui.ParticipantNames;

import javax.annotation.Resource;

public class ParticipantService {

    @Resource
    private ParticipantDao participantDao;

    public ParticipantService() {
    }

    public ParticipantService(ParticipantDao participantDao) {
        this.participantDao = participantDao;
    }

    public ParticipantCount getParticipantCount(Long initiativeId) {
        return participantDao.getParticipantCount(initiativeId);
    }

    public ParticipantNames findParticipants(Long initiativeId) {
        ParticipantNames participantNames = new ParticipantNames();

        for (Participant participant : participantDao.findPublicParticipants(initiativeId)) {
            if (participant.isFranchise()) {
                participantNames.getFranchise().add(participant.getName());
            }
            else {
                participantNames.getNoFranchise().add(participant.getName());
            }
        }

        return participantNames;
    }
}
