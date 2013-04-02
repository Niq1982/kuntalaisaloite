package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.FakeUserDao;
import fi.om.municipalityinitiative.newdao.UserDao;
import fi.om.municipalityinitiative.newdto.service.User;
import fi.om.municipalityinitiative.util.Maybe;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserService {

    static final String LOGIN_USER_PARAMETER = "loginUser";

    UserDao userDao = new FakeUserDao();

    public void login(String userName, String password, HttpServletRequest request) {
        request.getSession().setAttribute(LOGIN_USER_PARAMETER, userDao.getUser(userName, password));
    }

    public void requireOmUser() {
        Maybe<User> maybeUser = getOptionalUser();
        if (!maybeUser.isPresent()) {
            throw new AuthenticationRequiredException();
        }
        if (!maybeUser.get().isOmUser()) {
            throw new AccessDeniedException("No privileges");
        }
    }

    public Maybe<User> getOptionalUser() {
        Object user = getCurrentRequest().getSession().getAttribute(LOGIN_USER_PARAMETER);
        return Maybe.fromNullable((User) user);
    }


    // TODO: Session may ne null?

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        getCurrentRequest().getSession().invalidate();
    }

    private static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }
}
