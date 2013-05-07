package fi.om.municipalityinitiative.newdto.ui;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.newdto.Author;

import java.util.Collections;
import java.util.List;

public class Authors {

    private List<AuthorInfo> publicAuthors = Lists.newArrayList();
    private int privateNames = 0;

    public Authors(List<Author> allAuthors) {
        for (Author author : allAuthors) {
            if (author.getContactInfo().isShowName()) {
                AuthorInfo authorInfo = new AuthorInfo();
                authorInfo.setName(author.getContactInfo().getName());
                authorInfo.setMunicipality(author.getMunicipality());
                publicAuthors.add(authorInfo);
            }
            else ++privateNames;
        }
    }

    public List<AuthorInfo> getPublicAuthors() {
        return Collections.unmodifiableList(publicAuthors);
    }

    public int getPublicNames() {
        return publicAuthors.size();
    }

    public int getPrivateNames() {
        return privateNames;
    }

}
