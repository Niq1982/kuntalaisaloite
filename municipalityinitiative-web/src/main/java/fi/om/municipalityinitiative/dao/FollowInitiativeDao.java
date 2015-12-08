package fi.om.municipalityinitiative.dao;

import java.util.Map;

public interface FollowInitiativeDao {

    void addFollow(Long initiativeId, String email, String hash);

    void removeFollow(String hash);

    Map<String, String> listFollowers(Long initiativeId);
}
