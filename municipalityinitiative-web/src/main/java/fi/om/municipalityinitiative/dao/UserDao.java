package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.Maybe;

public interface UserDao {

    User getAdminUser(String userName, String password);

    Maybe<VerifiedUser> getVerifiedUser(String hash);

    VerifiedUserId addVerifiedUser(String hash, ContactInfo contactInfo);

    Maybe<VerifiedUserId> getVerifiedUserId(String hash);

    void updateUserInformation(String hash, ContactInfo contactInfo);
}
