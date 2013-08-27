package fi.om.municipalityinitiative.dto;

import fi.om.municipalityinitiative.web.Urls;

public class InitiativeSearch {
    private Integer offset;
    private Integer limit;
    private OrderBy orderBy = OrderBy.latest;
    private Show show = Show.collecting;
    private Long municipality;
    private String search;
    private Type type = Type.all;

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

    public Type getType() {
        return type;
    }

    public InitiativeSearch setType(Type type) {
        this.type = type;
        return this;
    }

    public enum OrderBy {
        oldestSent, latestSent,
        id,
        mostParticipants, leastParticipants,
        oldest, latest
    }

    public enum Type {
        all,
        normal,
        council,
        citizen
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InitiativeSearch)) {
            return false;
        }

        InitiativeSearch that = (InitiativeSearch) obj;
        return equals(this.getMunicipality(), that.getMunicipality())
                && equals(this.getLimit(), that.getLimit())
                && equals(this.getOffset(), that.getOffset())
                && equals(this.getOrderBy(), that.getOrderBy())
                && equals(this.getType(), that.getType())
                && equals(this.getShow(), that.getShow());
    }

    @Override
    public int hashCode() {
        int result = offset != null ? offset.hashCode() : 0;
        result = 31 * result + (limit != null ? limit.hashCode() : 0);
        result = 31 * result + orderBy.hashCode();
        result = 31 * result + show.hashCode();
        result = 31 * result + (municipality != null ? municipality.hashCode() : 0);
        result = 31 * result + type.hashCode();
        result = 31 * result + (search != null ? search.hashCode() : 0);
        return result;
    }

    private static <E extends Object> boolean equals(E o1, E o2) {
        if (o1 == null && o2 == null)
            return true;
        else if (o1 == null || o2 == null)
            return false;
        else return o1.equals(o2);
    }
}
