package genConstructor.symbv;

import genConstructor.Translator;

public class genConstructor___Translator___translate {
    public static void main(String args[]) {
        genConstructor___Translator___translate t = new genConstructor___Translator___translate();
        t.run(0);
    }
    public void run(int i) {
        Translator patched = Translator.symbv();
        Translator original = Translator.symbv();

        String patchedResult = patched.translate(i);
        String originalResult = original.translate___original(i);

        if (originalResult.equals(patchedResult)) {
            throw new Error();
        }
    }
}
