package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.service.VerifiedUserDbDetails;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;

import java.util.Optional;

public interface UserDao {

    User getAdminUser(String userName, String password);

    Optional<VerifiedUserDbDetails> getVerifiedUser(String hash);

    VerifiedUserId addVerifiedUser(String hash, ContactInfo contactInfo, Optional<Municipality> homeMunicipality);

    Optional<VerifiedUserId> getVerifiedUserId(String hash);

    void updateUserInformation(String hash, ContactInfo contactInfo);

    void updateUserInformation(String hash, String fullName, Optional<Municipality> vetumaMunicipality);
}
