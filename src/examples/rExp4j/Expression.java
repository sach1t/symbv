package rExp4j;

import java.util.Arrays;
import java.util.List;

public class Expression {
    private final List<Double> tokens;

    public Expression() {
        this.tokens = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0);
    }

    public double Evaluate() {
        ArrayStack arrayStack = new ArrayStack();

        this.tokens.forEach(x -> {
            if (arrayStack.isEmpty()) {
                arrayStack.push(x);
            } else {
                arrayStack.push(arrayStack.peek() + x);
            }
        });
        return arrayStack.pop();
    }
}
