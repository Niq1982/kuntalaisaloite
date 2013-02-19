package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SearchParameterGeneratorTest {
    InitiativeSearch initiativeSearch;

    @Before
    public void setup() {
        initiativeSearch = new InitiativeSearch();
        initiativeSearch.setOffset(5);
        initiativeSearch.setLimit(10);
        initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.id);
        initiativeSearch.setShow(InitiativeSearch.Show.collecting);
        initiativeSearch.setMunicipality(1L);
        initiativeSearch.setSearch("pattern");
    }

    @Test
    public void generatesAllFieldsAsRequired() {
        String parameters = SearchParameterGenerator.generateParameters(initiativeSearch);
        assertThat(parameters, is("?offset=5&limit=10&orderBy=id&show=collecting&municipality=1&search=pattern"));
    }

    @Test
    public void changes_limit() {
        String parameters = new SearchParameterGenerator(initiativeSearch).getWithLimit(17);
        assertThat(parameters, is("?offset=5&limit=17&orderBy=id&show=sent&municipality=1&search=pattern"));
    }

    @Test
    public void changes_offset() {
        String parameters = new SearchParameterGenerator(initiativeSearch).getWithOffset(12);
        assertThat(parameters, is("?offset=12&limit=10&orderBy=id&show=sent&municipality=1&search=pattern"));
    }

    @Test
    public void orderBy_id() {
        initiativeSearch.setOrderBy(null);
        String parameters = new SearchParameterGenerator(initiativeSearch).getWithOrderById();
        assertThat(parameters, is("?offset=5&limit=10&orderBy=id&show=sent&municipality=1&search=pattern"));
    }

    @Test
    public void orderBy_oldestSent() {
        String parameters = new SearchParameterGenerator(initiativeSearch).getWithOrderByOldestSent();
        assertThat(parameters, is("?offset=5&limit=10&orderBy=oldestSent&show=sent&municipality=1&search=pattern"));
    }

    @Test
    public void orderBy_latestSent() {
        String parameters = new SearchParameterGenerator(initiativeSearch).getWithOrderByLatestSent();
        assertThat(parameters, is("?offset=5&limit=10&orderBy=latestSent&show=sent&municipality=1&search=pattern"));
    }

    @Test
    public void get_with_show_only_sent() {
        initiativeSearch.setShow(InitiativeSearch.Show.collecting);
        String parameters = new SearchParameterGenerator(initiativeSearch).getWithStateSent();
        assertThat(parameters, is("?orderBy=latestSent&show=sent"));
    }

}
