package rExp4j.symbv;

import rExp4j.Expression;

public class rExp4j___Expression___Evaluate {
    public static void main(String args[]) {
        rExp4j___Expression___Evaluate t = new rExp4j___Expression___Evaluate();
        t.run(new int[4]);
    }
    public void run(int[] nums) {
        Expression patched = new Expression();
        Expression original = new Expression();

        int patchedResult = patched.Evaluate(nums);
        int originalResult = original.Evaluate___original(nums);

        if (originalResult == patchedResult) {
            throw new Error();
        }
    }
}
