package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.FakeUserDao;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.UserDao;
import fi.om.municipalityinitiative.newdto.LoginUserHolder;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.User;
import fi.om.municipalityinitiative.util.Maybe;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserService {

    static final String LOGIN_USER_PARAMETER = "loginUser";
    static final String LOGIN_INITIATIVE_PARAMETER = "loginInitiative";

    UserDao userDao = new FakeUserDao();

    @Resource
    InitiativeDao initiativeDao;

    public void login(String userName, String password, HttpServletRequest request) {
        request.getSession().setAttribute(LOGIN_USER_PARAMETER, userDao.getUser(userName, password));
    }

    public void login(Long initiativeId, String managementHash, HttpServletRequest request) {
        request.getSession().setAttribute(LOGIN_INITIATIVE_PARAMETER, initiativeDao.getById(initiativeId, managementHash));
        request.getSession().setAttribute(LOGIN_USER_PARAMETER, User.normalUser());
    }

    public LoginUserHolder getRequiredLoginUserHolder(HttpServletRequest request) {
        if (request.getSession() == null) {
            throw new AccessDeniedException("Not logged in as author");
        }
        return parseLoginUser(request.getSession());
    }

    private static LoginUserHolder parseLoginUser(HttpSession session) {
        return new LoginUserHolder(
                (User) session.getAttribute(LOGIN_USER_PARAMETER),
                Maybe.fromNullable((Initiative) session.getAttribute(LOGIN_INITIATIVE_PARAMETER)
        ));
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

    public static Maybe<User> getUser() {
        String loginUserParameter = LOGIN_USER_PARAMETER;
        return getObject(loginUserParameter);
    }

    @SuppressWarnings("unchecked")
    private static <T> Maybe<T> getObject(String sessionAttributeName) {
        Maybe<HttpSession> session = getSession();
        if (session.isPresent()) {
            Object sessionObject = session.get().getAttribute(sessionAttributeName);
            return Maybe.fromNullable((T) sessionObject);
        }
        return Maybe.absent();
    }

    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
    }

    private static Maybe<HttpSession> getSession() {
        // TODO: Throw exception if no session and catch it at errorfilter
        // XXX: Actually remove whole static usage if possible
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (request != null) {
            return Maybe.fromNullable(request.getSession());
        }
        return Maybe.absent();

    }

    @Deprecated
    public String getManagementHash() {
        return ((Initiative) getSession().get().getAttribute(LOGIN_INITIATIVE_PARAMETER)).getManagementHash().get();
    }

    public boolean isOmUser() {
        Maybe<User> user = getUser();
        return user.isPresent() && user.get().isOmUser();
    }
}
