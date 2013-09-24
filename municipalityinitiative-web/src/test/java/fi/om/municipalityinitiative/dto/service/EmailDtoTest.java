package fi.om.municipalityinitiative.dto.service;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EmailDtoTest {

    @Test
    public void splits_emails_to_list() {
        List<String> strings = EmailDto.parseEmails(" first@com ;; second@com;third@com ");
        assertThat(strings.get(0), is("first@com"));
        assertThat(strings.get(1), is("second@com"));
        assertThat(strings.get(2), is("third@com"));
    }

    @Test
    public void combines_emails_from_list() {

        ArrayList<String> emailStrings = Lists.newArrayList(" first@com;second@com", "third@com", "", " fourth@com ");
        String emails = EmailDto.emailsToString(emailStrings);
        assertThat(emails, is("first@com;second@com;third@com;fourth@com"));
    }



}
