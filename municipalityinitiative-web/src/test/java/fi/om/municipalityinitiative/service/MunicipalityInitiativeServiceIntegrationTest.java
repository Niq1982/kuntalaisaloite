package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.newdto.InitiativeViewInfo;
import fi.om.municipalityinitiative.newdto.MunicipalityInfo;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.newweb.MunicipalityInitiativeUICreateDto;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import fi.om.municipalityinitiative.util.TestUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
public class MunicipalityInitiativeServiceIntegrationTest {

    @Resource
    private MunicipalityInitiativeService service;

    @Resource
    TestHelper testHelper;

    private MunicipalityInfo testMunicipality;

    @Before
    public void setup() {
        testHelper.dbCleanup();
        testMunicipality = new MunicipalityInfo();
        testMunicipality.setName("Test municipality");
        testMunicipality.setId(testHelper.createTestMunicipality(testMunicipality.getName()));
    }


    @Test
    public void create() {
        service.addMunicipalityInitiative(createDto());
        assertThat(testHelper.countAll(QMunicipalityInitiative.municipalityInitiative), is(1L));
    }

    @Test
    public void create_and_get() {
        MunicipalityInitiativeUICreateDto createDto = createDto();
        Long initiativeId = service.addMunicipalityInitiative(createDto);
        InitiativeViewInfo initiative = service.getMunicipalityInitiative(initiativeId);

        assertThat(initiative.getId(), is(initiativeId));
        assertThat(initiative.getAuthorName(), is(createDto.getContactName()));
        assertThat(initiative.getName(), is(createDto.getName()));
        assertThat(initiative.getProposal(), is(createDto.getProposal()));
        assertThat(initiative.isShowName(), is(createDto.getShowName()));
        assertThat(initiative.getMunicipalityName(), is(testMunicipality.getName()));

        assertThat(initiative.getCreateTime(), is(notNullValue()));

        // TODO: Verify all other values somehow

    }

    private MunicipalityInitiativeUICreateDto createDto() {
        MunicipalityInitiativeUICreateDto createDto = new MunicipalityInitiativeUICreateDto();
        createDto.setContactAddress("contact address " +randomString());
        createDto.setContactPhone("contact phone " +randomString());
        createDto.setContactName("contact name " + randomString());
        createDto.setContactEmail("contact email " + randomString());
        createDto.setProposal("Proposal " + randomString());
        createDto.setName("Name " + randomString());
        createDto.setFranchise(true);
        createDto.setShowName(true);
        createDto.setMunicipality(testMunicipality.getId());
        createDto.setHomeMunicipality(testMunicipality.getId());
        createDto.setMunicipalMembership(true);

        TestUtils.assertNoNullFields(createDto);
        return createDto;
    }


    private static String randomString() {
        return String.valueOf(new Random().nextInt());
    }

    private void assertCreateAndGetDtos(MunicipalityInitiativeCreateDto create, InitiativeViewInfo get) {
        Assert.assertThat(get.getProposal(), CoreMatchers.is(create.proposal));
        Assert.assertThat(get.getName(), CoreMatchers.is(create.name));
        Assert.assertThat(get.getAuthorName(), CoreMatchers.is(create.contactName));
        //Assert.assertThat(get.getMunicipalityName(), CoreMatchers.is(testMunicipality.getName()));
        Assert.assertThat(get.getCreateTime(), CoreMatchers.is(CoreMatchers.notNullValue()));
        Assert.assertThat(get.getId(), CoreMatchers.is(CoreMatchers.notNullValue()));
    }

}
