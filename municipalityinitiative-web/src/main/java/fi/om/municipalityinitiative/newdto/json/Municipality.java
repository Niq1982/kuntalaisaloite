package fi.om.municipalityinitiative.newdto.json;

public class Municipality {
    private String name;
    private long id;

    public Municipality(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
