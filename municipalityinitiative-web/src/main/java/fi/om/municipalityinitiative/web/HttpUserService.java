package fi.om.municipalityinitiative.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fi.om.municipalityinitiative.dto.LocalizedString;
import fi.om.municipalityinitiative.dto.User;
import fi.om.municipalityinitiative.service.UserService;

public interface HttpUserService extends UserService {

    User login(String ssn, String firstName, String lastName,
            boolean finnishCitizen, LocalizedString homeMunicipality,
            HttpServletRequest request, HttpServletResponse response);

    void verifyCSRFToken(HttpServletRequest request);

    void prepareForLogin(HttpServletRequest request);

    void logout(HttpServletRequest request, HttpServletResponse response);

}
