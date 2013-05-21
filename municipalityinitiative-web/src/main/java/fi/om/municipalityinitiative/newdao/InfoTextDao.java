package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.InfoPageText;
import fi.om.municipalityinitiative.newdto.InfoTextFooterLink;
import fi.om.municipalityinitiative.newdto.InfoTextSubject;
import fi.om.municipalityinitiative.util.LanguageCode;

import java.util.List;

public interface InfoTextDao {

    public void publishFromDraft(String uri, String modifierName);

    public void saveDraft(InfoPageText uri);

    public void draftFromPublished(String uri, String modifierName);

    public List<InfoTextSubject> getNotEmptySubjects(LanguageCode languageCode);

    public List<InfoTextSubject> getAllSubjects(LanguageCode languageCode);

    public InfoPageText getPublished(String uri);

    public InfoPageText getDraft(String uri);

    List<InfoTextFooterLink> getFooterLinks(LanguageCode language);
}
