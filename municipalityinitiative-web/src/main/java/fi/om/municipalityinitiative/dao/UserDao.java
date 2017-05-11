package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.service.VerifiedUserDbDetails;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.Maybe;

public interface UserDao {

    User getAdminUser(String userName, String password);

    Maybe<VerifiedUserDbDetails> getVerifiedUser(String hash);

    VerifiedUserId addVerifiedUser(String hash, ContactInfo contactInfo, Maybe<Municipality> homeMunicipality);

    Maybe<VerifiedUserId> getVerifiedUserId(String hash);

    void updateUserInformation(String hash, ContactInfo contactInfo);

    void updateUserInformation(String hash, String fullName, Maybe<Municipality> vetumaMunicipality);
}
