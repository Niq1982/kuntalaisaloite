package fi.om.municipalityinitiative.service.id;

import java.io.Serializable;

public abstract class Id implements Serializable {

    private final long id;

    public Id(long id) {
        this.id = id;
    }

    public long toLong() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Id id1 = (Id) o;

        return id == id1.id;
    }

    @Override
    public final int hashCode() {
        return (int) id;
    }

    @Override
    public final String toString() {
        return String.valueOf(id);
    }
}

