package fi.om.municipalityinitiative.service;

import java.util.List;

import fi.om.municipalityinitiative.dto.InitiativeManagement;
import fi.om.municipalityinitiative.dto.User;

public interface TestDataService {

    void createTestUsersFromTemplates(List<User> userTemplates);

    void createTestInitiativesFromTemplates(List<InitiativeManagement> initiatives, User currentUser, String initiatorEmail, String reserveEmail);
}