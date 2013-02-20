package fi.om.municipalityinitiative.newdto;

import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Test;

public class InitiativeSearchTest {

    @Test
    public void copy_fills_all_fields() {
        InitiativeSearch original = ReflectionTestUtils.modifyAllFields(new InitiativeSearch());

        InitiativeSearch copy = original.copy();

        ReflectionTestUtils.assertNoNullFields(copy);
        ReflectionTestUtils.assertReflectionEquals(original, copy);
    }
}
