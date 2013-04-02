package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.service.User;

public interface UserDao {
    public User getUser(String userName, String password);
}
