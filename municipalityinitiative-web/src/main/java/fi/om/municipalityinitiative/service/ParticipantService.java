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

    public ParticipantService(ParticipantDao participantDao) {
        this.participantDao = participantDao;
    }

    public ParticipantCount getParticipantCount(Long initiativeId) {
        return participantDao.getParticipantCount(initiativeId);
    }

    public Participants findPublicParticipants(Long initiativeId) {
        return toParticipantNames(participantDao.findPublicParticipants(initiativeId));
    }

    public static <E extends Participant> Participants toParticipantNames(List<E> publicPublicParticipants) {
        Participants participants = new Participants();

        for (Participant participant : publicPublicParticipants) {
            if (participant.isFranchise()) {
                participants.getFranchise().add(participant);
            }
            else {
                participants.getNoFranchise().add(participant);
            }
        }

        return participants;
    }
}
