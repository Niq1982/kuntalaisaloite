package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.service.VerifiedUserDbDetails;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static fi.om.municipalityinitiative.util.MaybeMatcher.isNotPresent;
import static fi.om.municipalityinitiative.util.MaybeMatcher.isPresent;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
@Transactional
public class JdbcUserDaoTest {

    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String HASH = "hash";

    @Resource
    private TestHelper testHelper;

    @Resource
    private UserDao userDao;

    @Resource
    ParticipantDao participantDao;

    private Maybe<Municipality> testMunicipality;
    private Long testMunicipalityId;
    private Long testInitiativeId;
    private Long testVerifiedInitiativeId;
    private Long otherMunicipalityId;

    @Before
    public void setup() throws Exception {
        testHelper.dbCleanup();
        testMunicipality = Maybe.of(new Municipality(testHelper.createTestMunicipality("Municipality"), "Municipality", "Municipality", true));
        testMunicipalityId = testMunicipality.getValue().getId();
        testInitiativeId = testHelper.create(testMunicipalityId, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testVerifiedInitiativeId = testHelper.create(testMunicipalityId, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE_CITIZEN);
    }

    @Test
    public void create_and_get_verified_user() {
        ContactInfo contactInfo = contactInfo();
        VerifiedUserId verifiedUserId = userDao.addVerifiedUser(HASH, contactInfo, testMunicipality);
        Maybe<VerifiedUserDbDetails> verifiedUser = userDao.getVerifiedUser(HASH);
        assertThat(verifiedUser, isPresent());
        assertThat(verifiedUserId, is(notNullValue()));
        ReflectionTestUtils.assertReflectionEquals(verifiedUser.get().getContactInfo(), contactInfo);
        assertThat(verifiedUser.get().getHomeMunicipality().get().getId(), is(testMunicipality.get().getId()));
    }

    @Test
    public void update_contact_info() {
        userDao.addVerifiedUser(HASH, contactInfo(), testMunicipality);

        ContactInfo updatedContactInfo = ReflectionTestUtils.modifyAllFields(new ContactInfo());
        userDao.updateUserInformation(HASH, updatedContactInfo);

        ContactInfo result = userDao.getVerifiedUser(HASH).get().getContactInfo();
        assertThat(result.getPhone(), is(updatedContactInfo.getPhone()));
        assertThat(result.getAddress(), is(updatedContactInfo.getAddress()));
        assertThat(result.getEmail(), is(updatedContactInfo.getEmail()));
        assertThat(result.getName(), is(not(updatedContactInfo.getName()))); // Name should not be changed);
    }

    @Test
    public void update_name_and_municipality() {

        userDao.addVerifiedUser(HASH, contactInfo(), testMunicipality);

        String newName = "New Name";
        String newMunicipalityName = "name";
        userDao.updateUserInformation(HASH, newName, Maybe.of(new Municipality(testHelper.createTestMunicipality(newMunicipalityName), newMunicipalityName, newMunicipalityName, true)));

        VerifiedUserDbDetails result = userDao.getVerifiedUser(HASH).get();
        assertThat(result.getContactInfo().getName(), is(newName));
        assertThat(result.getHomeMunicipality(), isPresent());
        assertThat(result.getHomeMunicipality().get().getNameFi(), is(newMunicipalityName));
    }

    @Test
    public void get_returns_absent_if_not_found() {
        assertThat(userDao.getVerifiedUser("unknown-user-hash"), isNotPresent());
    }

    @Test
    public void get_initiatives_verified_user_has_participated() {

        userDao.addVerifiedUser(HASH, contactInfo(), testMunicipality);
        // Verified user participates verified initiative
        Long verifiedUserId = userDao.getVerifiedUserId(HASH).getValue().toLong();
        testHelper.createVerifiedParticipantWithVerifiedUserId(new TestHelper.AuthorDraft(testVerifiedInitiativeId, testMunicipalityId).withVerifiedUserId(verifiedUserId));

        VerifiedUserDbDetails user = userDao.getVerifiedUser(HASH).getValue();

        assertThat(user.getInitiativesWithParticipation(), contains(testVerifiedInitiativeId));
    }

    @Test
    public void get_normal_initiatives_verified_user_has_participated() {

        userDao.addVerifiedUser(HASH, contactInfo(), testMunicipality);
        Long verifiedUserId = userDao.getVerifiedUserId(HASH).getValue().toLong();

        // Verified user participates to normal initiative
        Long participantId = testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(testInitiativeId, testMunicipalityId));
        participantDao.verifiedUserParticipatesNormalInitiative(participantId, new VerifiedUserId(verifiedUserId), true);

        VerifiedUserDbDetails user = userDao.getVerifiedUser(HASH).getValue();

        assertThat(user.getInitiativesWithParticipation(), contains(testInitiativeId));

    }

    private static ContactInfo contactInfo() {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(EMAIL);
        contactInfo.setName(NAME);
        contactInfo.setPhone(PHONE);
        contactInfo.setAddress(ADDRESS);
        return contactInfo;
    }
}
