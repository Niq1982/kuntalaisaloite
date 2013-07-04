package fi.om.municipalityinitiative.dto.ui;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.util.Maybe;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class PublicAuthorsTest {

    @Test
    public void strips_authors_according_to_showName() {

        List<Author> authorList = Lists.newArrayList();

        Author publicAuthor = new Author();
        publicAuthor.setContactInfo(new ContactInfo());
        publicAuthor.getContactInfo().setName("Public Name");
        publicAuthor.getContactInfo().setShowName(true);
        publicAuthor.setMunicipality(Maybe.of(new Municipality(1L, null, null, false)));
        authorList.add(publicAuthor);

        Author privateAuthor = new Author();
        privateAuthor.setContactInfo(privateContactInfo());
        authorList.add(privateAuthor);
        authorList.add(privateAuthor);

        PublicAuthors publicAuthors = new PublicAuthors(authorList);
        assertThat(publicAuthors.getPrivateNameCount(), is(2));
        assertThat(publicAuthors.getPublicNameCount(), is(1));
        assertThat(publicAuthors.getPublicAuthors(), hasSize(1));
        assertThat(publicAuthors.getPublicAuthors().get(0).getMunicipality().get().getId(), is(1L));
        assertThat(publicAuthors.getPublicAuthors().get(0).getName(), is("Public Name"));
    }

    private static ContactInfo privateContactInfo() {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setShowName(false);
        return contactInfo;
    }
}
