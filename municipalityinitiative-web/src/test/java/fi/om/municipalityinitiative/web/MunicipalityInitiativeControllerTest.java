package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.conf.ControllerTestConfiguration;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ControllerTestConfiguration.class})
public class MunicipalityInitiativeControllerTest {

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.annotationConfigSetup(ControllerTestConfiguration.class).build();
    }

    @Test
    @Ignore("Will not work. Fix when beginning to make controllers")
    public void moi() throws Exception {
        mockMvc.perform(get("/").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());

    }


}
