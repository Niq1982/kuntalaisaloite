package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Test;

public class PrepareSafeInitiativeUICreateDtoTest {

    @Test
    public void parse_fills_all_fields() {
        PrepareSafeInitiativeUICreateDto parsed = PrepareSafeInitiativeUICreateDto.parse(ReflectionTestUtils.modifyAllFields(new PrepareInitiativeUICreateDto()));
        ReflectionTestUtils.assertNoNullFields(parsed);

    }
}
