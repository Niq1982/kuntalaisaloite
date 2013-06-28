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
    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Id that = (Id) obj;
        return this.id == that.id;
    }

    @Override
    public final int hashCode() {
        return (int) id;
    }

    @Override
    public final String toString() {
        return getClass().getSimpleName() + "(" + id + ")";
    }
}

