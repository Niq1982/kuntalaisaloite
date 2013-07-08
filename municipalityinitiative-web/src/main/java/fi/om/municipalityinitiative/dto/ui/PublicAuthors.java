package fi.om.municipalityinitiative.dto.ui;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dto.Author;

import java.util.Collections;
import java.util.List;

public class PublicAuthors {

    private List<AuthorInfo> publicAuthors = Lists.newArrayList();
    private int privateNameCount = 0;

    public PublicAuthors(List<? extends Author> allAuthors) {
        for (Author author : allAuthors) {
            if (author.getContactInfo().isShowName()) {
                AuthorInfo authorInfo = new AuthorInfo();
                authorInfo.setName(author.getContactInfo().getName());
                authorInfo.setMunicipality(author.getMunicipality());
                publicAuthors.add(authorInfo);
            }
            else ++privateNameCount;
        }
    }

    public List<AuthorInfo> getPublicAuthors() {
        return Collections.unmodifiableList(publicAuthors);
    }

    public int getPublicNameCount() {
        return publicAuthors.size();
    }

    public int getPrivateNameCount() {
        return privateNameCount;
    }

}
