package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dto.User;


/**
 * This interface contains only methods needed in services. Actual login and logout are
 * implementation specific details. 
 * 
 * @author samppasa
 */
public interface UserService {

    User getCurrentUser();
    
    User getCurrentUser(boolean verifyCSRF);

    User currentAsRegisteredUser();

    User getUserInRole(Role... roles);

    void requireUserInRole(Role... roles);

}