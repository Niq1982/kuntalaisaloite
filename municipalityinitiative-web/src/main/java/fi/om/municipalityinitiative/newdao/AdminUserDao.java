package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.dto.user.User;

public interface AdminUserDao {
    public User getUser(String userName, String password);
}
