package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.LocalDate;

import java.util.Locale;

public class InitiativeUIInfo {

    private final Initiative initiative;
    private final Locale locale;

    public InitiativeUIInfo(Initiative initiative, Locale locale) {
        this.initiative = initiative;
        this.locale = locale;
    }

    public static InitiativeUIInfo parse(Initiative initiative, Locale locale) {
        return new InitiativeUIInfo(initiative, locale);
    }

    public String getName() {
        return initiative.getName();
    }

    public String getProposal() {
        return initiative.getProposal();
    }

    public String getAuthorName() {
        return initiative.getAuthorName();
    }

    public boolean isShowName() {
        return initiative.isShowName();
    }

    public LocalDate getCreateTime() {
        return initiative.getCreateTime();
    }

    public Long getId() {
        return initiative.getId();
    }

    public Maybe<String> getManagementHash() {
        return initiative.getManagementHash();
    }

    public boolean isCollectable() {
        return initiative.isCollectable();
    }

    public boolean isSent() {
        return initiative.isSent();
    }

    public Maybe<LocalDate> getSentTime() {
        return initiative.getSentTime();
    }

    public MunicipalityInfo getMunicipality() {
        MunicipalityInfo municipalityInfo = new MunicipalityInfo();
        municipalityInfo.setId(initiative.getMunicipality().getId());
        municipalityInfo.setName(locale == Locales.LOCALE_FI
                ? initiative.getMunicipality().getFinnishName()
                : initiative.getMunicipality().getSwedishName());
        return municipalityInfo;
    }

}
