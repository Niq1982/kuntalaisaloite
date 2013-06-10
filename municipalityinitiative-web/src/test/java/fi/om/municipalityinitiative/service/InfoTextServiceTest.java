package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dao.InfoTextDao;
import fi.om.municipalityinitiative.dto.InfoTextSubject;
import fi.om.municipalityinitiative.util.InfoTextCategory;
import fi.om.municipalityinitiative.util.LanguageCode;
import fi.om.municipalityinitiative.util.Locales;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class InfoTextServiceTest {

    InfoTextService infoTextService;

    InfoTextDao infoTextDaoMock = mock(InfoTextDao.class);

    @Before
    public void setup() {
        infoTextService = new InfoTextService();
        infoTextService.infoTextDao = infoTextDaoMock;
    }

    @Test
    public void groups_subjects_by_category() {

        final List<InfoTextSubject> list = Lists.newArrayList();
        list.add(new InfoTextSubject(InfoTextCategory.KUNTALAISALOITE, "uri1", ""));
        list.add(new InfoTextSubject(InfoTextCategory.KUNTALAISALOITE_FI, "uri2", ""));
        list.add(new InfoTextSubject(InfoTextCategory.KUNTALAISALOITE, "uri3", ""));

        stub(infoTextDaoMock.getAllSubjects(LanguageCode.FI)).toReturn(list);

        Map<String,List<InfoTextSubject>> map = infoTextService.getOmSubjectList(Locales.LOCALE_FI, TestHelper.omLoginUser);

        assertThat(map.get(InfoTextCategory.KUNTALAISALOITE.name()).size(), is(2));
        assertThat(map.get(InfoTextCategory.KUNTALAISALOITE_FI.name()).size(), is(1));

    }

    @Test
    public void wont_fail_if_empty_list() {

        stub(infoTextDaoMock.getAllSubjects(LanguageCode.FI)).toReturn(new ArrayList<InfoTextSubject>());

        Map<String, List<InfoTextSubject>> map = infoTextService.getOmSubjectList(Locales.LOCALE_FI, TestHelper.omLoginUser);

        assertThat(map.size(), is(InfoTextCategory.values().length));
        for (List<InfoTextSubject> categorySubjects : map.values()) {
            assertThat(categorySubjects.size(), is(0));
        }
    }


}
