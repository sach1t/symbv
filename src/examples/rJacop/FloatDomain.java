package rJacop;

public class FloatDomain {

    // 1e150;
    public static final double MaxFloat = Double.MAX_VALUE;

    // returns previous (toward -inf) floating-point number before d
    // supposed to be used by constraints
    public double upBit(double x) {
        if (x > 0.0) {
            if (Double.isInfinite(x)) {
                // Double.NaN;
                return FloatDomain.MaxFloat;
            }
            return Double.longBitsToDouble(Double.doubleToLongBits(x) + 1);
        } else if (x == 0.0)
            return Double.longBitsToDouble(1);
        else
            return Double.longBitsToDouble(Double.doubleToLongBits(x) - 1);
    }

    public double upBit___original(double x) {
        double y;
        if (Double.isInfinite(x) && x > 0)
            // Double.NaN;
            return FloatDomain.MaxFloat;
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
