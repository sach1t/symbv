package rExp4j;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Expression {

    private final List<Integer> tokens;

    public Expression() {
        this.tokens = Arrays.asList(1, 2, 3, 4, 5);
    }

    public int Evaluate(int[] nums) {
        ArrayStack arrayStack = new ArrayStack();
        for (int x: nums) {
            if (arrayStack.isEmpty()) {
                arrayStack.push(x);
            } else {
                arrayStack.push(arrayStack.peek() + 1);
            }
        }
        return arrayStack.pop();
    }
}
