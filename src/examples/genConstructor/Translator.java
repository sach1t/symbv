package genConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Translator {
    List<String> responses;
    String defaultResponse;

    private Translator(List<String> responses, String defaultResponse) {
        this.responses = responses;
        this.defaultResponse = defaultResponse;
    }

    public static Translator symbv() {
        return new Translator(new ArrayList<>(Arrays.asList("0","1")),"0");
    }

    public String translate(int i) {
        i = Math.abs(i);
        return this.responses.get(i % this.responses.size());
    }
}
