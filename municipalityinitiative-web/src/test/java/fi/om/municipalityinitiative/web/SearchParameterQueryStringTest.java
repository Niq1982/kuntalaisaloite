package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.service.Municipality;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SearchParameterQueryStringTest {
    InitiativeSearch initiativeSearch;


    @Resource
    private TestHelper testHelper;

    @Before
    public void setup() {
        initiativeSearch = new InitiativeSearch();
        initiativeSearch.setOffset(5);
        initiativeSearch.setLimit(10);
        initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.id);
        initiativeSearch.setShow(InitiativeSearch.Show.collecting);
        initiativeSearch.setType(InitiativeSearch.Type.citizen);
        initiativeSearch.setMunicipalities(1L);
        initiativeSearch.setSearch("pattern");
    }


    @Test
    public void generatesAllFieldsAsRequired() {
        String parameters = SearchParameterQueryString.generateParameters(initiativeSearch);
        assertThat(parameters, is("?offset=5&limit=10&orderBy=id&show=collecting&municipalities=1&search=pattern&type=citizen"));
    }

    @Test
    public void changes_limit_and_clears_offset() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithLimit(17);
        assertThat(parameters, is("?offset=0&limit=17&orderBy=id&show=collecting&municipalities=1&search=pattern&type=citizen"));
    }

    @Test
    public void changes_offset() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithOffset(12);
        assertThat(parameters, is("?offset=12&limit=10&orderBy=id&show=collecting&municipalities=1&search=pattern&type=citizen"));
    }

    @Test
    public void orderBy_id() {
        initiativeSearch.setOrderBy(null);
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithOrderById();
        assertThat(parameters, is("?offset=5&limit=10&orderBy=id&show=collecting&municipalities=1&search=pattern&type=citizen"));
    }

    @Test
    public void orderBy_oldestSent() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithOrderByOldestSent();
        assertThat(parameters, is("?offset=5&limit=10&orderBy=oldestSent&show=collecting&municipalities=1&search=pattern&type=citizen"));
    }

    @Test
    public void orderBy_latestSent() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithOrderByLatestSent();
        assertThat(parameters, is("?offset=5&limit=10&orderBy=latestSent&show=collecting&municipalities=1&search=pattern&type=citizen"));
    }

    @Test
    public void orderBy_leastParticipants() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithOrderByLeastParticipants();
        assertThat(parameters, is("?offset=5&limit=10&orderBy=leastParticipants&show=collecting&municipalities=1&search=pattern&type=citizen"));
    }

    @Test
    public void orderBy_mostParticipants() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithOrderByMostParticipants();
        assertThat(parameters, is("?offset=5&limit=10&orderBy=mostParticipants&show=collecting&municipalities=1&search=pattern&type=citizen"));
    }

    @Test
    public void orderBy_latest() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithOrderByLatest();
        assertThat(parameters, is("?offset=5&limit=10&orderBy=latest&show=collecting&municipalities=1&search=pattern&type=citizen"));
    }

    @Test
    public void orderBy_oldest() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithOrderByOldest();
        assertThat(parameters, is("?offset=5&limit=10&orderBy=oldest&show=collecting&municipalities=1&search=pattern&type=citizen"));
    }

    @Test
    public void get_with_show_only_sent() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithStateSent();
        assertThat(parameters, is("?orderBy=latestSent&show=sent&municipalities=1&type=citizen"));
    }

    @Test
    public void get_with_show_only_collecting() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithStateCollecting();
        assertThat(parameters, is("?orderBy=latest&show=collecting&municipalities=1&type=citizen"));
    }

    @Test
    public void get_with_show_all() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithStateAll();
        assertThat(parameters, is("?orderBy=latest&show=all&municipalities=1&type=citizen"));
    }

    @Test
    public void get_with_show_only_draft() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithStateDraft();
        assertThat(parameters, is("?orderBy=latest&show=draft&municipalities=1&type=citizen"));
    }

    @Test
    public void get_with_show_only_accepted() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithStateAccepted();
        assertThat(parameters, is("?orderBy=latest&show=accepted&municipalities=1&type=citizen"));
    }

    @Test
    public void get_with_show_only_review() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithStateReview();
        assertThat(parameters, is("?orderBy=latest&show=review&municipalities=1&type=citizen"));
    }

    @Test
    public void get_with_show_only_fix() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithStateFix();
        assertThat(parameters, is("?orderBy=latest&show=fix&municipalities=1&type=citizen"));
    }

    @Test
    public void get_with_type_all() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithTypeAll();
        assertThat(parameters, is("?orderBy=latest&show=collecting&municipalities=1&type=all"));
    }

    @Test
    public void get_with_type_normal() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithTypeNormal();
        assertThat(parameters, is("?orderBy=latest&show=collecting&municipalities=1&type=normal"));
    }

    @Test
    public void get_with_type_council() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithTypeCouncil();
        assertThat(parameters, is("?orderBy=latest&show=collecting&municipalities=1&type=council"));
    }

    @Test
    public void get_with_type_citizen() {
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithTypeCitizen();
        assertThat(parameters, is("?orderBy=latest&show=collecting&municipalities=1&type=citizen"));
    }
    @Test
    public void generatesAllFields_with_two_municipalities() {
        ArrayList<Long> municipalities = new ArrayList<Long>();
        municipalities.add(1L);
        municipalities.add(4L);
        initiativeSearch.setMunicipalities(municipalities);
        String parameters = SearchParameterQueryString.generateParameters(initiativeSearch);
        assertThat(parameters, is("?offset=5&limit=10&orderBy=id&show=collecting&municipalities=1,4&search=pattern&type=citizen"));
    }
    @Test
    public void generatesAllFields_with_three_municipalities() {
        ArrayList<Long> municipalities = new ArrayList<Long>();
        municipalities.add(1L);
        municipalities.add(4L);
        municipalities.add(5L);
        initiativeSearch.setMunicipalities(municipalities);
        String parameters = SearchParameterQueryString.generateParameters(initiativeSearch);
        assertThat(parameters, is("?offset=5&limit=10&orderBy=id&show=collecting&municipalities=1,4,5&search=pattern&type=citizen"));
    }

    @Test
    public void get_with_municipality_ids(){
        ArrayList<Long> municipalities = new ArrayList<Long>();
        municipalities.add(1L);
        municipalities.add(4L);
        municipalities.add(5L);
        initiativeSearch.setMunicipalities(municipalities);
        String parameters = new SearchParameterQueryString(initiativeSearch).getWithMunicipalityIds(municipalities);
        assertThat(parameters, is("?orderBy=latest&show=all&municipalities=1,4,5&type=all"));

    }

    @Test
    public void get_with_municipalities(){
        ArrayList<Municipality> municipalities = new ArrayList<Municipality>();
        Municipality testMunicipality = new Municipality(1L, "fi", "sv", true);;
        Municipality testMunicipality2 = new Municipality(4L, "fi", "sv", true);;
        Municipality testMunicipality3 = new Municipality(5L, "fi", "sv", true);;


        municipalities.add(testMunicipality);
        municipalities.add(testMunicipality2);
        municipalities.add(testMunicipality3);

        String parameters = new SearchParameterQueryString(initiativeSearch).getWithMunicipalities(municipalities);
        assertThat(parameters, is("?orderBy=latest&show=all&municipalities=1,4,5&type=all"));

    }


}
