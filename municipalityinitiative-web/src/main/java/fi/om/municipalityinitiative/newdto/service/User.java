package fi.om.municipalityinitiative.newdto.service;

public class User {

    private final boolean omUser;

    public User(boolean omUser) {
        this.omUser = omUser;
    }

    public static User omUser() {
        return new User(true);
    }

    public boolean isOmUser() {
        return omUser;
    }

    public static User normalUser() {
        return new User(false);
    }
}
