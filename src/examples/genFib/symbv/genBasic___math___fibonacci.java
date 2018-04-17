package genFib.symbv;

import genBasic.math;

public class genBasic___math___fibonacci {
    public static void main(String args[]) {
        genBasic___math___fibonacci t = new genBasic___math___fibonacci();
        t.run(0);
    }
    public void run(int n) {
        math patched = new math();
        math original = new math();

        int patchedResult = patched.fibonacci(n);
        int originalResult = original.fibonacci___original(n);

        if (originalResult == patchedResult) {
            throw new Error();
        }
    }
}
