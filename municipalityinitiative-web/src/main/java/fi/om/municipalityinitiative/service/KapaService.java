package fi.om.municipalityinitiative.service;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.UserDao;
import fi.om.municipalityinitiative.dto.json.InitiativeListJson;
import fi.om.municipalityinitiative.dto.service.VerifiedUserDbDetails;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class KapaService {

    @Resource
    private EncryptionService encryptionService;

    @Resource
    private InitiativeDao initiativeDao;

    @Resource
    private UserDao userDao;


    @Transactional(readOnly = true)
    public KapaInitiativeResult findInitiativesForUser(String ssn) {

        Optional<VerifiedUserDbDetails> verifiedUser = userDao.getVerifiedUser(encryptionService.registeredUserHash(ssn));

        if (verifiedUser.isPresent()) {

            KapaInitiativeResult kapaInitiativeResult = new KapaInitiativeResult();
            VerifiedUserId authorId = verifiedUser.get().getVerifiedUserId();

            // Get all users initiatives
            kapaInitiativeResult.initiatives = initiativeDao.findInitiatives(authorId, false).stream().map(InitiativeListJson::new).collect(Collectors.toList());

            // Get IDS so we can parse them out from supported initiatives
            Set<Long> myInitiativeIds = kapaInitiativeResult.initiatives.stream().map(InitiativeListJson::getId).collect(Collectors.toSet());

            kapaInitiativeResult.supports = initiativeDao.findInitiativesByParticipation(authorId)
                    .stream()
                    .filter(i -> !myInitiativeIds.contains(i.getId()))
                    .map(InitiativeListJson::new).collect(Collectors.toList());

            // After all, filter out complete drafts from own initiatives
            kapaInitiativeResult.initiatives = kapaInitiativeResult.initiatives.stream()
                    .filter(i -> !Strings.isNullOrEmpty(i.getName()))
                    .collect(Collectors.toList());

            return kapaInitiativeResult;
        }
        return new KapaInitiativeResult();
    }

    public static class KapaInitiativeResult {
        public List<InitiativeListJson> supports = Lists.newArrayList();
        public List<InitiativeListJson> initiatives = Lists.newArrayList();

    }
}
