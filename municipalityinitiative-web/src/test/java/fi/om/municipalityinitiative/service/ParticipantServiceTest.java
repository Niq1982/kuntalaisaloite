package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.service.PublicParticipant;
import fi.om.municipalityinitiative.newdto.ui.ParticipantNames;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class ParticipantServiceTest {

    private static final Long ID = -5L;
    public static final LocalDate DATE = new LocalDate(2010, 1, 1);
    private ParticipantDao participantDaoMock;
    private ParticipantService participantService;

    @Before
    public void setup() {
        participantDaoMock = mock(ParticipantDao.class);
        participantService = new ParticipantService(participantDaoMock);
    }

    @Test
    public void parses_participants_according_to_franchise() {

        List<PublicParticipant> publicParticipants = Lists.newArrayList();
        publicParticipants.add(new PublicParticipant(DATE, "HasFranchise Foo", true));
        publicParticipants.add(new PublicParticipant(DATE, "HasFranchise Bar", true));
        publicParticipants.add(new PublicParticipant(DATE, "HasNoFranchise Winamp", false));

        stub(participantDaoMock.findPublicParticipants(ID)).toReturn(publicParticipants);

        ParticipantNames result = participantService.findPublicParticipants(ID);

        assertThat(result.getFranchise().size(), is(2));
        assertThat(result.getNoFranchise().size(), is(1));

        assertThat(result.getFranchise(), containsInAnyOrder("HasFranchise Foo", "HasFranchise Bar"));
        assertThat(result.getNoFranchise(), contains("HasNoFranchise Winamp"));

    }

}
