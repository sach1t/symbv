package rExp4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Expression {

    private List<Integer> tokens;

    public Expression() {
        this.tokens = new ArrayList<>();
        tokens.add(1);
        tokens.add(2);
        tokens.add(3);
        tokens.add(4);
        tokens.add(5);
    }

    public int Evaluate(int num) {
        ArrayStack arrayStack = new ArrayStack();
        tokens.add(num);
        for (int x: tokens) {
            if (arrayStack.isEmpty()) {
                arrayStack.push(x);
            } else {
                arrayStack.push(arrayStack.peek() + x);
            }
        }
        return arrayStack.pop();
    }
}
