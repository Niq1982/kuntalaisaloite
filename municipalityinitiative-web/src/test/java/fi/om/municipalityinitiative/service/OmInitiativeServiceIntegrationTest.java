package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestFakeEmailConfiguration.class})
public class OmInitiativeServiceIntegrationTest {

    @Resource
    private OmInitiativeService omInitiativeService;

    @Resource
    private IntegrationTestConfiguration.FakeUserService fakeUserService;
}
