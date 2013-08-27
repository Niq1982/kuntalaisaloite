package fi.om.municipalityinitiative.service;

import com.google.common.collect.Sets;
import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.MunicipalityDao;
import fi.om.municipalityinitiative.dao.UserDao;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.ui.PrepareInitiativeUICreateDto;
import fi.om.municipalityinitiative.dto.user.*;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.exceptions.AuthenticationRequiredException;
import fi.om.municipalityinitiative.exceptions.InvalidLoginException;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Set;

public class UserService {

    static final String LOGIN_USER_PARAMETER = "loginUser";
    static final String VETUMA_PREPARED_INITIATIVE = "vetumaPreparedInitiative";

    @Resource
    UserDao userDao;

    @Resource
    AuthorDao authorDao;

    @Resource
    MunicipalityDao municipalityDao;

    String omUserSalt;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService() {  // This is for tests, spring needs this
    }

    public UserService(String omUserSalt) {
        this.omUserSalt = omUserSalt;
    }

    @Transactional(readOnly = true)
    public void adminLogin(String userName, String password, HttpServletRequest request) {
        User adminUser = userDao.getAdminUser(userName, saltAndEncryptPassword(password));
        storeLoggedInUser(request, adminUser);
    }

    private static void storeLoggedInUser(HttpServletRequest request, User adminUser) {
        request.getSession().setAttribute(LOGIN_USER_PARAMETER, adminUser);
    }

    private String saltAndEncryptPassword(String password) {
        return EncryptionService.toSha1(omUserSalt + password);
    }

    @Transactional(readOnly = true)
    public Long authorLogin(String managementHash, HttpServletRequest request) {
        // TODO: Merge these to one call
        Maybe<NormalAuthorId> authorId = authorDao.getAuthorId(managementHash);
        Set<Long> initiativeIds = authorDao.getAuthorsInitiatives(managementHash);

        if (authorId.isNotPresent() || initiativeIds.size() == 0) {
            throw new InvalidLoginException("Invalid login credentials");
        }

        storeLoggedInUser(request, User.normalUser(authorId.get(), initiativeIds));

        return initiativeIds.iterator().next();
    }

    public LoginUserHolder getRequiredLoginUserHolder(HttpServletRequest request) {
        Maybe<LoginUserHolder> loginUserHolder = parseLoginUser(request);

        if (loginUserHolder.isNotPresent()) {
            throw new AccessDeniedException("Not logged in as author");
        }

        return loginUserHolder.get();
    }

    public OmLoginUserHolder getRequiredOmLoginUserHolder(HttpServletRequest request) {

        Maybe<LoginUserHolder> loginUserHolder = parseLoginUser(request);

        if (loginUserHolder.isNotPresent()) {
            throw new AuthenticationRequiredException();
        }
        loginUserHolder.get().assertOmUser();

        return new OmLoginUserHolder((OmLoginUser) loginUserHolder.get().getUser());
    }

    public boolean hasManagementRightForInitiative(Long initiativeId, HttpServletRequest request) {
        Maybe<LoginUserHolder> loginUserHolderMaybe = parseLoginUser(request);
        return loginUserHolderMaybe.isPresent()
                && loginUserHolderMaybe.get().hasManagementRightsForInitiative(initiativeId);
    }

    private Maybe<LoginUserHolder> parseLoginUser(HttpServletRequest request) {

        Maybe<User> user = getOptionalLoginUser(request);

        if (user.isNotPresent()) {
            return Maybe.absent();
        }

        if (user.get().isNotOmUser()) {
            // FIXME: assertStillAuthor(user.get().getAuthorId(), request);
        }

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

    public LoginUserHolder<User> getLoginUserHolder(HttpServletRequest request) {
        return new LoginUserHolder<>(getUser(request));
    }

    public void prepareForLogin(HttpServletRequest request) {
        // Create new session
        request.getSession(true);
    }

    @Transactional(readOnly = false)
    public void login(String hash, String fullName, String address, Maybe<Municipality> vetumaMunicipality, HttpServletRequest request) {

        ContactInfo contactInfo;
        Set<Long> initiativesWithManagementRight;
        Set<Long> initiativesWithParticipation;
        VerifiedUserId verifiedUserId;
        Maybe<VerifiedUser> verifiedUser = userDao.getVerifiedUser(hash);
        if (verifiedUser.isPresent()) {
            userDao.updateUserInformation(hash, fullName, vetumaMunicipality);
            verifiedUser = userDao.getVerifiedUser(hash);
            contactInfo = verifiedUser.get().getContactInfo();
            initiativesWithManagementRight = verifiedUser.get().getInitiativesWithManagementRight();
            verifiedUserId = verifiedUser.get().getAuthorId();
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

        Maybe<Municipality> municipality;
        if (vetumaMunicipality.isPresent()) { // If got municipality from vetuma, replace with municipalitydata stored in our own database
            municipality = Maybe.fromNullable(municipalityDao.getMunicipality(vetumaMunicipality.get().getId()));

            if (municipality.isNotPresent()) {
                log.error("Got municipality from vetuma that was not found from own database: "
                        + vetumaMunicipality.get().getId() + ", "
                        + vetumaMunicipality.get().getNameFi() + ", "
                        + vetumaMunicipality.get().getNameSv());
            }
        }
        else {
            municipality = Maybe.absent();
        }

        storeLoggedInUser(request, User.verifiedUser(verifiedUserId, hash, contactInfo, initiativesWithManagementRight, initiativesWithParticipation, municipality));
    }

    public void putPrepareDataForVetuma(PrepareInitiativeUICreateDto initiative, HttpServletRequest request) {
        request.getSession().setAttribute(VETUMA_PREPARED_INITIATIVE, initiative);
    }

    public Maybe<PrepareInitiativeUICreateDto> popPrepareDataForVetuma(HttpServletRequest request) {
        HttpSession session = request.getSession();
        PrepareInitiativeUICreateDto preparedInitiative = (PrepareInitiativeUICreateDto) session.getAttribute(VETUMA_PREPARED_INITIATIVE);
        if (preparedInitiative == null) {
            return Maybe.absent();
        }
        session.removeAttribute(VETUMA_PREPARED_INITIATIVE);
        return Maybe.of(preparedInitiative);
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
            Maybe<VerifiedUser> verifiedUser = userDao.getVerifiedUser(((VerifiedUser) user).getHash());
            if (verifiedUser.isPresent()) {
                session.setAttribute(LOGIN_USER_PARAMETER, verifiedUser.get());
            }
        }
    }
}
