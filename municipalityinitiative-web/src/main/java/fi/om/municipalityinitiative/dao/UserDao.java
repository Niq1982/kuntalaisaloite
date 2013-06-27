package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.service.VerifiedUser;

public interface UserDao {

    User getAdminUser(String userName, String password);

    VerifiedUser getVerifiedUser(String hash);

    Long addVerifiedUser(String hash, ContactInfo contactInfo);

}
