package fi.om.municipalityinitiative.service;


import com.mysema.query.QueryException;
import fi.om.municipalityinitiative.dao.FollowInitiativeDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.service.email.EmailService;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.hash.RandomHashGenerator;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Locale;
import java.util.Map;

public class FollowInitiativeService {

    @Resource
    private FollowInitiativeDao followInitiativeDao;

    @Resource
    private EmailService emailService;

    @Resource
    InitiativeDao initiativeDao;

    @Transactional(readOnly = false)
    public void followInitiative(Long initiativeId, String email, Locale locale) {
        Initiative initiative = initiativeDao.get(initiativeId);
        if (initiative.getDecisionDate().isPresent() || !initiative.getState().equals(InitiativeState.PUBLISHED)) {
            throw new AccessDeniedException("Can't follow initiative.");
        }
        String hash = RandomHashGenerator.longHash();
        try {
            followInitiativeDao.addFollow(initiativeId, email, hash);
            emailService.sendConfirmToFollower(initiativeId, email, hash, locale);
        }
        catch (QueryException e) {
            e.printStackTrace();
        }

    }

    @Transactional(readOnly = false)
    public long stopFollowingInitiative(String hash) {
        return followInitiativeDao.removeFollow(hash);
    }

    @Transactional(readOnly = true)
    public Map<String, String> listFollowersForInitiative(Long initiativeId) {
        return followInitiativeDao.listFollowers(initiativeId);
    }
}
