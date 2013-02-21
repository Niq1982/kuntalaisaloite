package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.newdto.InitiativeSearch;

import java.lang.reflect.Field;

/**
 * Freemarker uses this class for generating links with get-parameters at search page.
 */
public class SearchParameterGenerator {

    private final InitiativeSearch original;

    public SearchParameterGenerator(InitiativeSearch search) {
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
                .append("=")
                .append(fieldValue.toString());
    }

    public String getWithLimit(int limit) {
        return generateParameters(original.copy().setLimit(limit));
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
                .setMunicipality(original.getMunicipality())
                .setOrderBy(InitiativeSearch.OrderBy.latestSent));
    }
    public String getWithStateCollecting() {
        return generateParameters(new InitiativeSearch()
                .setShow(InitiativeSearch.Show.collecting)
                .setMunicipality(original.getMunicipality())
                .setOrderBy(InitiativeSearch.OrderBy.latest));
    }
    public String getWithStateAll() {
        return generateParameters(new InitiativeSearch()
                .setShow(InitiativeSearch.Show.all)
                .setMunicipality(original.getMunicipality())
                .setOrderBy(InitiativeSearch.OrderBy.latest));
    }

}
