package genContains.symbv;

import genContains.Contains;

public class genContains___Contains___containsAny {
    public static void main(String args[]) {
        genContains___Contains___containsAny t = new genContains___Contains___containsAny();
        t.run(new String(),
            new String(),
            new String());
    }
    public void run(String text, String query1, String query2) {
        Contains patched = new Contains();
        Contains original = new Contains();

        boolean patchedResult = patched.containsAny(text,
            query1,
            query2);
        boolean originalResult = original.containsAny___original(text,
            query1,
            query2);

        if (originalResult == patchedResult) {
            throw new Error();
        }
    }
}
