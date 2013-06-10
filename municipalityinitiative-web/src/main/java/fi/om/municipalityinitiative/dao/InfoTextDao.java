package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.InfoPageText;
import fi.om.municipalityinitiative.dto.InfoTextFooterLink;
import fi.om.municipalityinitiative.dto.InfoTextSubject;
import fi.om.municipalityinitiative.util.LanguageCode;

import java.util.List;

public interface InfoTextDao {

    void publishFromDraft(String uri, String modifierName);

    void saveDraft(InfoPageText uri);

    void draftFromPublished(String uri, String modifierName);

    List<InfoTextSubject> getNotEmptySubjects(LanguageCode languageCode);

    List<InfoTextSubject> getAllSubjects(LanguageCode languageCode);

    InfoPageText getPublished(String uri);

    InfoPageText getDraft(String uri);

    List<InfoTextFooterLink> getFooterLinks(LanguageCode language);
}
