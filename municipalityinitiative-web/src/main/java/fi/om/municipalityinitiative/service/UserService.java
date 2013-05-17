package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.exceptions.NotLoggedInException;
import fi.om.municipalityinitiative.newdao.AuthorDao;
import fi.om.municipalityinitiative.newdao.FakeUserDao;
import fi.om.municipalityinitiative.newdao.UserDao;
import fi.om.municipalityinitiative.newdto.LoginUserHolder;
import fi.om.municipalityinitiative.newdto.service.LoginUser;
import fi.om.municipalityinitiative.util.Maybe;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Set;

public class UserService {

    static final String LOGIN_USER_PARAMETER = "loginUser";
    static final String LOGIN_INITIATIVE_PARAMETER = "loginInitiative";

    UserDao userDao = new FakeUserDao();

    @Resource
    AuthorDao authorDao;

    public void login(String userName, String password, HttpServletRequest request) {
        request.getSession().setAttribute(LOGIN_USER_PARAMETER, userDao.getUser(userName, password));
    }

    public Long login(String managementHash, HttpServletRequest request) {
        Long authorId = authorDao.getAuthorId(managementHash);
        Set<Long> initiativeIds = authorDao.loginAndGetAuthorsInitiatives(managementHash);
        if (initiativeIds.size() == 0) {
            throw new NotLoggedInException("Invalid login credentials");
        }
        request.getSession().setAttribute(LOGIN_USER_PARAMETER, LoginUser.normalUser(authorId, initiativeIds));

        return initiativeIds.iterator().next();
    }

    public LoginUserHolder getRequiredLoginUserHolder(HttpServletRequest request) {
        Maybe<LoginUserHolder> loginUserHolder = parseLoginUser(request.getSession());

        if (loginUserHolder.isNotPresent()) {
            throw new AccessDeniedException("Not logged in as author");
        }

        return loginUserHolder.get();
    }

    public LoginUserHolder getRequiredOmLoginUserHolder(HttpServletRequest request) {

        Maybe<LoginUserHolder> loginUserHolder = parseLoginUser(request.getSession());

        if (loginUserHolder.isNotPresent()) {
            throw new AuthenticationRequiredException();
        }
        loginUserHolder.get().assertOmUser();

        return loginUserHolder.get();
    }

    public boolean hasManagementRightForInitiative(Long initiativeId, HttpServletRequest request) {
        Maybe<LoginUserHolder> loginUserHolderMaybe = parseLoginUser(request.getSession());
        return loginUserHolderMaybe.isPresent()
                && loginUserHolderMaybe.get().hasManagementRightsForInitiative(initiativeId);
    }

    private static Maybe<LoginUserHolder> parseLoginUser(HttpSession session) {

        if (session == null)
            return Maybe.absent();

        LoginUser loginUser = (LoginUser) session.getAttribute(LOGIN_USER_PARAMETER);

        if (loginUser == null)
            return Maybe.absent();


        return Maybe.of(new LoginUserHolder(
                loginUser
        ));
    }

    public static Maybe<LoginUser> getUser() {
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

    public boolean isOmUser(HttpServletRequest request) {
        try {
            LoginUserHolder requiredOmLoginUserHolder = getRequiredOmLoginUserHolder(request);
            return requiredOmLoginUserHolder.getLoginUser().isOmUser();
        }
        catch (AuthenticationRequiredException | AccessDeniedException e) {
            return false;
        }

    }
}
