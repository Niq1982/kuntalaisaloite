package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.newdto.InitiativeViewInfo;
import fi.om.municipalityinitiative.newdto.MunicipalityInfo;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeUICreateDto;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import fi.om.municipalityinitiative.util.TestUtils;
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
    public void createNotCollectable() {
        service.addMunicipalityInitiative(createDto(), false);
        assertThat(testHelper.countAll(QMunicipalityInitiative.municipalityInitiative), is(1L));
    }
    
    @Test
    public void createCollectable() {
        service.addMunicipalityInitiative(createDto(), true);
        assertThat(testHelper.countAll(QMunicipalityInitiative.municipalityInitiative), is(1L));
    }    

    @Test
    public void all_fields_are_set_when_getting_municipalityInitiativeInfo() {
        Long initiativeId = service.addMunicipalityInitiative(createDto(), true);
        InitiativeViewInfo initiative = service.getMunicipalityInitiative(initiativeId);
        TestUtils.assertNoNullFields(initiative);
    }

    @Test
    public void create_and_get() {
        MunicipalityInitiativeUICreateDto createDto = createDto();
        Long initiativeId = service.addMunicipalityInitiative(createDto, false);
        InitiativeViewInfo initiative = service.getMunicipalityInitiative(initiativeId);

        assertThat(initiative.getId(), is(initiativeId));
        assertThat(initiative.getAuthorName(), is(createDto.getContactName()));
        assertThat(initiative.getName(), is(createDto.getName()));
        assertThat(initiative.getProposal(), is(createDto.getProposal()));
        assertThat(initiative.isShowName(), is(createDto.getShowName()));
        assertThat(initiative.getMunicipalityName(), is(testMunicipality.getName()));

        assertThat(initiative.getCreateTime(), is(notNullValue()));
        assertThat(initiative.getManagementHash(), is(org.hamcrest.Matchers.nullValue()));

        // TODO: Verify all other values somehow

    }
    
    @Test
    public void creating_collectable_initiative_adds_hash() {
        MunicipalityInitiativeUICreateDto createDto = createDto();
        Long initiativeId = service.addMunicipalityInitiative(createDto, true);
        InitiativeViewInfo initiative = service.getMunicipalityInitiative(initiativeId);
        
        assertThat(initiative.getManagementHash(), is("0000000000111111111122222222223333333333"));

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

}
