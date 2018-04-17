package genContains.symbv;

import genContains.Contains;

public class genContains___Contains___contains {
    public static void main(String args[]) {
        genContains___Contains___contains t = new genContains___Contains___contains();
        t.run(new String(),
            new String());
    }
    public void run(String text, String query) {
        Contains patched = new Contains();
        Contains original = new Contains();

        boolean patchedResult = patched.contains(text,
            query);
        boolean originalResult = original.contains___original(text,
            query);

        if (originalResult == patchedResult) {
            throw new Error();
        }
    }
}
