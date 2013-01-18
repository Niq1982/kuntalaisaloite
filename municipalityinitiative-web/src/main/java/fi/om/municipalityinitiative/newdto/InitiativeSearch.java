package fi.om.municipalityinitiative.newdto;

public class InitiativeSearch {
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
}
