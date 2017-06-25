package fi.om.municipalityinitiative.web;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.util.Locales;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SearchParameterQueryStringTest {

    public static final String BASE_URL = "http://localhost";
    InitiativeSearch initiativeSearch;

    @Resource
    private TestHelper testHelper;

    private Urls urls;
    private List<? extends Municipality> allMunicipalities;

    private Municipality HELSINKI = new Municipality(1L, "Helsinki", "Helsingfors", true);

    @BeforeClass
    public static void initUrls() {
        Urls.initUrls(BASE_URL, null, null, null, null, true);
    }

    @Before
    public void setup() {
        initiativeSearch = new InitiativeSearch();
        initiativeSearch.setOffset(5);
        initiativeSearch.setLimit(10);
        initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.id);
        initiativeSearch.setShow(InitiativeSearch.Show.collecting);
        initiativeSearch.setType(InitiativeSearch.Type.citizen);
        // initiativeSearch.setMunicipalities(1L);
        initiativeSearch.setSearch("pattern");

        urls = Urls.get(Locales.LOCALE_FI);

        allMunicipalities = Lists.newArrayList(HELSINKI);
    }

    @Test
    public void generatesAllFieldsAsRequired() {

        String parameters = SearchParameterQueryString.generateParameters(initiativeSearch, allMunicipalities, urls);
        assertThat(parameters, is(urls.search() + "?offset=5&limit=10&orderBy=id&show=collecting&search=pattern&type=citizen"));
    }

    @Test
    public void changes_limit_and_clears_offset() {
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithLimit(17);
        assertThat(parameters, is(urls.search() + "?offset=0&limit=17&orderBy=id&show=collecting&search=pattern&type=citizen"));
    }

    @Test
    public void changes_offset() {
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithOffset(12);
        assertThat(parameters, is(urls.search() + "?offset=12&limit=10&orderBy=id&show=collecting&search=pattern&type=citizen"));
    }

    @Test
    public void orderBy_id() {
        initiativeSearch.setOrderBy(null);
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithOrderById();
        assertThat(parameters, is(urls.search() + "?offset=5&limit=10&orderBy=id&show=collecting&search=pattern&type=citizen"));
    }

    @Test
    public void orderBy_oldestSent() {
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithOrderByOldestSent();
        assertThat(parameters, is(urls.search() + "?offset=5&limit=10&orderBy=oldestSent&show=collecting&search=pattern&type=citizen"));
    }

    @Test
    public void orderBy_latestSent() {
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithOrderByLatestSent();
        assertThat(parameters, is(urls.search() + "?offset=5&limit=10&orderBy=latestSent&show=collecting&search=pattern&type=citizen"));
    }

    @Test
    public void orderBy_leastParticipants() {
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithOrderByLeastParticipants();
        assertThat(parameters, is(urls.search() + "?offset=5&limit=10&orderBy=leastParticipants&show=collecting&search=pattern&type=citizen"));
    }

    @Test
    public void orderBy_mostParticipants() {
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithOrderByMostParticipants();
        assertThat(parameters, is(urls.search() + "?offset=5&limit=10&orderBy=mostParticipants&show=collecting&search=pattern&type=citizen"));
    }

    @Test
    public void orderBy_latest() {
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithOrderByLatest();
        assertThat(parameters, is(urls.search() + "?offset=5&limit=10&orderBy=latest&show=collecting&search=pattern&type=citizen"));
    }

    @Test
    public void orderBy_oldest() {
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithOrderByOldest();
        assertThat(parameters, is(urls.search() + "?offset=5&limit=10&orderBy=oldest&show=collecting&search=pattern&type=citizen"));
    }

    @Test
    public void get_with_show_only_sent() {
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithStateSent();
        assertThat(parameters, is(urls.search() + "?orderBy=latestSent&show=sent&type=citizen"));
    }

    @Test
    public void get_with_show_only_collecting() {
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithStateCollecting();
        assertThat(parameters, is(urls.search() + "?orderBy=latest&show=collecting&type=citizen"));
    }

    @Test
    public void get_with_show_all() {
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithStateAll();
        assertThat(parameters, is(urls.search() + "?orderBy=latest&show=all&type=citizen"));
    }

    @Test
    public void get_with_show_only_draft() {
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithStateDraft();
        assertThat(parameters, is(urls.search() + "?orderBy=latest&show=draft&type=citizen"));
    }

    @Test
    public void get_with_show_only_accepted() {
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithStateAccepted();
        assertThat(parameters, is(urls.search() + "?orderBy=latest&show=accepted&type=citizen"));
    }

    @Test
    public void get_with_show_only_review() {
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithStateReview();
        assertThat(parameters, is(urls.search() + "?orderBy=latest&show=review&type=citizen"));
    }

    @Test
    public void get_with_show_only_fix() {
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithStateFix();
        assertThat(parameters, is(urls.search() + "?orderBy=latest&show=fix&type=citizen"));
    }

    @Test
    public void get_with_type_all() {
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithTypeAll();
        assertThat(parameters, is(urls.search() + "?orderBy=latest&show=collecting&type=all"));
    }

    @Test
    public void get_with_type_normal() {
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithTypeNormal();
        assertThat(parameters, is(urls.search() + "?orderBy=latest&show=collecting&type=normal"));
    }

    @Test
    public void get_with_type_citizen() {
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithTypeCitizen();
        assertThat(parameters, is(urls.search() + "?orderBy=latest&show=collecting&type=citizen"));
    }

    @Test
    public void generateAllFields_for_one_municipality_changes_url_to_municipality_url() {
        ArrayList<Long> municipalities = new ArrayList<>();
        municipalities.add(1L);
        initiativeSearch.setMunicipalities(municipalities);
        String fi_parameters = SearchParameterQueryString.generateParameters(initiativeSearch, allMunicipalities, Urls.FI);
        assertThat(fi_parameters, is(BASE_URL + "/fi/kunta/helsinki" + "?offset=5&limit=10&orderBy=id&show=collecting&municipalities=1&search=pattern&type=citizen"));

        String sv_parameters = SearchParameterQueryString.generateParameters(initiativeSearch, allMunicipalities, Urls.SV);
        assertThat(sv_parameters, is(BASE_URL + "/sv/kommun/helsingfors" + "?offset=5&limit=10&orderBy=id&show=collecting&municipalities=1&search=pattern&type=citizen"));
    }

    @Test
    public void generatesAllFields_with_two_municipalities() {
        ArrayList<Long> municipalities = new ArrayList<>();
        municipalities.add(1L);
        municipalities.add(4L);
        initiativeSearch.setMunicipalities(municipalities);
        String parameters = SearchParameterQueryString.generateParameters(initiativeSearch, allMunicipalities, urls);
        assertThat(parameters, is(urls.search() + "?offset=5&limit=10&orderBy=id&show=collecting&municipalities=1,4&search=pattern&type=citizen"));
    }
    @Test
    public void generatesAllFields_with_three_municipalities() {
        ArrayList<Long> municipalities = new ArrayList<>();
        municipalities.add(1L);
        municipalities.add(4L);
        municipalities.add(5L);
        initiativeSearch.setMunicipalities(municipalities);
        String parameters = SearchParameterQueryString.generateParameters(initiativeSearch, allMunicipalities, urls);
        assertThat(parameters, is(urls.search() + "?offset=5&limit=10&orderBy=id&show=collecting&municipalities=1,4,5&search=pattern&type=citizen"));
    }

    @Test
    public void get_with_municipality_ids(){
        ArrayList<Long> municipalities = new ArrayList<>();
        municipalities.add(1L);
        municipalities.add(4L);
        municipalities.add(5L);
        initiativeSearch.setMunicipalities(municipalities);
        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithMunicipalityIds(municipalities);
        assertThat(parameters, is(urls.search() + "?orderBy=latest&show=all&municipalities=1,4,5&type=all"));

    }

    @Test
    public void get_with_municipalities(){
        ArrayList<Municipality> municipalities = new ArrayList<>();
        Municipality testMunicipality = new Municipality(1L, "fi", "sv", true);
        Municipality testMunicipality2 = new Municipality(4L, "fi", "sv", true);
        Municipality testMunicipality3 = new Municipality(5L, "fi", "sv", true);


        municipalities.add(testMunicipality);
        municipalities.add(testMunicipality2);
        municipalities.add(testMunicipality3);

        String parameters = new SearchParameterQueryString(Urls.get(Locales.LOCALE_FI), initiativeSearch, Lists.newArrayList()).getWithMunicipalities(municipalities);
        assertThat(parameters, is(urls.search() + "?orderBy=latest&show=all&municipalities=1,4,5&type=all"));

    }


}
