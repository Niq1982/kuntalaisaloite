package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.ui.ParticipantNames;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class ParticipantServiceTest {

    private static final Long ID = -5L;
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
        participants.add(new Participant("HasFranchise Foo", true));
        participants.add(new Participant("HasFranchise Bar", true));
        participants.add(new Participant("HasNoFranchise Winamp", false));

        stub(participantDaoMock.findPublicParticipants(ID)).toReturn(participants);

        ParticipantNames result = participantService.findParticipants(ID);

        assertThat(result.getFranchise().size(), is(2));
        assertThat(result.getNoFranchise().size(), is(1));

        assertThat(result.getFranchise(), containsInAnyOrder("HasFranchise Foo", "HasFranchise Bar"));
        assertThat(result.getNoFranchise(), contains("HasNoFranchise Winamp"));

    }

}
