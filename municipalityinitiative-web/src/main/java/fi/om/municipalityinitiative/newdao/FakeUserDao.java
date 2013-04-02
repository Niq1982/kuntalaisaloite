package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.service.User;
import fi.om.municipalityinitiative.service.AccessDeniedException;

public class FakeUserDao implements UserDao {

    private static String FAKE_ADMIN_USERNAME = "admin";
    private static String FAKE_ADMIN_PASSWORD = "admin";

    private static String FAKE_USER_USERNAME = "user";
    private static String FAKE_USER_PASSWORD = "user";



    @Override
    public User getUser(String userName, String password) {
        if (userName.equals(FAKE_ADMIN_USERNAME) && password.equals(FAKE_ADMIN_PASSWORD)) {
            return User.omUser();
        }
        else if (userName.equals(FAKE_USER_USERNAME) && password.equals(FAKE_USER_PASSWORD)) {
            return User.normalUser();
        }
        throw new AccessDeniedException("Invalid login information");
    }
}
