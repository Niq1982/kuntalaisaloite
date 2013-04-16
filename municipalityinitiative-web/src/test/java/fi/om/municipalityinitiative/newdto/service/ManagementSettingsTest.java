package fi.om.municipalityinitiative.newdto.service;

import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.concurrent.Callable;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ManagementSettingsTest {

    @Test
    public void edit_is_allowed_only_if_initiative_draft() throws Exception {

        final Initiative initiative = createInitiative();

        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return managementSettings(initiative).isAllowEdit();
            }
        }, true, InitiativeState.DRAFT);

    }

    @Test
    public void send_to_review_is_allowed_only_if_initiative_draft() throws Exception {

        final Initiative initiative = createInitiative();

        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return managementSettings(initiative).isAllowSendToReview();
            }
        }, true, InitiativeState.DRAFT);
    }

    @Test
    public void om_accept_is_allowed_only_if_initiative_review() throws Exception {

        final Initiative initiative = createInitiative();

        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return managementSettings(initiative).isAllowOmAccept();
            }
        }, true, InitiativeState.REVIEW);

    }

    @Test
    public void update_is_allowed_only_when_not_draft() throws Exception {

        final Initiative initiative = createInitiative();

        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return managementSettings(initiative).isAllowUpdate();
            }
        }, false, InitiativeState.DRAFT);
    }

    @Test
    public void never_able_to_update_if_sent_to_municipality() throws Exception {

        final Initiative initiative = createInitiative();

        initiative.setSentTime(Maybe.of(new LocalDate(2010, 1, 1)));

        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return managementSettings(initiative).isAllowUpdate();
            }
        }, false, InitiativeState.values());

    }

    @Test
    public void may_be_sent_to_municipality_only_if_at_least_accepted_and_not_sent() throws Exception {

        final Initiative initiative = createInitiative();
        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return managementSettings(initiative).isAllowSendToMunicipality();
            }
        }, true, InitiativeState.ACCEPTED, InitiativeState.PUBLISHED);
    }

    @Test
    public void never_able_to_send_to_municipality_if_already_sent() throws Exception {
        final Initiative initiative = createInitiative();
        initiative.setSentTime(Maybe.of(new LocalDate(2010, 1, 1)));

        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return managementSettings(initiative).isAllowSendToMunicipality();
            }
        }, false, InitiativeState.values());
    }

    @Test
    public void is_allow_publish_only_if_initiative_state_accepted() throws Exception {
        final Initiative initiative = createInitiative();

        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return managementSettings(initiative).isAllowPublish();
            }
        }, true, InitiativeState.ACCEPTED);
    }

    @Test
    public void is_allow_to_participate_only_if_initiative_state_is_published() throws Exception {
        final Initiative initiative = createInitiative();
        initiative.setSentTime(Maybe.<LocalDate>absent());
        initiative.setType(InitiativeType.COLLABORATIVE);

        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return managementSettings(initiative).isAllowParticipate();
            }
        }, true, InitiativeState.PUBLISHED);
    }

    @Test
    public void is_not_allowed_to_participate_if_initiative_sent() {
        final Initiative initiative = createInitiative();
        initiative.setType(InitiativeType.COLLABORATIVE);
        initiative.setState(InitiativeState.PUBLISHED);

        precondition(managementSettings(initiative).isAllowParticipate(), is(true));
        initiative.setSentTime(Maybe.of(new LocalDate(2010, 1, 1)));
        assertThat(managementSettings(initiative).isAllowParticipate(), is(false));
    }

    @Test
    public void is_not_allowed_to_participate_if_initiative_not_collectable() {
        final Initiative initiative = createInitiative();
        initiative.setType(InitiativeType.COLLABORATIVE);
        initiative.setState(InitiativeState.PUBLISHED);

        precondition(managementSettings(initiative).isAllowParticipate(), is(true));
        initiative.setType(InitiativeType.SINGLE);
        assertThat(managementSettings(initiative).isAllowParticipate(), is(false));
    }

    private static void assertExpectedOnlyWithGivenStates(Initiative initiative, Callable<Boolean> callable, boolean expected, InitiativeState... givenStates) throws Exception {
        for (InitiativeState initiativeState : InitiativeState.values()) {
            initiative.setState(initiativeState);
            if (isInGivenStates(initiativeState, givenStates)) {
                assertThat(initiativeState.name(), callable.call(), is(expected));
            }
            else {
                assertThat(initiativeState.name(), callable.call(), is(!expected));
            }
        }

    }

    private static boolean isInGivenStates(InitiativeState initiativeState, InitiativeState[] givenStates) {
        for (InitiativeState givenState : givenStates) {
            if (givenState == initiativeState)
                return true;
        }
        return false;
    }

    private static Initiative createInitiative() {
        Initiative initiative = new Initiative();
        return initiative;
    }

    private static ManagementSettings managementSettings(Initiative initiative) {
        return new ManagementSettings(initiative);

    }


}
