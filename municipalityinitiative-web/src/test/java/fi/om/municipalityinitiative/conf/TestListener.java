package fi.om.municipalityinitiative.conf;

import fi.om.municipalityinitiative.web.WebTestBase;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

public class TestListener extends RunListener {
    @Override
    public void testRunStarted(Description description) throws Exception {
    }

    private void lines() {
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        WebTestBase.destroyDriver();
    }
}
