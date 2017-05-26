package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Test;

public class PrepareVerifiedInitiativeUICreateDtoTest {

    @Test
    public void parse_fills_all_fields() {
        PrepareVerifiedInitiativeUICreateDto parsed = PrepareVerifiedInitiativeUICreateDto.parse(ReflectionTestUtils.modifyAllFields(new PrepareInitiativeUICreateDto()));
        ReflectionTestUtils.assertNoNullFields(parsed);

    }
}
