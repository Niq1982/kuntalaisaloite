package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.ParticipantDao;
import fi.om.municipalityinitiative.dto.NormalAuthor;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.service.Participant;
import fi.om.municipalityinitiative.dto.ui.ParticipantCount;
import fi.om.municipalityinitiative.dto.ui.ParticipantListInfo;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ParticipantService {

    @Resource
    private ParticipantDao participantDao;

    @Resource
    private AuthorDao authorDao;

    @Resource
    private InitiativeDao initiativeDao;

    public ParticipantService() {
    }

    @Transactional(readOnly = true)
    public ParticipantCount getParticipantCount(Long initiativeId) {
        return participantDao.getParticipantCount(initiativeId);
    }

    @Transactional(readOnly = true)
    public List<ParticipantListInfo> findPublicParticipants(Long initiativeId) {

        List<Participant> participants = initiativeDao.isVerifiableInitiative(initiativeId)
                ? participantDao.findVerifiedPublicParticipants(initiativeId)
                : participantDao.findNormalPublicParticipants(initiativeId);
        return toListInfo(participants, getAuthorIds(initiativeId));
    }

    @Transactional(readOnly = true)
    public List<ParticipantListInfo> findAllParticipants(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        List<Participant> allParticipants = initiativeDao.isVerifiableInitiative(initiativeId)
                ? participantDao.findVerifiedAllParticipants(initiativeId)
                : participantDao.findNormalAllParticipants(initiativeId);
        return toListInfo(allParticipants, getAuthorIds(initiativeId));
    }

    private Set<NormalAuthorId> getAuthorIds(Long initiativeId) {
        Set<NormalAuthorId> authorIds = Sets.newHashSet();
        for (NormalAuthor author : authorDao.findNormalAuthors(initiativeId)) {
            authorIds.add(author.getId());
        }
        return authorIds;
    }

    static List<ParticipantListInfo> toListInfo(List<Participant> participants, Set<NormalAuthorId> authorIds) {
        ArrayList<ParticipantListInfo> participantList = Lists.newArrayList();
        for (Participant participant : participants) {
            participantList.add(new ParticipantListInfo(participant, authorIds.contains(new NormalAuthorId(participant.getId()))));
        }
        return participantList;
    }

    @Transactional(readOnly = false)
    public void deleteParticipant(Long initiativeId, LoginUserHolder loginUserHolder, Long participantId) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        participantDao.deleteParticipant(initiativeId, participantId);
    }

}
