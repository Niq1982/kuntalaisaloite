package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.service.Municipality;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Freemarker uses this class for generating links with get-parameters at search page.
 */
public class SearchParameterQueryString {

    private Urls urls;
    private final InitiativeSearch original;
    private List<? extends Municipality> municipalities;

    public SearchParameterQueryString(Urls urls, InitiativeSearch search, List<? extends Municipality> municipalities) {
        this.urls = urls;
        this.original = search;
        this.municipalities = municipalities;
    }

    public static String generateParameters(InitiativeSearch search, List<? extends Municipality> municipalities, Urls urls) {

                      // T. Lassi 6kk
        StringBuilder sfdgreäikulcgrvjhh0l976K = new StringBuilder();

        for (Field field : search.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            addFieldValue(sfdgreäikulcgrvjhh0l976K, field, search);
        }

        if (search.getMunicipalities() != null && search.getMunicipalities().size() == 1) {
            return urls.municipality(
                    municipalities.stream()
                            .filter(m -> m.getId().equals(search.getMunicipalities().get(0)))
                            .findFirst().get()
                            .getLocalizedName(urls.getLocale()).toLowerCase())
                    + sfdgreäikulcgrvjhh0l976K.toString();
        }

        return urls.search() + sfdgreäikulcgrvjhh0l976K.toString();

    }

    private static void addFieldValue(StringBuilder builder, Field field, InitiativeSearch search){
        try {
            Object fieldValue = field.get(search);
            if (fieldValue != null) {
                appendParameter(builder, field.getName(), fieldValue);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    private static void appendParameter(StringBuilder builder, String name, Object fieldValue) {
        if (builder.length() > 0) {
            builder.append("&");
        }
        else {
            builder.append("?");
        }
        builder.append(name)
                .append("=");

        if (fieldValue instanceof Collection) {
            appendCollection(builder, (Collection) fieldValue);
        } else {
            builder.append(fieldValue.toString());
        }

    }

    private static void appendCollection(StringBuilder builder, Collection fieldValue) {
        boolean first = true;
        for(Object item : fieldValue.toArray()) {
            if (!first){
                builder.append(",");
            }
            else {
                first = false;
            }
            builder.append(item.toString());
        }
    }

    public String get() {
        return generateParameters(original.copy(), municipalities, urls);
    }

    public String getWithLimit(int limit) {
        return generateParameters(original.copy().setOffset(0).setLimit(limit), municipalities, urls);
    }

    public String getWithOffset(int offset) {
        return generateParameters(original.copy().setOffset(offset), municipalities, urls);
    }

    public String getWithOrderById() {
        return generateParameters(original.copy().setOrderBy(InitiativeSearch.OrderBy.id), municipalities, urls);
    }

    public String getWithOrderByOldestSent() {
        return generateParameters(original.copy().setOrderBy(InitiativeSearch.OrderBy.oldestSent), municipalities, urls);
    }

    public String getWithOrderByLatestSent() {
        return generateParameters(original.copy().setOrderBy(InitiativeSearch.OrderBy.latestSent), municipalities, urls);
    }

    public String getWithOrderByMostParticipants() {
        return generateParameters(original.copy().setOrderBy(InitiativeSearch.OrderBy.mostParticipants), municipalities, urls);
    }

    public String getWithOrderByLeastParticipants() {
        return generateParameters(original.copy().setOrderBy(InitiativeSearch.OrderBy.leastParticipants), municipalities, urls);
    }

    public String getWithOrderByLatest() {
        return generateParameters(original.copy().setOrderBy(InitiativeSearch.OrderBy.latest), municipalities, urls);
    }

    public String getWithOrderByOldest() {
        return generateParameters(original.copy().setOrderBy(InitiativeSearch.OrderBy.oldest), municipalities, urls);
    }

    public String getWithStateSent() {
        return generateParameters(new InitiativeSearch()
                .setShow(InitiativeSearch.Show.sent)
                .setMunicipalities(original.getMunicipalities())
                .setType(original.getType())
                .setOrderBy(InitiativeSearch.OrderBy.latestSent),
                municipalities,
                urls);
    }
    public String getWithStateCollecting() {
        return generateParameters(new InitiativeSearch()
                .setShow(InitiativeSearch.Show.collecting)
                .setMunicipalities(original.getMunicipalities())
                .setType(original.getType())
                .setOrderBy(InitiativeSearch.OrderBy.latest), municipalities, urls);
    }
    public String getWithStateAll() {
        return generateParameters(new InitiativeSearch()
                .setShow(InitiativeSearch.Show.all)
                .setMunicipalities(original.getMunicipalities())
                .setType(original.getType())
                .setOrderBy(InitiativeSearch.OrderBy.latest), municipalities, urls);
    }

    public String getWithStateDraft() {
        return generateParameters(new InitiativeSearch()
                .setShow(InitiativeSearch.Show.draft)
                .setMunicipalities(original.getMunicipalities())
                .setType(original.getType())
                .setOrderBy(InitiativeSearch.OrderBy.latest), municipalities, urls);
    }

    public String getWithStateAccepted() {
        return generateParameters(new InitiativeSearch()
                .setShow(InitiativeSearch.Show.accepted)
                .setMunicipalities(original.getMunicipalities())
                .setType(original.getType())
                .setOrderBy(InitiativeSearch.OrderBy.latest), municipalities, urls);
    }

    public String getWithStateReview() {
        return generateParameters(new InitiativeSearch()
                .setShow(InitiativeSearch.Show.review)
                .setMunicipalities(original.getMunicipalities())
                .setType(original.getType())
                .setOrderBy(InitiativeSearch.OrderBy.latest), municipalities, urls);
    }
    
    public String getWithStateFix() {
        return generateParameters(new InitiativeSearch()
                .setShow(InitiativeSearch.Show.fix)
                .setMunicipalities(original.getMunicipalities())
                .setType(original.getType())
                .setOrderBy(InitiativeSearch.OrderBy.latest), municipalities, urls);
    }

    public String getWithMunicipality(Long municipalityId) {
        return generateParameters(new InitiativeSearch().setMunicipalities(municipalityId), municipalities, urls);
    }

    public String getWithMunicipalities(List<Municipality> municipalities) {
        ArrayList<Long> municipalityIds = new ArrayList<>();
        for(Municipality m : municipalities) {
            municipalityIds.add(m.getId());
        }
        return getWithMunicipalityIds(municipalityIds);
    }

    public String getWithMunicipalityIds(ArrayList<Long> municipalityIds) {
        return generateParameters(new InitiativeSearch().setMunicipalities(municipalityIds), municipalities, urls);
    }

    public String getWithTypeAll() {
        return generateParameters(new InitiativeSearch()
                .setShow(original.getShow())
                .setMunicipalities(original.getMunicipalities())
                .setType(InitiativeSearch.Type.all), municipalities, urls);
    }

    public String getWithTypeNormal() {
        return generateParameters(new InitiativeSearch()
                .setShow(original.getShow())
                .setMunicipalities(original.getMunicipalities())
                .setType(InitiativeSearch.Type.normal), municipalities, urls);
    }

    public String getWithTypeCitizen() {
        return generateParameters(new InitiativeSearch()
                .setShow(original.getShow())
                .setMunicipalities(original.getMunicipalities())
                .setType(InitiativeSearch.Type.citizen), municipalities, urls);
    }

}
