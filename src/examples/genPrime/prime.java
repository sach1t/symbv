package genPrime;

public class prime {

    public boolean isPrime(int value) {
        // consider negative, 0, 1 and 2 as a special case.
        if (value <= 1) {
            return false;
        } else if (value == 2) {
            return true;
        }
        // get square root and only loop up to that value.
        int squareRoot = new Double(Math.ceil(Math.sqrt(value))).intValue();
        for (int i = 2; i <= squareRoot; i++) {
            if ((value % i) == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isPrime___original(int value) {
        for (int i = value; i > 1; i--) {
            if ((value % i) == 0) {
                return false;
            }
        }
        return true;
    }
}
