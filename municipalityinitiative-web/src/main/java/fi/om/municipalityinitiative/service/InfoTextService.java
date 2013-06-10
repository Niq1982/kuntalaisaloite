package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fi.om.municipalityinitiative.dao.InfoTextDao;
import fi.om.municipalityinitiative.dto.InfoPageText;
import fi.om.municipalityinitiative.dto.InfoTextSubject;
import fi.om.municipalityinitiative.dto.user.OmLoginUserHolder;
import fi.om.municipalityinitiative.util.InfoTextCategory;
import fi.om.municipalityinitiative.util.LanguageCode;
import fi.om.municipalityinitiative.util.Locales;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InfoTextService {

    @Resource
    InfoTextDao infoTextDao;

    public InfoTextService() {
    }

    @Transactional(readOnly = true)
    public InfoPageText getPublished(String uri) {
        InfoPageText published = infoTextDao.getPublished(uri);
        return published;
    }

    @Transactional(readOnly = true)
    public InfoPageText getDraft(String uri, OmLoginUserHolder requiredOmLoginUserHolder) {
        requiredOmLoginUserHolder.assertOmUser();
        return infoTextDao.getDraft(uri);
    }

    @Transactional(readOnly = true)
    public Map<String, List<InfoTextSubject>> getPublicSubjectList(Locale locale) {
        return mapByCategory(infoTextDao.getNotEmptySubjects(languageCode(locale)));
    }

    @Transactional(readOnly = true)
    public Map<String, List<InfoTextSubject>> getOmSubjectList(Locale locale, OmLoginUserHolder requiredOmLoginUserHolder) {
        requiredOmLoginUserHolder.assertOmUser();
        return mapByCategory(infoTextDao.getAllSubjects(languageCode(locale)));
    }

    @Transactional(readOnly = false)
    public void updateDraft(OmLoginUserHolder requiredOmLoginUserHolder, String localizedPageName, String content, String subject) {
        requiredOmLoginUserHolder.assertOmUser();

        InfoPageText infoPageText = InfoPageText.builder(localizedPageName)
                .withText(subject, content)
                .withModifier(requiredOmLoginUserHolder.getUser().getName(), DateTime.now())
                .build();
        infoTextDao.saveDraft(infoPageText);
    }

    @Transactional(readOnly = false)
    public void publishDraft(String uri, OmLoginUserHolder requiredOmLoginUserHolder) {
        requiredOmLoginUserHolder.assertOmUser();
        infoTextDao.publishFromDraft(uri, requiredOmLoginUserHolder.getUser().getName());
    }

    @Transactional(readOnly = false)
    public void restoreDraftFromPublished(String uri, OmLoginUserHolder requiredOmLoginUserHolder) {
        requiredOmLoginUserHolder.assertOmUser();
        infoTextDao.draftFromPublished(uri, requiredOmLoginUserHolder.getUser().getName());
    }

    private static Map<String, List<InfoTextSubject>> mapByCategory(List<InfoTextSubject> subjectList) {
        Map<String, List<InfoTextSubject>> map = Maps.newHashMap();
        for (InfoTextCategory infoTextCategory : InfoTextCategory.values()) {
            map.put(infoTextCategory.name(), Lists.<InfoTextSubject>newArrayList());
        }

        for (InfoTextSubject infoTextSubject : subjectList) {
            map.get(infoTextSubject.getInfoTextCategory().name()).add(infoTextSubject);
        }

        return map;
    }


    private static LanguageCode languageCode(Locale locale) {
        return locale.equals(Locales.LOCALE_FI) ? LanguageCode.FI : LanguageCode.SV;
    }
}
