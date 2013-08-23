package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.ui.ParticipantListInfo;
import fi.om.municipalityinitiative.web.Urls;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.List;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class ParticipantServiceIntegrationTest extends ServiceIntegrationTestBase{

    @Resource
    private ParticipantService participantService;

    private Long testMunicipalityId;

    @Override
    protected void childSetup() {
        testMunicipalityId = testHelper.createTestMunicipality("Some municipality");
    }

    @Test
    public void findPublicParticipants_for_verified_initiative() {
        Long initiativeId = createVerifiedInitiativeWithAuthor();
        assertThat(participantService.findPublicParticipants(0, initiativeId), hasSize(1));
    }

    @Test
    public void findPublicParticipants_for_normal_initiative() {
        Long initiativeId = createNormalInitiativeWithAuthor();
        assertThat(participantService.findPublicParticipants(0, initiativeId), hasSize(1));
    }

    @Test
    public void findPublicParticipants_limits_results() {
        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipalityId));

        int participantCount = 101;
        createParticipants(initiativeId, participantCount);

        // Get without offset, should be limited
        assertThat(participantService.findPublicParticipants(0, initiativeId), hasSize(Urls.MAX_PARTICIPANT_LIST_LIMIT));

        int offset = 60;

        // Use offset, result is the content of "last page"
        List<ParticipantListInfo> lastOnes = participantService.findPublicParticipants(offset, initiativeId);
        assertThat(lastOnes, hasSize(participantCount- offset));

        assertThat(lastOnes.get(0).getParticipant().getName(), is(String.valueOf(participantCount - offset)));
        assertThat(lastOnes.get(lastOnes.size()-1).getParticipant().getName(), is("1"));

    }

    private void createParticipants(Long initiativeId, int participantCount) {
        for (int i = 0; i < participantCount; ++i) {
            testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipalityId).withParticipantName(String.valueOf(i+1)));
        }
    }

    @Test
    public void findAllParticipants_for_verified_initiative() {
        Long initiativeId = createVerifiedInitiativeWithAuthor();
        assertThat(participantService.findAllParticipants(initiativeId, TestHelper.authorLoginUserHolder, 0), hasSize(1));
    }

    @Test
    public void findAllParticipants_for_normal_initiative() {
        Long initiativeId = createNormalInitiativeWithAuthor();
        assertThat(participantService.findAllParticipants(initiativeId, TestHelper.authorLoginUserHolder, 0), hasSize(1));
    }

    @Test
    public void findPublicParticipants_for_normal_initiative_sets_author_flag_true_if_author() {
        Long initiativeId = createNormalInitiativeWithAuthor();
        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipalityId).withPublicName(true));

        List<ParticipantListInfo> publicParticipants = participantService.findPublicParticipants(0, initiativeId);
        precondition(publicParticipants, hasSize(2));

        assertThat(publicParticipants.get(0).isAuthor(), is(false));
        assertThat(publicParticipants.get(1).isAuthor(), is(true));
    }

    @Test
    public void findPublicParticipants_for_verified_initiative_sets_author_flag_true_if_author() {
        Long initiativeId = createVerifiedInitiativeWithAuthor();
        testHelper.createVerifiedParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipalityId).withPublicName(true));

        List<ParticipantListInfo> publicParticipants = participantService.findPublicParticipants(0, initiativeId);
        precondition(publicParticipants, hasSize(2));

        assertThat(publicParticipants.get(0).isAuthor(), is(false));
        assertThat(publicParticipants.get(1).isAuthor(), is(true));

    }

    private Long createNormalInitiativeWithAuthor() {
        return testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).applyAuthor().toInitiativeDraft());
    }

    private Long createVerifiedInitiativeWithAuthor() {
        return testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).applyAuthor().toInitiativeDraft());
    }


}
