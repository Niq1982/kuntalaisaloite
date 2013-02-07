package fi.om.municipalityinitiative.newdto.email;

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
        CollectableInitiativeEmailInfo collectableInitiativeEmailInfo = CollectableInitiativeEmailInfo.parse(initiativeEmailInfo, "comment");

        assertThat(collectableInitiativeEmailInfo.getComment(), is("comment"));
        ReflectionTestUtils.assertNoNullFields(collectableInitiativeEmailInfo);
    }

}
