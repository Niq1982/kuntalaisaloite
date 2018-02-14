package fi.om.municipalityinitiative.dto.json;

public class PublicAuthorsJson {

    private PublicApiAuthors publicAuthors;

    public PublicAuthorsJson(PublicApiAuthors publicAuthors) {
        this.publicAuthors = publicAuthors;
    }

    public int getPublicNames() {
        return publicAuthors.getPublicNameCount();
    }

    public int getPrivateNames() {
        return publicAuthors.getPrivateNameCount();
    }
}
