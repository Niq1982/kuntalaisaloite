package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.exceptions.NotLoggedInException;
import fi.om.municipalityinitiative.newdao.FakeUserDao;
import fi.om.municipalityinitiative.newdao.UserDao;
import fi.om.municipalityinitiative.newdto.service.User;

import javax.servlet.http.HttpServletRequest;

public class UserService {

    static final String LOGIN_USER_PARAMETER = "loginUser";

    UserDao userDao = new FakeUserDao();

    public void login(String userName, String password, HttpServletRequest request) {
        request.getSession().setAttribute(LOGIN_USER_PARAMETER, userDao.getUser(userName, password));
    }

    public void requireOmUser(HttpServletRequest request) {
        if (!getCurrentUser(request).isOmUser()) {
            throw new AccessDeniedException("No privileges");
        }
    }

    private static User getCurrentUser(HttpServletRequest request) {

        if (request.getSession().getAttribute(LOGIN_USER_PARAMETER) == null) {
            throw new AuthenticationRequiredException();
        }

        return (User) request.getSession().getAttribute(LOGIN_USER_PARAMETER);

    }
}
