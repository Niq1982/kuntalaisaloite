package fi.om.municipalityinitiative.web;

import org.junit.Test;

public class StatusPageWebTest extends WebTestBase {

    @Override
    public void childSetup() {

    }
    @Test
    public void status_page_opens() {
        open(urls.getStatusPage());
        assertTitle("Status page - Kuntalaisaloitepalvelu");
    }
}
