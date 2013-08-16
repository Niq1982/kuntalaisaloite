package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.service.ui.PublicInitiativeService;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
@Ignore
public class EfficiencyTest {

    public static final int THREAD_COUNT = 100;

    private static Long municipality;

    @Resource
    private PublicInitiativeService service;

    @Resource
    private TestHelper testHelper;

    @Resource
    PostgresQueryFactory queryFactory;

    private AtomicInteger currentlyMiningDatabase = new AtomicInteger(0);
    private Long initiativeCount;

    @Before
    @Transactional
    public void setup() {

        if (testHelper.countAll(QMunicipalityInitiative.municipalityInitiative) < 10000) {
            testHelper.dbCleanup();
            municipality = testHelper.createTestMunicipality("Municipality");

            System.out.println("Creating initiatives for test...");
            for (int i = 0; i < 5000; ++i) {
                testHelper.simpleCreate(municipality, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE, DateTime.now().minusDays(i));
                testHelper.simpleCreate(municipality, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE, DateTime.now().minusDays(i));
                testHelper.simpleCreate(municipality, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE, DateTime.now().minusDays(i));
                testHelper.simpleCreate(municipality, InitiativeState.DRAFT, InitiativeType.UNDEFINED, DateTime.now().minusDays(i));
                testHelper.simpleCreate(municipality, InitiativeState.PUBLISHED, InitiativeType.SINGLE, DateTime.now().minusDays(i));
                testHelper.simpleCreate(municipality, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE_COUNCIL, DateTime.now().minusDays(i));
            }
            System.out.println("Done. InitiativeCount: " + initiativeCount);

        }
        initiativeCount = testHelper.countAll(QMunicipalityInitiative.municipalityInitiative);
    }



    @Test
    public void massive_initiative_list() {

        List<Callable<List<InitiativeListInfo>>> threads = Lists.newArrayList();

        System.out.println("Beginning");
        for (int i = 0; i < THREAD_COUNT; ++i) {
            threads.add(new Callable<List<InitiativeListInfo>>() {
                @Override
                public List<InitiativeListInfo> call() throws Exception {
//                    currentlyMiningDatabase.incrementAndGet();
                    long l = System.currentTimeMillis();
                    List<InitiativeListInfo> initiatives = service.findMunicipalityInitiatives(new InitiativeSearch(), new LoginUserHolder<>(User.anonym()));
                    System.out.println("Duration: " + (System.currentTimeMillis() - l));

//                    System.out.println(currentlyMiningDatabase);
//                    currentlyMiningDatabase.decrementAndGet();

                    return initiatives;
                }
            });
        }


        ExecutorService executor = Executors.newCachedThreadPool();

        long l = System.currentTimeMillis();
        try {
            for (Future<List<InitiativeListInfo>> future : executor.invokeAll(threads)) {
                future.get();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdownNow();
        }

        System.out.println("Done. Initiatives: "+initiativeCount+", Threads:" + THREAD_COUNT);
        System.out.println("Duration in ms: " + (System.currentTimeMillis() - l));

    }
}
