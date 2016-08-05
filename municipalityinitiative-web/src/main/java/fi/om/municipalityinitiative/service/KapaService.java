package fi.om.municipalityinitiative.service;


import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.UserDao;
import fi.om.municipalityinitiative.dto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.Maybe;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

public class KapaService {

    @Resource
    private EncryptionService encryptionService;

    @Resource
    private InitiativeDao initiativeDao;

    @Resource
    private UserDao userDao;


    @Transactional(readOnly = true)
    public List<InitiativeListInfo> findInitiativesForUser(String ssn) {

        Maybe<VerifiedUser> verifiedUser = userDao.getVerifiedUser(encryptionService.registeredUserHash(ssn));

        if (verifiedUser.isPresent()) {
            VerifiedUserId authorId = verifiedUser.get().getAuthorId();
            return initiativeDao.findInitiatives(authorId);
        }
        return Lists.newArrayList();
    }
}
