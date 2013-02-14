package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.service.PublicParticipant;
import fi.om.municipalityinitiative.newdto.ui.ParticipantCount;
import fi.om.municipalityinitiative.newdto.ui.ParticipantNames;

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

    public ParticipantNames findPublicParticipants(Long initiativeId) {
        return toParticipantNames(participantDao.findPublicParticipants(initiativeId));
    }

    public static <E extends PublicParticipant> ParticipantNames toParticipantNames(List<E> publicPublicParticipants) {
        ParticipantNames participantNames = new ParticipantNames();

        for (PublicParticipant publicParticipant : publicPublicParticipants) {
            if (publicParticipant.isFranchise()) {
                participantNames.getFranchise().add(publicParticipant.getName());
            }
            else {
                participantNames.getNoFranchise().add(publicParticipant.getName());
            }
        }

        return participantNames;
    }
}
