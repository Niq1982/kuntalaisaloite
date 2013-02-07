package fi.om.municipalityinitiative.newdto.email;

import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Test;

public class CollectableInitiativeEmailInfoTest {

    @Test
    public void constructor_fills_all_fields() {
        CollectableInitiativeEmailInfo original = ReflectionTestUtils.modifyAllFields(new CollectableInitiativeEmailInfo());
        CollectableInitiativeEmailInfo copy = new CollectableInitiativeEmailInfo(original);

        ReflectionTestUtils.assertNoNullFields(copy);
        ReflectionTestUtils.assertReflectionEquals(original, copy);

    }

}
