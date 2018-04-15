package pathanalysis;

import soot.Unit;

public class UnitWrapper {
    Unit u;
    public UnitWrapper(Unit u) {
        this.u = u;
    }

    @Override
    public String toString() {
        return u.toString();
    }

    @Override
    public int hashCode() {
        return u.toString().hashCode();
    }

    public Unit unwrap() {
        return u;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UnitWrapper && u.toString().equals(obj.toString());
    }
}