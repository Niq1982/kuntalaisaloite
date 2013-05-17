package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.user.LoginUser;
import fi.om.municipalityinitiative.service.AccessDeniedException;

import java.util.Collections;

public class FakeUserDao implements UserDao {

    private static final String FAKE_ADMIN_USERNAME = "admin";
    private static final String FAKE_ADMIN_PASSWORD = "admin";

    private static final String FAKE_USER_USERNAME = "user";
    private static final String FAKE_USER_PASSWORD = "user";

    @Override
    public LoginUser getUser(String userName, String password) {
        if (userName.equals(FAKE_ADMIN_USERNAME) && password.equals(FAKE_ADMIN_PASSWORD)) {
            return LoginUser.omUser();
        }
        else if (userName.equals(FAKE_USER_USERNAME) && password.equals(FAKE_USER_PASSWORD)) {
            return LoginUser.normalUser(-5L, Collections.<Long>emptySet());
        }
        throw new AccessDeniedException("Invalid login information");
    }
}
