package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.user.User;

public interface UserDao {
    public User getUser(String userName, String password);
}
