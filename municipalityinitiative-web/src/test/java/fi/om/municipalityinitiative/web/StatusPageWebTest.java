package fi.om.municipalityinitiative.web;

import org.junit.Test;

public class StatusPageWebTest extends WebTestBase {

    @Override
    protected boolean overrideDriverToHtmlUnit() {
        return true;
    }

    @Test
    public void status_page_opens() {
        open(urls.getStatusPage());
        assertTitle("Status page - Kuntalaisaloitepalvelu");
    }
}
