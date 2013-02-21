package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.json.Municipality;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.ui.Participants;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class ParticipantServiceTest {

    private static final Long ID = -5L;
    public static final LocalDate DATE = new LocalDate(2010, 1, 1);
    private static final Municipality MUNICIPALITY = new Municipality("Municipality", 1L);
    private ParticipantDao participantDaoMock;
    private ParticipantService participantService;

    @Before
    public void setup() {
        participantDaoMock = mock(ParticipantDao.class);
        participantService = new ParticipantService(participantDaoMock);
    }

    @Test
    public void parses_participants_according_to_franchise() {

        List<Participant> participants = Lists.newArrayList();
        participants.add(new Participant(DATE, "HasFranchise Foo", true, MUNICIPALITY));
        participants.add(new Participant(DATE, "HasFranchise Bar", true,MUNICIPALITY));
        participants.add(new Participant(DATE, "HasNoFranchise Winamp", false,MUNICIPALITY));

        stub(participantDaoMock.findPublicParticipants(ID)).toReturn(participants);

        Participants result = participantService.findPublicParticipants(ID);

        assertThat(result.getFranchise().size(), is(2));
        assertThat(result.getNoFranchise().size(), is(1));

        assertThat(result.getFranchise().get(0).getName(), is("HasFranchise Foo")); // Might be in another order?
        assertThat(result.getFranchise().get(1).getName(), is("HasFranchise Bar"));

        assertThat(result.getNoFranchise().get(0).getName(), is("HasNoFranchise Winamp"));

    }

}
