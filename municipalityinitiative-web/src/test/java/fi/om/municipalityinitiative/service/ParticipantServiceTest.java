package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import org.joda.time.LocalDate;
import org.junit.Before;

import static org.mockito.Mockito.mock;

public class ParticipantServiceTest {

    private static final Long ID = -5L;
    public static final LocalDate DATE = new LocalDate(2010, 1, 1);
    private static final Municipality MUNICIPALITY = new Municipality(1, "Municipality", "Kommun", false);
    private ParticipantDao participantDaoMock;
    private ParticipantService participantService;

    @Before
    public void setup() {
        participantDaoMock = mock(ParticipantDao.class);
        participantService = new ParticipantService(participantDaoMock);
    }

}
