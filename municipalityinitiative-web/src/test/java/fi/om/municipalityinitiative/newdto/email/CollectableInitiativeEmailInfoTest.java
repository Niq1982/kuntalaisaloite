package fi.om.municipalityinitiative.newdto.email;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class CollectableInitiativeEmailInfoTest {

    private static final Municipality MUNICIPALITY = new Municipality("Municipality", 1L);

    @Test
    public void constructor_fills_all_fields() {
        CollectableInitiativeEmailInfo original = ReflectionTestUtils.modifyAllFields(new CollectableInitiativeEmailInfo());
        CollectableInitiativeEmailInfo copy = new CollectableInitiativeEmailInfo(original);

        ReflectionTestUtils.assertNoNullFields(copy);
        ReflectionTestUtils.assertReflectionEquals(original, copy);
    }

    @Test
    public void parse_fills_all_fields() {
        InitiativeEmailInfo initiativeEmailInfo = ReflectionTestUtils.modifyAllFields(new InitiativeEmailInfo());
        
        CollectableInitiativeEmailInfo collectableInitiativeEmailInfo = CollectableInitiativeEmailInfo.parse(initiativeEmailInfo, "comment", Lists.<Participant>newArrayList());

        assertThat(collectableInitiativeEmailInfo.getComment(), is("comment"));
        
        ReflectionTestUtils.assertNoNullFields(collectableInitiativeEmailInfo);
    }
    
    @Test
    public void parses_participants_according_to_franchise() {
        
        InitiativeEmailInfo initiativeEmailInfo = new InitiativeEmailInfo();
        
        List<Participant> participants = Lists.newArrayList();
        
        participants.add(new Participant(new LocalDate(2010, 1, 1), "FranchiseGuy Name", true, MUNICIPALITY));
        participants.add(new Participant(new LocalDate(2010, 1, 1), "NoFranchiseGuy Name", false, MUNICIPALITY));
        
        CollectableInitiativeEmailInfo collectableInitiativeEmailInfo = CollectableInitiativeEmailInfo.parse(initiativeEmailInfo, "comment", participants);

        assertThat(collectableInitiativeEmailInfo.getParticipantsFranchise(), hasSize(1));
        assertThat(collectableInitiativeEmailInfo.getParticipantsFranchise().get(0).getName(), is("FranchiseGuy Name"));
        
        assertThat(collectableInitiativeEmailInfo.getParticipantsNoFranchise(), hasSize(1));
        assertThat(collectableInitiativeEmailInfo.getParticipantsNoFranchise().get(0).getName(), is("NoFranchiseGuy Name"));
        
        
        
    }

}
