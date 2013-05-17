package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.user.LoginUser;

public interface UserDao {
    public LoginUser getUser(String userName, String password);
}
