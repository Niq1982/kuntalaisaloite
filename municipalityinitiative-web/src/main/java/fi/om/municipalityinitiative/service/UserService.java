package fi.om.municipalityinitiative.service;

import com.google.common.collect.Sets;
import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.MunicipalityDao;
import fi.om.municipalityinitiative.dao.MunicipalityUserDao;
import fi.om.municipalityinitiative.dao.UserDao;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.service.VerifiedUserDbDetails;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.user.*;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.exceptions.AuthenticationRequiredException;
import fi.om.municipalityinitiative.exceptions.InvalidLoginException;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.Set;

public class UserService {

    static final String LOGIN_USER_PARAMETER = "loginUser";

    @Resource
    private UserDao userDao;

    @Resource
    private AuthorDao authorDao;

    @Resource
    private MunicipalityUserDao municipalityUserDao;

    @Resource
    private MunicipalityDao municipalityDao;

    private String omUserSalt;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService() {  // This is for tests, spring needs this
    }

    public UserService(String omUserSalt) {
        this.omUserSalt = omUserSalt;
    }

    @Transactional(readOnly = true)
    public void adminLogin(String userName, String password, HttpServletRequest request) {
        User adminUser = userDao.getAdminUser(userName, saltAndEncryptPassword(password));
        storeLoggedInUserSession(request, adminUser);
    }

    private static void storeLoggedInUserSession(HttpServletRequest request, User user) {
        request.getSession(true).setAttribute(LOGIN_USER_PARAMETER, user);
    }

    private String saltAndEncryptPassword(String password) {
        return EncryptionService.toSha1(omUserSalt + password);
    }

    @Transactional(readOnly = true)
    public Long authorLogin(String managementHash, HttpServletRequest request) {
        // TODO: Merge these to one call
        Optional<NormalAuthorId> authorId = authorDao.getAuthorId(managementHash);
        Set<Long> initiativeIds = authorDao.getAuthorsInitiatives(managementHash);

        if (!authorId.isPresent() || initiativeIds.size() == 0) {
            throw new InvalidLoginException("Invalid login credentials");
        }

        storeLoggedInUserSession(request, User.normalUser(authorId.get(), initiativeIds));

        return initiativeIds.iterator().next();
    }

    public LoginUserHolder getRequiredLoginUserHolder(HttpServletRequest request) {
        Optional<LoginUserHolder> loginUserHolder = parseLoginUser(request);

        if (!loginUserHolder.isPresent()) {
            throw new AccessDeniedException("Not logged in as author");
        }

        return loginUserHolder.get();
    }

    public OmLoginUserHolder getRequiredOmLoginUserHolder(HttpServletRequest request) {

        Optional<LoginUserHolder> loginUserHolder = parseLoginUser(request);

        if (!loginUserHolder.isPresent()) {
            throw new AuthenticationRequiredException();
        }
        loginUserHolder.get().assertOmUser();

        return new OmLoginUserHolder((OmLoginUser) loginUserHolder.get().getUser());
    }

    public boolean hasManagementRightForInitiative(Long initiativeId, HttpServletRequest request) {
        Optional<LoginUserHolder> loginUserHolderOptional = parseLoginUser(request);
        return loginUserHolderOptional.isPresent()
                && loginUserHolderOptional.get().hasManagementRightsForInitiative(initiativeId);
    }

    private static Optional<LoginUserHolder> parseLoginUser(HttpServletRequest request) {

        Optional<User> user = getOptionalLoginUser(request);

        if (!user.isPresent()) {
            return Optional.empty();
        }

        if (user.get().isNotOmUser()) {
            // NOTE: assertStillAuthor(user.get().getAuthorId(), request); -- would be nice.
            // Because management rights are stored in session, it's possible that author has management rights
            // as long as it's session is valid even that author is deleted.
        }

        return Optional.of(new LoginUserHolder(user.get()));
    }

    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
    }

    public static Optional<User> getOptionalLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return Optional.empty();

        User user = (User) session.getAttribute(LOGIN_USER_PARAMETER);
        if (user == null)
            return Optional.empty();

        return Optional.of(user);
    }

    public User getUser(HttpServletRequest request) {
        Optional<User> optionalLoginUser = getOptionalLoginUser(request);
        if (optionalLoginUser.isPresent()) {
            return optionalLoginUser.get();
        }
        return User.anonym();
    }

    public LoginUserHolder<User> getLoginUserHolder(HttpServletRequest request) {
        return new LoginUserHolder<>(getUser(request));
    }

    public void prepareForLogin(HttpServletRequest request) {
        // Create new session
        request.getSession(true);
    }

    @Transactional(readOnly = false)
    public void login(String hash, String fullName, String address, Optional<Municipality> vetumaMunicipality, HttpServletRequest request, int age) {

        ContactInfo contactInfo;
        Set<Long> initiativesWithManagementRight;
        Set<Long> initiativesWithParticipation;
        VerifiedUserId verifiedUserId;
        Optional<VerifiedUserDbDetails> verifiedUser = userDao.getVerifiedUser(hash);
        if (verifiedUser.isPresent()) {
            userDao.updateUserInformation(hash, fullName, vetumaMunicipality);
            verifiedUser = userDao.getVerifiedUser(hash);
            contactInfo = verifiedUser.get().getContactInfo();
            initiativesWithManagementRight = verifiedUser.get().getInitiativesWithManagementRight();
            verifiedUserId = verifiedUser.get().getVerifiedUserId();
            initiativesWithParticipation = verifiedUser.get().getInitiativesWithParticipation();
        }
        else {
            contactInfo = new ContactInfo(); // User logged in but never registered to database (has not participated or created any initiatives)
            contactInfo.setName(fullName);
            contactInfo.setAddress(address);
            initiativesWithManagementRight = Sets.newHashSet();
            initiativesWithParticipation = Sets.newHashSet();
            verifiedUserId = null; // FIXME: This is bad.
        }

        Optional<Municipality> municipality;
        if (vetumaMunicipality.isPresent()) { // If got municipality from vetuma, replace with municipalitydata stored in our own database
            municipality = Optional.ofNullable(municipalityDao.getMunicipality(vetumaMunicipality.get().getId()));

            if (!municipality.isPresent()) {
                log.error("Got municipality from vetuma that was not found from own database: "
                        + vetumaMunicipality.get().getId() + ", "
                        + vetumaMunicipality.get().getNameFi() + ", "
                        + vetumaMunicipality.get().getNameSv());
            }
        }
        else {
            municipality = Optional.empty();
        }

        storeLoggedInUserSession(request, User.verifiedUser(verifiedUserId, hash, contactInfo, initiativesWithManagementRight, initiativesWithParticipation, municipality, age));
    }

    /**
     * After changes to verified users contact info has been made, this function should be called so
     * contact information stored to session will be updated.
     * Nothing bad will happen if it's not called, only some prefilled fields may be filled with old information. Ugly tho.
     *
     * @param request
     */
    @Transactional(readOnly = true)
    public void refreshUserData(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(LOGIN_USER_PARAMETER);

        if (user instanceof VerifiedUser) {
            Optional<VerifiedUserDbDetails> dbVerifiedUser = userDao.getVerifiedUser(((VerifiedUser) user).getHash());
            if (dbVerifiedUser.isPresent()) {

                VerifiedUser refreshedUser = User.verifiedUser(
                        dbVerifiedUser.get().getVerifiedUserId(),
                        dbVerifiedUser.get().getHash(),
                        dbVerifiedUser.get().getContactInfo(),
                        dbVerifiedUser.get().getInitiativesWithManagementRight(),
                        dbVerifiedUser.get().getInitiativesWithParticipation(),
                        dbVerifiedUser.get().getHomeMunicipality(),
                        ((VerifiedUser) user).getAge());

                session.setAttribute(LOGIN_USER_PARAMETER, refreshedUser);
            }
        }
    }

    public MunicipalityUserHolder getRequiredMunicipalityUserHolder(HttpServletRequest request, Long initiativeId) {

        Optional<LoginUserHolder> loginUserHolder = parseLoginUser(request);

        if (!loginUserHolder.isPresent()) {
            throw new AuthenticationRequiredException();
        }
        loginUserHolder.get().assertMunicipalityLoginUser(initiativeId);

        return new MunicipalityUserHolder((MunicipalityLoginUser) loginUserHolder.get().getUser());
    }

    @Transactional(readOnly = true)
    public Long municipalityUserLogin(String managementHash, String managementLoginHash, HttpServletRequest request) {
        Long initiativeId = municipalityUserDao.getInitiativeId(managementHash, managementLoginHash);
        if (initiativeId == null) {
            throw new InvalidLoginException("Invalid login credentials");
        }
        storeLoggedInUserSession(request, User.municipalityLoginUser(initiativeId));
        return initiativeId;
    }

}
