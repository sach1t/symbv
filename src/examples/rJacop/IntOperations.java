package rJacop;

public class IntOperations {
    int result(int w, int x, int y, int z) {
        int resultA = Operations.multiplyInt(w, x);
        int resultB = Operations.addInt(y, z);
        return Operations.subtractInt(resultA, resultB);
    }
}

class Operations {
    /**
     * Returns the product of the arguments,
     * if the result overflows MaxInt or MinInt is returned.
     *
     * @param x the first value
     * @param y the second value
     * @return the result or MaxInt/MinInt if result causes overflow
     */
    public static int multiplyInt(int x, int y) {
        long r = (long)x * (long)y;
        if ((int)r != r) {
            return r > 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE; //IntDomain.MaxInt : IntDomain.MinInt;
        }
        return (int)r;
    }

    /**
     * Returns the sum of its arguments,
     * if the result overflows MaxInt or MinInt is returned.
     *
     * @param x the first value
     * @param y the second value
     * @return the result or MaxInt/MinInt if result causes overflow
     */
    public static int addInt(int x, int y) {
        int r = x + y;
        // HD 2-12 Overflow iff both arguments have the opposite sign of the result
        if (((x ^ r) & (y ^ r)) < 0) {
            return r > 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE; //IntDomain.MaxInt : IntDomain.MinInt;
        }
        return r;
    }

    /**
     * Returns the difference of the arguments,
     * if the result overflows MaxInt or MinInt is returned.
     *
     * @param x the first value
     * @param y the second value to subtract from the first
     * @return the result or MaxInt/MinInt if result causes overflow
     */
    public static int subtractInt(int x, int y) {
        int r = x - y;
        // HD 2-12 Overflow iff the arguments have different signs and
        // the sign of the result is different than the sign of x
        if (((x ^ y) & (x ^ r)) < 0) {
            return r > 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE; //IntDomain.MaxInt : IntDomain.MinInt;
        }
        return r;
    }
}
