package fi.om.municipalityinitiative.dto;

import com.google.common.base.Optional;

public class InitiativeSearch {

    private boolean includePublic = true;
    
    private boolean includeOwn = false;

    private Optional<Integer> offsetIndex = Optional.absent();
    private Optional<Integer> limit = Optional.absent();

    private OrderBy orderBy = OrderBy.START_DATE;

    //TODO: add OM/VRK cases

    public InitiativeSearch() {}

    public InitiativeSearch(boolean includePublic, boolean includeOwn) {
        this.includePublic = includePublic;
        this.includeOwn = includeOwn;
    }

    public boolean isIncludePublic() {
        return includePublic;
    }

    public void setIncludePublic(boolean includePublic) {
        this.includePublic = includePublic;
    }

    public boolean isIncludeOwn() {
        return includeOwn;
    }

    public void setIncludeOwn(boolean includeOwn) {
        this.includeOwn = includeOwn;
    }

    public boolean isEmptyCondition() {
        return (!includeOwn && !includePublic);
    }

    public Optional<Integer> getOffsetIndex() {
        return offsetIndex;
    }

    public Optional<Integer> getLimit() {
        return limit;
    }

    public InitiativeSearch setRestrict(int offsetIndex, int limit) {
        setOffsetIndex(offsetIndex);
        setLimit(limit);
        return this;
    }

    public InitiativeSearch setOffsetIndex(int offsetIndex) {
        this.offsetIndex = Optional.of(offsetIndex);
        return this;
    }

    public InitiativeSearch setLimit(int limit) {
        this.limit = Optional.of(limit);
        return this;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public enum OrderBy {
        ID,START_DATE;
    }
}
