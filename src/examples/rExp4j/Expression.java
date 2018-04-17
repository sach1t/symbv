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
        Stack<Integer> arrayStack = new Stack<>();
        for (int x : nums) {
            if (arrayStack.isEmpty()) {
                arrayStack.push(x);
            } else {
                arrayStack.push(arrayStack.peek() + x);
            }
        }
        return arrayStack.pop();
    }

    public int Evaluate___original(int[] nums) {
        ArrayStack arrayStack = new ArrayStack();
        for (int x : nums) {
            if (arrayStack.isEmpty()) {
                arrayStack.push(x);
            } else {
                arrayStack.push(arrayStack.peek() + x);
            }
        }
        return arrayStack.pop();
    }
}
