package fi.om.municipalityinitiative.newdto.service;

import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ManagementSettingsTest {

    @Test
    public void edit_is_allowed_only_if_initiative_draft() {

        for (InitiativeState state : InitiativeState.values()) {

            Initiative initiative = createInitiative();
            initiative.setState(state);
            if (state.equals(InitiativeState.DRAFT)) {
                assertThat(state.toString(), managementSettings(initiative).isAllowEdit(), is(true));
            }
            else {
                assertThat(state.toString(), managementSettings(initiative).isAllowEdit(), is(false));
            }
        }
    }

    @Test
    public void update_is_allowed_only_when_not_draft() {

        for (InitiativeState state : InitiativeState.values()) {

            Initiative initiative = createInitiative();
            initiative.setState(state);
            if (state.equals(InitiativeState.DRAFT)) {
                assertThat(state.toString(), managementSettings(initiative).isAllowUpdate(), is(false));
            }
            else {
                assertThat(state.toString(), managementSettings(initiative).isAllowUpdate(), is(true));
            }
        }
    }

    @Test
    public void update_is_not_allowed_if_sent_to_municipality() {

        Initiative initiative = createInitiative();
        initiative.setState(InitiativeState.PUBLISHED);

        precondition(managementSettings(initiative).isAllowUpdate(), is(true));
        initiative.setSentTime(Maybe.<LocalDate>of(new LocalDate(2010, 1, 1)));
        assertThat(managementSettings(initiative).isAllowUpdate(), is(false));

    }

    @Test
    public void may_be_sent_to_municipality_only_if_at_least_accepted_and_not_sent() {

        for (InitiativeState state : InitiativeState.values()) {

            Initiative initiative = createInitiative();
            initiative.setState(state);
            if (state.equals(InitiativeState.ACCEPTED) || state.equals(InitiativeState.PUBLISHED)) {
                assertThat(state.toString(), managementSettings(initiative).isAllowSendToMunicipality(), is(true));
            } else {
                assertThat(state.toString(), managementSettings(initiative).isAllowSendToMunicipality(), is(false));
            }

            initiative.setSentTime(Maybe.of(new LocalDate(2010, 1, 1)));
            assertThat("Always false if sent", managementSettings(initiative).isAllowSendToMunicipality(), is(false));
        }
    }

    private static Initiative createInitiative() {
        Initiative initiative = new Initiative();
        return initiative;
    }

    private static ManagementSettings managementSettings(Initiative initiative) {
        return new ManagementSettings(initiative);

    }


}
