package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.user.User;

public interface AdminUserDao {
    public User getUser(String userName, String password);
}
