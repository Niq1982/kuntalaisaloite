package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dao.UserDao;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.exceptions.InvalidLoginException;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.util.FakeSession;
import fi.om.municipalityinitiative.util.Maybe;
import org.apache.http.HttpResponse;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class UserServiceIntegrationTest extends ServiceIntegrationTestBase{

    @Resource
    private UserService userService;

    private HttpServletRequest requestMock;

    @Resource
    private UserDao userDao; // Hmm... Ugly to depend on this, but is used on assertions

    @Override
    public void childSetup() {

        requestMock = mock(HttpServletRequest.class);
        HttpSession session = new FakeSession();
        stub(requestMock.getSession()).toReturn(session);
    }

    @Test
    public void admin_user_login_is_ok() {
        testHelper.createTestAdminUser("admin", "password", "Some Admin Name");

        userService.adminLogin("admin", "password", requestMock);

        assertThat(userService.getRequiredOmLoginUserHolder(requestMock).getUser().isOmUser(), is(true));
    }

    @Test
    public void admin_user_has_full_name() {

        testHelper.createTestAdminUser("admin", "password", "Some Admin Name");

        userService.adminLogin("admin", "password", requestMock);

        assertThat(userService.getRequiredOmLoginUserHolder(requestMock).getUser().getName(), is("Some Admin Name"));
    }

    @Test(expected = InvalidLoginException.class)
    public void throws_exception_if_user_not_found() {
        userService.adminLogin("admin", "password", requestMock);
    }

    @Test(expected = InvalidLoginException.class)
    public void author_login_with_invalid_hash_throws_exception() {
        userService.authorLogin("unknown hash", requestMock);
    }

    @Test
    public void author_login_with_management_hash_logs_user_in() {
        Long initiative = testHelper.createCollaborativeAccepted(testHelper.createTestMunicipality("moi"));

        userService.authorLogin(testHelper.getPreviousTestManagementHash(), requestMock);
        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(requestMock);

        assertThat(loginUserHolder.getNormalLoginUser().getAuthorId(), is(testHelper.getLastAuthorId()));
        assertThat(loginUserHolder.getUser().hasRightToInitiative(initiative), is(true));
        assertThat(loginUserHolder.getUser().hasRightToInitiative(-1L), is(false));
    }

    @Test
    public void vetuma_login_gets_municipality_and_saves_all_data_to_session() {

        String municipalityName = "Some municipality";
        Long municipalityId = testHelper.createTestMunicipality(municipalityName);
        Municipality municipality = new Municipality(municipalityId, municipalityName, "", false);

        String name = "Full Name";
        String address = "Address";
        userService.login("anyHash", name, address, Maybe.of(municipality), requestMock, mock(HttpServletResponse.class));

        LoginUserHolder<User> loginUserHolder = userService.getLoginUserHolder(requestMock);

        assertThat(loginUserHolder.isVerifiedUser(), is(true));
        assertThat(loginUserHolder.getVerifiedUser().getInitiatives(), hasSize(0));
        assertThat(loginUserHolder.getVerifiedUser().getHash(), is(notNullValue()));
        assertThat(loginUserHolder.getVerifiedUser().getContactInfo().getName(), is(name));
        assertThat(loginUserHolder.getVerifiedUser().getContactInfo().getAddress(), is(address));
        assertThat(loginUserHolder.getVerifiedUser().getHomeMunicipality().isPresent(), is(true));
        assertThat(loginUserHolder.getVerifiedUser().getHomeMunicipality().get().getId(), is(municipalityId));
        assertThat(loginUserHolder.getVerifiedUser().getHomeMunicipality().get().getNameFi(), is(municipalityName));
    }

    @Test
    public void vetuma_login_sets_municipality_absent_if_not_found() {
        userService.login("anyHash", "Full Name", "Address", Maybe.<Municipality>absent(), requestMock, mock(HttpServletResponse.class));
        LoginUserHolder<User> loginUserHolder = userService.getLoginUserHolder(requestMock);
        assertThat(loginUserHolder.getVerifiedUser().getHomeMunicipality().isPresent(), is(false));

    }

    @Test
    @Transactional // Uses userDao for asserting saved data
    public void login_updates_saved_users_municipality_and_name_if_changed() {
        Long originalMunicipalityId = testHelper.createTestMunicipality("Some municipality");

        testHelper.createInitiative(new TestHelper.InitiativeDraft(originalMunicipalityId).applyAuthor().toInitiativeDraft(), true);
        String oldAddress = TestHelper.DEFAULT_AUTHOR_ADDRESS;
        String userSsnHash = testHelper.getPreviousUserSsnHash();

        String newMunicipalityName = "New Municipality";
        Long newMunicipalityId = testHelper.createTestMunicipality(newMunicipalityName);
        Municipality newMunicipality = new Municipality(newMunicipalityId, "anyNameFromVetuma", "anyNameFromVetuma", true);

        String newName = "New Users Name";
        userService.login(userSsnHash, newName, "New address which will not be saved", Maybe.of(newMunicipality), requestMock, mock(HttpServletResponse.class));

        VerifiedUser verifiedUser = userDao.getVerifiedUser(userSsnHash).get();
        assertThat(verifiedUser.getContactInfo().getName(), is(newName));
        assertThat(verifiedUser.getContactInfo().getAddress(), is(oldAddress));
        assertThat(verifiedUser.getHomeMunicipality().get().getNameFi(), is(newMunicipalityName));

    }

    @Test
    @Transactional
    public void login_updates_municipality_to_null_if_changed_to_null() {
        Long originalMunicipalityId = testHelper.createTestMunicipality("Some municipality");

        testHelper.createInitiative(new TestHelper.InitiativeDraft(originalMunicipalityId).applyAuthor().toInitiativeDraft(), true);
        String oldAddress = TestHelper.DEFAULT_AUTHOR_ADDRESS;
        String userSsnHash = testHelper.getPreviousUserSsnHash();

        String newName = "New Users Name";
        userService.login(userSsnHash, newName, "New address which will not be saved", Maybe.<Municipality>absent(), requestMock, mock(HttpServletResponse.class));

        VerifiedUser verifiedUser = userDao.getVerifiedUser(userSsnHash).get();
        assertThat(verifiedUser.getContactInfo().getName(), is(newName));
        assertThat(verifiedUser.getContactInfo().getAddress(), is(oldAddress));
        assertThat(verifiedUser.getHomeMunicipality().isPresent(), is(false));
    }

    @Test
    public void login_gets_and_saves_old_user_information_to_session_if_user_already_exists() {

        Long originalMunicipalityId = testHelper.createTestMunicipality("Some municipality");

        testHelper.createInitiative(new TestHelper.InitiativeDraft(originalMunicipalityId).applyAuthor().toInitiativeDraft(), true);
        String oldAddress = TestHelper.DEFAULT_AUTHOR_ADDRESS;
        String oldEmail = TestHelper.DEFAULT_PARTICIPANT_EMAIL;
        String oldPhone = TestHelper.DEFAULT_AUTHOR_PHONE;

        String userSsnHash = testHelper.getPreviousUserSsnHash();

        String newName = "New Users Name";
        userService.login(userSsnHash, newName, "New address which will not be saved", Maybe.<Municipality>absent(), requestMock, mock(HttpServletResponse.class));

        VerifiedUser verifiedUser = (VerifiedUser) userService.getUser(requestMock);
        assertThat(verifiedUser.getContactInfo().getName(), is(newName));
        assertThat(verifiedUser.getContactInfo().getAddress(), is(oldAddress));
        assertThat(verifiedUser.getContactInfo().getPhone(), is(oldPhone));
        assertThat(verifiedUser.getContactInfo().getEmail(), is(oldEmail));
        assertThat(verifiedUser.getHomeMunicipality().isPresent(), is(false));
    }


}
