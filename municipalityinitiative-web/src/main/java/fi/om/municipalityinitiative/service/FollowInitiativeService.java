package fi.om.municipalityinitiative.service;


import com.mysema.query.QueryException;
import fi.om.municipalityinitiative.dao.FollowInitiativeDao;
import fi.om.municipalityinitiative.service.email.EmailService;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

public class FollowInitiativeService {

    @Resource
    private FollowInitiativeDao followInitiativeDao;

    @Resource
    private EmailService emailService;

    @Transactional(readOnly = false)
    public void followInitiative(Long initiativeId, String email ) {

        String hash = RandomHashGenerator.longHash();
        try {
            followInitiativeDao.addFollow(initiativeId, email, hash);
        }
        catch (QueryException e) {
            e.printStackTrace();
        }
        emailService.sendConfirmToFollower(initiativeId, email, hash);

    }

    @Transactional(readOnly = false)
    public void stopFollowingInitiative(String hash) {
        followInitiativeDao.removeFollow(hash);
    }

    @Transactional(readOnly = true)
    public Map<String, String> listFollowersForInitiative(Long initiativeId) {
        return followInitiativeDao.listFollowers(initiativeId);
    }
}
