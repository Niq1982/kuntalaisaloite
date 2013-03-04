package fi.om.municipalityinitiative.newdto.service;

public class Municipality {
    private long id;
    private String finnishName;
    private String swedishName;

    public Municipality(long id, String finnishName, String swedishName) {
        this.id = id;
        this.finnishName = finnishName;
        this.swedishName = swedishName;
    }

    public long getId() {
        return id;
    }

    public String getFinnishName() {
        return finnishName;
    }

    public String getSwedishName() {
        return swedishName;
    }
}
