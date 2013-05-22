package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.user.User;
import fi.om.municipalityinitiative.service.AccessDeniedException;

import java.util.Collections;

public class FakeAdminUserDao implements AdminUserDao {

    private static final String FAKE_ADMIN_USERNAME = "admin";
    private static final String FAKE_ADMIN_PASSWORD = "admin";

    private static final String FAKE_USER_USERNAME = "user";
    private static final String FAKE_USER_PASSWORD = "user";

    @Override
    public User getUser(String userName, String password) {
        if (userName.equals(FAKE_ADMIN_USERNAME) && password.equals(FAKE_ADMIN_PASSWORD)) {
            return User.omUser("");
        }
        else if (userName.equals(FAKE_USER_USERNAME) && password.equals(FAKE_USER_PASSWORD)) {
            return User.normalUser(-5L, Collections.<Long>emptySet());
        }
        throw new AccessDeniedException("Invalid login information");
    }
}
