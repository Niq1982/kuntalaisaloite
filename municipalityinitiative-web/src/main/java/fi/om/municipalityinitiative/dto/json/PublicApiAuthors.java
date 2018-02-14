package fi.om.municipalityinitiative.dto.json;

import fi.om.municipalityinitiative.dto.Author;
import java.util.List;

public class PublicApiAuthors {
    private int publicNameCount = 0;
    private int privateNameCount = 0;

    public PublicApiAuthors(List<? extends Author> allAuthors) {
        for (Author author : allAuthors) {
            if (author.getContactInfo().isShowName()) {
                ++publicNameCount;
            } else {
                ++privateNameCount;
            }
        }
    }

    public int getPublicNameCount() {
        return publicNameCount;
    }

    public int getPrivateNameCount() {
        return privateNameCount;
    }
}
