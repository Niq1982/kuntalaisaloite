package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.FakeUserDao;
import fi.om.municipalityinitiative.newdao.UserDao;
import fi.om.municipalityinitiative.newdto.service.User;
import fi.om.municipalityinitiative.util.Maybe;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserService {

    static final String LOGIN_USER_PARAMETER = "loginUser";

    UserDao userDao = new FakeUserDao();

    public void login(String userName, String password, HttpServletRequest request) {
        request.getSession().setAttribute(LOGIN_USER_PARAMETER, userDao.getUser(userName, password));
    }

    public void requireOmUser() {
        Maybe<User> maybeUser = getUser();
        if (!maybeUser.isPresent()) {
            throw new AuthenticationRequiredException();
        }
        if (!maybeUser.get().isOmUser()) {
            throw new AccessDeniedException("No privileges");
        }
    }

    public Maybe<User> getUser() {
        Maybe<HttpSession> session = getSession();
        if (session.isPresent()) {
            Object user = session.get().getAttribute(LOGIN_USER_PARAMETER);
            return Maybe.fromNullable((User) user);
        }
        return Maybe.absent();
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Maybe<HttpSession> session = getSession();
        if (session.isPresent()) {
            session.get().invalidate();
        }
    }

    private static Maybe<HttpSession> getSession() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (request != null) {
            return Maybe.fromNullable(request.getSession());
        }
        return Maybe.absent();

    }

}
