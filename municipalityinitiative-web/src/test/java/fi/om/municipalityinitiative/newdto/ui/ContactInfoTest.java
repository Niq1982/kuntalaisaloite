package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Test;

public class ContactInfoTest {

    @Test
    public void constructor_fills_all_fields() {
        ContactInfo original = ReflectionTestUtils.modifyAllFields(new ContactInfo());
        ContactInfo copy = new ContactInfo(original);
        ReflectionTestUtils.assertNoNullFields(copy);
        ReflectionTestUtils.assertReflectionEquals(copy, original);
    }

}
