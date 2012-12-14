package fi.om.municipalityinitiative.service;

import java.util.List;

import fi.om.municipalityinitiative.service.StatusServiceImpl.KeyValueInfo;

public interface StatusService {

    List<KeyValueInfo> getSystemInfo();

    List<KeyValueInfo> getSchemaVersionInfo();

    List<KeyValueInfo> getApplicationInfo();

    List<KeyValueInfo> getConfigurationInfo();

    List<KeyValueInfo> getConfigurationTestInfo();

}