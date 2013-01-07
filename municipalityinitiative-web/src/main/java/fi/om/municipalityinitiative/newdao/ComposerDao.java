package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.ComposerCreateDto;
import fi.om.municipalityinitiative.newdto.SupportCount;

public interface ComposerDao {

    Long add(ComposerCreateDto createDto);

    SupportCount countSupports(Long municipalityId);
}
