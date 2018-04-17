package genPrime.symbv;

import genPrime.prime;

public class genPrime___prime___isPrime {
    public static void main(String args[]) {
        genPrime___prime___isPrime t = new genPrime___prime___isPrime();
        t.run(0);
    }
    public void run(int value) {
        prime patched = new prime();
        prime original = new prime();

        boolean patchedResult = patched.isPrime(value);
        boolean originalResult = original.isPrime___original(value);

        if (originalResult == patchedResult) {
            throw new Error();
        }
    }
}
