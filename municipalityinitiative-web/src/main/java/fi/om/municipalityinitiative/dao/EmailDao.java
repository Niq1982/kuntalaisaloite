package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.service.EmailDto;

public interface EmailDao {

    Long addEmail(EmailDto emailDto);
}
