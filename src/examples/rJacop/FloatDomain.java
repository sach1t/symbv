package rJacop;

public class FloatDomain {
    public static final double MaxFloat = Double.MAX_VALUE; // 1e150;

    // returns previous (toward -inf) floating-point number before d
    // supposed to be used by constraints
    public double upBit(double x) {
        double y;

        if (Double.isInfinite(x) && x > 0)
            return FloatDomain.MaxFloat; //Double.NaN;

        long xBits = Double.doubleToLongBits(x);
        if (x > 0.0)
            y = Double.longBitsToDouble(xBits + 1);
        else if (x == 0.0)
            y = Double.longBitsToDouble(1);
        else
            y = Double.longBitsToDouble(xBits - 1);
        return y;
    }
}
