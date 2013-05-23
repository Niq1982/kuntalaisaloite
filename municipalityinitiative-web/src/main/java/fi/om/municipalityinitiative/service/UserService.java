package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.exceptions.InvalidLoginException;
import fi.om.municipalityinitiative.newdao.AdminUserDao;
import fi.om.municipalityinitiative.newdao.AuthorDao;
import fi.om.municipalityinitiative.dto.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.util.Maybe;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Set;

public class UserService {

    static final String LOGIN_USER_PARAMETER = "loginUser";

    @Resource
    AdminUserDao adminUserDao;

    @Resource
    AuthorDao authorDao;

    String omUserSalt;

    public UserService(String omUserSalt) {
        this.omUserSalt = omUserSalt;
    }

    public void adminLogin(String userName, String password, HttpServletRequest request) {
        request.getSession().setAttribute(LOGIN_USER_PARAMETER, adminUserDao.getUser(userName, saltAndEncryptPassword(password)));
    }

    private String saltAndEncryptPassword(String password) {
        return EncryptionService.toSha1(omUserSalt + password);
    }

    public Long authorLogin(String managementHash, HttpServletRequest request) {
        // TODO: Merge these to one call
        Long authorId = authorDao.getAuthorId(managementHash);
        Set<Long> initiativeIds = authorDao.loginAndGetAuthorsInitiatives(managementHash);

        if (authorId == null || initiativeIds.size() == 0) {
            throw new InvalidLoginException("Invalid login credentials");
        }

        request.getSession().setAttribute(LOGIN_USER_PARAMETER, User.normalUser(authorId, initiativeIds));

        return initiativeIds.iterator().next();
    }

    public LoginUserHolder getRequiredLoginUserHolder(HttpServletRequest request) {
        Maybe<LoginUserHolder> loginUserHolder = parseLoginUser(request);

        if (loginUserHolder.isNotPresent()) {
            throw new AccessDeniedException("Not logged in as author");
        }

        return loginUserHolder.get();
    }

    public LoginUserHolder getRequiredOmLoginUserHolder(HttpServletRequest request) {

        Maybe<LoginUserHolder> loginUserHolder = parseLoginUser(request);

        if (loginUserHolder.isNotPresent()) {
            throw new AuthenticationRequiredException();
        }
        loginUserHolder.get().assertOmUser();

        return loginUserHolder.get();
    }

    public boolean hasManagementRightForInitiative(Long initiativeId, HttpServletRequest request) {
        Maybe<LoginUserHolder> loginUserHolderMaybe = parseLoginUser(request);
        return loginUserHolderMaybe.isPresent()
                && loginUserHolderMaybe.get().hasManagementRightsForInitiative(initiativeId);
    }

    private static Maybe<LoginUserHolder> parseLoginUser(HttpServletRequest request) {

        Maybe<User> user = getOptionalLoginUser(request);

        if (user.isNotPresent())
            return Maybe.absent();

        return Maybe.of(new LoginUserHolder(user.get()));
    }

    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
    }

    public static Maybe<User> getOptionalLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null)
            return Maybe.absent();

        User user = (User) session.getAttribute(LOGIN_USER_PARAMETER);
        if (user == null)
            return Maybe.absent();

        return Maybe.of(user);
    }

    public User getUser(HttpServletRequest request) {
        Maybe<User> optionalLoginUser = getOptionalLoginUser(request);
        if (optionalLoginUser.isPresent()) {
            return optionalLoginUser.get();
        }
        return User.anonym();
    }
}
