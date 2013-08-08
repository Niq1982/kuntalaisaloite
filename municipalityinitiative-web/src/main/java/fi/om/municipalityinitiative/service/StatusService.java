package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.service.StatusServiceImpl.KeyValueInfo;

import java.util.List;

public interface StatusService {

    List<KeyValueInfo> getSystemInfo();

    List<KeyValueInfo> getSchemaVersionInfo();

    List<KeyValueInfo> getApplicationInfo();

    List<KeyValueInfo> getConfigurationInfo();

    List<KeyValueInfo> getConfigurationTestInfo();

    String getAppVersion();
}