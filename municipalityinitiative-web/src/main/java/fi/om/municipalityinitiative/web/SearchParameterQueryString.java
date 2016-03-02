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

    private final InitiativeSearch original;


    public SearchParameterQueryString(InitiativeSearch search) {
        this.original = search;
    }

    static String generateParameters(InitiativeSearch search) {

        StringBuilder builder = new StringBuilder();

        for (Field field : search.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            addFieldValue(builder, field, search);
        }

        return builder.toString();

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
        return generateParameters(original.copy());
    }

    public String getWithLimit(int limit) {
        return generateParameters(original.copy().setOffset(0).setLimit(limit));
    }

    public String getWithOffset(int offset) {
        return generateParameters(original.copy().setOffset(offset));
    }

    public String getWithOrderById() {
        return generateParameters(original.copy().setOrderBy(InitiativeSearch.OrderBy.id));
    }

    public String getWithOrderByOldestSent() {
        return generateParameters(original.copy().setOrderBy(InitiativeSearch.OrderBy.oldestSent));
    }

    public String getWithOrderByLatestSent() {
        return generateParameters(original.copy().setOrderBy(InitiativeSearch.OrderBy.latestSent));
    }

    public String getWithOrderByMostParticipants() {
        return generateParameters(original.copy().setOrderBy(InitiativeSearch.OrderBy.mostParticipants));
    }

    public String getWithOrderByLeastParticipants() {
        return generateParameters(original.copy().setOrderBy(InitiativeSearch.OrderBy.leastParticipants));
    }

    public String getWithOrderByLatest() {
        return generateParameters(original.copy().setOrderBy(InitiativeSearch.OrderBy.latest));
    }

    public String getWithOrderByOldest() {
        return generateParameters(original.copy().setOrderBy(InitiativeSearch.OrderBy.oldest));
    }

    public String getWithStateSent() {
        return generateParameters(new InitiativeSearch()
                .setShow(InitiativeSearch.Show.sent)
                .setMunicipalities(original.getMunicipalities())
                .setType(original.getType())
                .setOrderBy(InitiativeSearch.OrderBy.latestSent));
    }
    public String getWithStateCollecting() {
        return generateParameters(new InitiativeSearch()
                .setShow(InitiativeSearch.Show.collecting)
                .setMunicipalities(original.getMunicipalities())
                .setType(original.getType())
                .setOrderBy(InitiativeSearch.OrderBy.latest));
    }
    public String getWithStateAll() {
        return generateParameters(new InitiativeSearch()
                .setShow(InitiativeSearch.Show.all)
                .setMunicipalities(original.getMunicipalities())
                .setType(original.getType())
                .setOrderBy(InitiativeSearch.OrderBy.latest));
    }

    public String getWithStateDraft() {
        return generateParameters(new InitiativeSearch()
                .setShow(InitiativeSearch.Show.draft)
                .setMunicipalities(original.getMunicipalities())
                .setType(original.getType())
                .setOrderBy(InitiativeSearch.OrderBy.latest));
    }

    public String getWithStateAccepted() {
        return generateParameters(new InitiativeSearch()
                .setShow(InitiativeSearch.Show.accepted)
                .setMunicipalities(original.getMunicipalities())
                .setType(original.getType())
                .setOrderBy(InitiativeSearch.OrderBy.latest));
    }

    public String getWithStateReview() {
        return generateParameters(new InitiativeSearch()
                .setShow(InitiativeSearch.Show.review)
                .setMunicipalities(original.getMunicipalities())
                .setType(original.getType())
                .setOrderBy(InitiativeSearch.OrderBy.latest));
    }
    
    public String getWithStateFix() {
        return generateParameters(new InitiativeSearch()
                .setShow(InitiativeSearch.Show.fix)
                .setMunicipalities(original.getMunicipalities())
                .setType(original.getType())
                .setOrderBy(InitiativeSearch.OrderBy.latest));
    }

    public String getWithMunicipality(Long municipalityId) {
        return generateParameters(new InitiativeSearch().setMunicipalities(municipalityId));
    }

    public String getWithMunicipalities(List<Municipality> municipalities) {
        ArrayList<Long> municipalityIds = new ArrayList<>();
        for(Municipality m : municipalities) {
            municipalityIds.add(m.getId());
        }
        return getWithMunicipalityIds(municipalityIds);
    }

    public String getWithMunicipalityIds(ArrayList<Long> municipalityIds) {
        return generateParameters(new InitiativeSearch().setMunicipalities(municipalityIds));
    }

    public String getWithTypeAll() {
        return generateParameters(new InitiativeSearch()
                .setShow(original.getShow())
                .setMunicipalities(original.getMunicipalities())
                .setType(InitiativeSearch.Type.all));
    }

    public String getWithTypeNormal() {
        return generateParameters(new InitiativeSearch()
                .setShow(original.getShow())
                .setMunicipalities(original.getMunicipalities())
                .setType(InitiativeSearch.Type.normal));
    }

    public String getWithTypeCitizen() {
        return generateParameters(new InitiativeSearch()
                .setShow(original.getShow())
                .setMunicipalities(original.getMunicipalities())
                .setType(InitiativeSearch.Type.citizen));
    }

    public String getWithTypeCouncil() {
        return generateParameters(new InitiativeSearch()
                .setShow(original.getShow())
                .setMunicipalities(original.getMunicipalities())
                .setType(InitiativeSearch.Type.council));
    }
}
