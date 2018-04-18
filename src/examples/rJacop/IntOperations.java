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
        return Operations.safeInt(r);
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
        long r = (long)x + (long)y;
        return Operations.safeInt(r);
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
        long r = (long)x - (long)y;
        return Operations.safeInt(r);
    }

    private static int safeInt(long r) {
        if (r > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }

        if (r < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) r;
    }
}
