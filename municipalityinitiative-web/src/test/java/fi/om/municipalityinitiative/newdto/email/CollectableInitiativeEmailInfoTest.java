package fi.om.municipalityinitiative.newdto.email;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.newdto.service.PublicParticipant;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CollectableInitiativeEmailInfoTest {

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
        CollectableInitiativeEmailInfo collectableInitiativeEmailInfo = CollectableInitiativeEmailInfo.parse(initiativeEmailInfo, "comment", Lists.<PublicParticipant>newArrayList());

        assertThat(collectableInitiativeEmailInfo.getComment(), is("comment"));
        ReflectionTestUtils.assertNoNullFields(collectableInitiativeEmailInfo);
    }

}
