package fi.om.municipalityinitiative.service.ui;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class NotificationTest {


    @Test
    public void survices_null() {
        assertThat(new Notification().getLink(), is(nullValue()));
    }

    @Test
    public void adds_http() {

        assertLinkConversion("www.google.com", "http://www.google.com");
        assertLinkConversion("google.com", "http://google.com");
    }

    @Test
    public void does_not_add_http_if_some_protocol_exists() {

        assertLinkConversion("https://www.example.com", "https://www.example.com");
        assertLinkConversion("http://www.example.com", "http://www.example.com");

    }

    private void assertLinkConversion(final String link, String value) {
        assertThat(
                new Notification(){{
                    setLink(link);
                }}.getLink(),
                is(value)
        );
    }

}