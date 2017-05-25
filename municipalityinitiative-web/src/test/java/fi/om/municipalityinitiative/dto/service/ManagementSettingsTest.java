package fi.om.municipalityinitiative.dto.service;

import fi.om.municipalityinitiative.util.FixState;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.Callable;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ManagementSettingsTest {

    @Test
    public void edit_is_allowed_only_if_initiative_draft() throws Exception {

        final Initiative initiative = new Initiative();

        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ManagementSettings.of(initiative).isAllowEdit();
            }
        }, true, InitiativeState.DRAFT);

    }

    @Test
    public void send_to_review_is_allowed_only_if_initiative_draft() throws Exception {

        final Initiative initiative = new Initiative();

        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ManagementSettings.of(initiative).isAllowSendToReview();
            }
        }, true, InitiativeState.DRAFT);
    }

    @Test
    public void om_accept_is_allowed_if_initiative_review() throws Exception {

        final Initiative initiative = new Initiative();
        initiative.setFixState(FixState.OK);

        InitiativeState review = InitiativeState.REVIEW;
        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ManagementSettings.of(initiative).isAllowOmAccept();
            }
        }, true, review);

    }

    @Test
    public void om_accept_is_allowed_if_initiative_fixState_is_review() throws Exception {
        Initiative initiative = new Initiative();

        initiative.setFixState(FixState.REVIEW);
        assertThat(ManagementSettings.of(initiative).isAllowOmAccept(), is(true));

        initiative.setFixState(FixState.OK);
        assertThat(ManagementSettings.of(initiative).isAllowOmAccept(), is(false));
        initiative.setFixState(FixState.FIX);
        assertThat(ManagementSettings.of(initiative).isAllowOmAccept(), is(false));

    }

    @Test
    public void update_is_allowed_only_when_not_draft() throws Exception {

        final Initiative initiative = new Initiative();

        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ManagementSettings.of(initiative).isAllowUpdate();
            }
        }, false, InitiativeState.DRAFT);
    }

    @Test
    public void never_able_to_update_if_sent_to_municipality() throws Exception {

        final Initiative initiative = new Initiative();

        initiative.setSentTime(Optional.of(new LocalDate(2010, 1, 1)));

        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ManagementSettings.of(initiative).isAllowUpdate();
            }
        }, false, InitiativeState.values());

    }

    @Test
    public void may_be_sent_to_municipality_only_if_at_least_accepted_and_not_sent() throws Exception {

        final Initiative initiative = new Initiative();
        initiative.setFixState(FixState.OK);
        initiative.setType(InitiativeType.UNDEFINED);
        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ManagementSettings.of(initiative).isAllowSendToMunicipality();
            }
        }, true, InitiativeState.ACCEPTED, InitiativeState.PUBLISHED);
    }

    @Test
    public void may_not_be_sent_to_municipality_if_verified_initiative_and_not_published() throws Exception {
        final Initiative initiative = new Initiative();
        initiative.setFixState(FixState.OK);
        initiative.setType(InitiativeType.COLLABORATIVE_CITIZEN);

        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ManagementSettings.of(initiative).isAllowSendToMunicipality();
            }
        }, true, InitiativeState.PUBLISHED);
    }

    @Test
    public void never_able_to_send_to_municipality_if_already_sent() throws Exception {
        final Initiative initiative = new Initiative();
        initiative.setSentTime(Optional.of(new LocalDate(2010, 1, 1)));

        initiative.setType(InitiativeType.UNDEFINED);
        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ManagementSettings.of(initiative).isAllowSendToMunicipality();
            }
        }, false, InitiativeState.values());

        initiative.setType(InitiativeType.COLLABORATIVE_CITIZEN);
        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ManagementSettings.of(initiative).isAllowSendToMunicipality();
            }
        }, false, InitiativeState.values());

        initiative.setType(InitiativeType.COLLABORATIVE);
        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ManagementSettings.of(initiative).isAllowSendToMunicipality();
            }
        }, false, InitiativeState.values());
    }

    @Test
    public void not_able_to_send_to_municipality_if_fixState_not_ok() {
        final Initiative initiative = new Initiative();
        initiative.setState(InitiativeState.PUBLISHED);
        initiative.setFixState(FixState.OK);
        precondition(ManagementSettings.of(initiative).isAllowSendToMunicipality(), is(true));

        initiative.setFixState(FixState.FIX);
        precondition(ManagementSettings.of(initiative).isAllowSendToMunicipality(), is(false));

        initiative.setFixState(FixState.REVIEW);
        precondition(ManagementSettings.of(initiative).isAllowSendToMunicipality(), is(false));

    }

    @Test
    public void is_allow_publish_only_if_initiative_state_accepted() throws Exception {
        final Initiative initiative = new Initiative();

        initiative.setFixState(FixState.OK);

        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ManagementSettings.of(initiative).isAllowPublish();
            }
        }, true, InitiativeState.ACCEPTED);

    }

    @Test
    public void is_not_allow_publish_is_fixState_not_ok() {
        Initiative initiative = new Initiative();

        initiative.setState(InitiativeState.ACCEPTED);
        initiative.setFixState(FixState.OK);
        precondition(ManagementSettings.of(initiative).isAllowPublish(), is(true));

        initiative.setFixState(FixState.FIX);
        assertThat(ManagementSettings.of(initiative).isAllowPublish(), is(false));
        initiative.setFixState(FixState.REVIEW);
        assertThat(ManagementSettings.of(initiative).isAllowPublish(), is(false));

    }

    @Test
    public void is_allow_to_participate_only_if_initiative_state_is_published() throws Exception {
        final Initiative initiative = new Initiative();
        initiative.setSentTime(Optional.<LocalDate>empty());
        initiative.setType(InitiativeType.COLLABORATIVE);
        initiative.setFixState(FixState.OK);

        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ManagementSettings.of(initiative).isAllowParticipation();
            }
        }, true, InitiativeState.PUBLISHED);
    }

    @Test
    public void is_not_allowed_to_participate_if_initiative_sent() {
        final Initiative initiative = new Initiative();
        initiative.setType(InitiativeType.COLLABORATIVE);
        initiative.setState(InitiativeState.PUBLISHED);
        initiative.setFixState(FixState.OK);

        precondition(ManagementSettings.of(initiative).isAllowParticipation(), is(true));
        initiative.setSentTime(Optional.of(new LocalDate(2010, 1, 1)));

        assertThat(ManagementSettings.of(initiative).isAllowParticipation(), is(false));
    }

    @Test
    public void is_not_allowed_to_participate_if_initiative_not_collaborative() {
        final Initiative initiative = new Initiative();
        initiative.setType(InitiativeType.COLLABORATIVE);
        initiative.setState(InitiativeState.PUBLISHED);
        initiative.setFixState(FixState.OK);

        precondition(ManagementSettings.of(initiative).isAllowParticipation(), is(true));
        initiative.setType(InitiativeType.SINGLE);

        assertThat(ManagementSettings.of(initiative).isAllowParticipation(), is(false));
    }

    @Test
    public void is_not_allowed_to_participate_if_fixState_not_ok() {

        final Initiative initiative = new Initiative();
        initiative.setType(InitiativeType.COLLABORATIVE);
        initiative.setState(InitiativeState.PUBLISHED);
        initiative.setFixState(FixState.OK);

        precondition(ManagementSettings.of(initiative).isAllowParticipation(), is(true));

        initiative.setFixState(FixState.FIX);
        assertThat(ManagementSettings.of(initiative).isAllowParticipation(), is(false));
        initiative.setFixState(FixState.REVIEW);
        assertThat(ManagementSettings.of(initiative).isAllowParticipation(), is(false));

    }

    @Test
    public void is_allowed_to_invite_authors_only_if_accepted_or_published() throws Exception {
            final Initiative initiative = new Initiative();
            initiative.setFixState(FixState.OK);

        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ManagementSettings.of(initiative).isAllowInviteAuthors();
            }
        }, true, InitiativeState.PUBLISHED, InitiativeState.ACCEPTED);

    }

    @Test
    public void is_not_allowed_to_invite_authors_if_initiative_sent() {
        final Initiative sentInitiative = new Initiative();
        sentInitiative.setState(InitiativeState.PUBLISHED);
        sentInitiative.setFixState(FixState.OK);

        precondition(ManagementSettings.of(sentInitiative).isAllowInviteAuthors(), is(true));
        sentInitiative.setSentTime(Optional.of(new LocalDate()));
        assertThat(ManagementSettings.of(sentInitiative).isAllowInviteAuthors(), is(false));
    }

    @Test
    public void is_allowed_to_invite_authors_if_initiative_not_sent() {
        final Initiative notSentInitiative = new Initiative();
        notSentInitiative.setFixState(FixState.OK);
        notSentInitiative.setState(InitiativeState.PUBLISHED);
        notSentInitiative.setSentTime(Optional.of(new LocalDate()));

        assertThat(ManagementSettings.of(notSentInitiative).isAllowInviteAuthors(), is(false));
        notSentInitiative.setSentTime(Optional.<LocalDate>empty());
        assertThat(ManagementSettings.of(notSentInitiative).isAllowInviteAuthors(), is(true));
    }

    @Test
    public void is_not_allowed_to_inivite_authors_if_fixState_not_OK() {
        final Initiative publishedInitiative = new Initiative();
        publishedInitiative.setState(InitiativeState.PUBLISHED);
        publishedInitiative.setFixState(FixState.OK);
        precondition(ManagementSettings.of(publishedInitiative).isAllowInviteAuthors(), is(true));

        publishedInitiative.setFixState(FixState.REVIEW);
        assertThat(ManagementSettings.of(publishedInitiative).isAllowInviteAuthors(), is(false));

        publishedInitiative.setFixState(FixState.FIX);
        assertThat(ManagementSettings.of(publishedInitiative).isAllowInviteAuthors(), is(false));
    }

    @Test
    public void is_allowed_to_send_initiative_back_for_fixing_only_when_accepted_or_published() throws Exception {
        final Initiative initiative = new Initiative();
        initiative.setFixState(FixState.OK);

        assertExpectedOnlyWithGivenStates(initiative, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ManagementSettings.of(initiative).isAllowOmSendBackForFixing();
            }
        }, true, InitiativeState.PUBLISHED, InitiativeState.ACCEPTED);
    }

    @Test
    public void not_allowed_to_send_initiative_back_for_fixing_if_initiative_sent() throws Exception {
        final Initiative sentInitiative = new Initiative();
        sentInitiative.setState(InitiativeState.PUBLISHED);
        sentInitiative.setSentTime(Optional.of(new LocalDate()));

        assertThat(ManagementSettings.of(sentInitiative).isAllowOmSendBackForFixing(), is(false));
    }

    @Test
    public void not_allowed_to_send_back_for_fixing_if_already_sent_back() {
        final Initiative publishedInitiative = new Initiative();
        publishedInitiative.setState(InitiativeState.PUBLISHED);
        publishedInitiative.setFixState(FixState.OK);
        precondition(ManagementSettings.of(publishedInitiative).isAllowOmSendBackForFixing(), is(true));

        publishedInitiative.setFixState(FixState.REVIEW);
        assertThat(ManagementSettings.of(publishedInitiative).isAllowOmSendBackForFixing(), is(false));

        publishedInitiative.setFixState(FixState.FIX);
        assertThat(ManagementSettings.of(publishedInitiative).isAllowOmSendBackForFixing(), is(false));

    }

    @Test
    public void allowed_to_send_fix_to_review_if_fixState_is_FIX() {

        Initiative initiative = new Initiative();
        initiative.setFixState(FixState.REVIEW);

        precondition(ManagementSettings.of(initiative).isAllowSendFixToReview(), is(false));

        initiative.setFixState(FixState.FIX);

        assertThat(ManagementSettings.of(initiative).isAllowSendFixToReview(), is(true));
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


}
