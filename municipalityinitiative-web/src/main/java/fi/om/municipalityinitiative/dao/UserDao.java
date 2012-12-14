package fi.om.municipalityinitiative.dao;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import fi.om.municipalityinitiative.dto.User;

public interface UserDao {

    Long register(String ssnHash, DateTime lastLogin, String firstNames, String lastName, LocalDate dateOfBirth);

    User loginRegisteredUser(String ssnHash);

    void setUserRoles(Long id, boolean vrk, boolean om);

}