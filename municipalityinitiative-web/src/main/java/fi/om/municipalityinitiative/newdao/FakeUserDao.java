package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.service.User;
import fi.om.municipalityinitiative.service.AccessDeniedException;

public class FakeUserDao implements UserDao {

    private static String FAKE_USERNAME = "admin";
    private static String FAKE_PASSWORD = "admin";

    @Override
    public User getUser(String userName, String password) {
        if (userName.equals(FAKE_USERNAME) && password.equals(FAKE_PASSWORD)) {
            return User.omUser();
        }
        throw new AccessDeniedException("Invalid login information");
    }
}
