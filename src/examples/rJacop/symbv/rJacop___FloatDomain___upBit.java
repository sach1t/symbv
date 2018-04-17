package rJacop.symbv;

import rJacop.FloatDomain;

public class rJacop___FloatDomain___upBit {
    public static void main(String args[]) {
        rJacop___FloatDomain___upBit t = new rJacop___FloatDomain___upBit();
        t.run(0);
    }
    public void run(double x) {
        FloatDomain patched = new FloatDomain();
        FloatDomain original = new FloatDomain();

        double patchedResult = patched.upBit(x);
        double originalResult = original.upBit___original(x);

        if (originalResult == patchedResult) {
            throw new Error();
        }
    }
}
