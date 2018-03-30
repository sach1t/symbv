package simple;

public class IntPair {
    int x,y;

    public IntPair(int a, int b) {
        this.x = a;
        this.y = b;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntPair) {
            IntPair o = (IntPair)obj;
            return o.x == x && o.y == y;
        }

        return false;
    }
}
