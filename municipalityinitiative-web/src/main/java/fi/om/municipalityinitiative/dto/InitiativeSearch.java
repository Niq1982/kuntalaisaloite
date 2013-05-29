package fi.om.municipalityinitiative.dto;

import fi.om.municipalityinitiative.web.Urls;

public class InitiativeSearch {
    private Integer offset;
    private Integer limit;
    private OrderBy orderBy = OrderBy.latest;
    private Show show = Show.collecting;
    private Long municipality;
    private String search;

    public Long getMunicipality() {
        return municipality;
    }

    public InitiativeSearch setMunicipality(Long municipality) {
        this.municipality = municipality;
        return this;
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
        initiativeSearch.show = this.show;
        return initiativeSearch;
    }

    public InitiativeSearch setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Integer getLimit() {
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
        collecting(false),
        sent(false),
        all(false),

        //om views:
        draft(true),
        review(true),
        fix(true),
        accepted(true),
        omAll(true);

        private boolean omOnly;

        Show(boolean omOnly) {
            this.omOnly = omOnly;
        }

        public boolean isOmOnly() {
            return omOnly;
        }
    }

    public enum OrderBy {
        oldestSent, latestSent,
        id,
        mostParticipants, leastParticipants,
        oldest, latest
    }
}
