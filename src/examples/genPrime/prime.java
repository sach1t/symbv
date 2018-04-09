package genPrime;

public class prime {
    public boolean isPrime(int value) {
        for (int i = value; i > 1; i--) {
            if ((value % i) == 0) {
                return false;
            }
        }
        return true;
    }
}
