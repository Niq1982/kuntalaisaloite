package fi.om.municipalityinitiative.newdto;

import fi.om.municipalityinitiative.web.Urls;

public class InitiativeSearch {
    private Integer offset;
    private Integer limit;
    private OrderBy orderBy = OrderBy.latestSent;
    private Show show = Show.sent;
    private Long municipality;
    private String search;

    public Long getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Long municipality) {
        this.municipality = municipality;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public InitiativeSearch copy() {

        InitiativeSearch initiativeSearch = new InitiativeSearch();
        initiativeSearch.limit = this.limit;
        initiativeSearch.offset = this.offset;
        initiativeSearch.orderBy = this.orderBy;
        initiativeSearch.municipality = this.municipality;
        initiativeSearch.search = this.search;
        return initiativeSearch;
    }

    public InitiativeSearch setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public int getLimit() {
        if (limit == null)
            return Urls.DEFAULT_INITIATIVE_SEARCH_LIMIT;
        else
            return Math.min(limit, Urls.MAX_INITIATIVE_SEARCH_LIMIT);
    }

    public InitiativeSearch setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public Integer getOffset() {
        return offset;
    }

    public InitiativeSearch setOrderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public Show getShow() {
        return show;
    }

    public InitiativeSearch setShow(Show show) {
        this.show = show;
        return this;
    }

    public enum Show {
        collecting,
        sent
    }

    public enum OrderBy {
        oldestSent, latestSent, id
    }
}
