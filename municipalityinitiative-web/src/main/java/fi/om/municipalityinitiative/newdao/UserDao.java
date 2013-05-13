package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.service.LoginUser;

public interface UserDao {
    public LoginUser getUser(String userName, String password);
}
