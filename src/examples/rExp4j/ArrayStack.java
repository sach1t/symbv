package rExp4j;

import java.util.EmptyStackException;

class ArrayStack {

    private int[] data;

    private int idx;

    ArrayStack() {
        this(5);
    }

    ArrayStack(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException(
                    "Stack's capacity must be positive");
        }

        data = new int[initialCapacity];
        idx = -1;
    }

    void push(int value) {
        if (idx + 1 == data.length) {
            int[] temp = new int[(int) (data.length * 1.2) + 1];
            System.arraycopy(data, 0, temp, 0, data.length);
            data = temp;
        }

        data[++idx] = value;
    }

    int peek() {
        if (idx == -1) {
            throw new EmptyStackException();
        }
        return data[idx];
    }

    int pop() {
        if (idx == -1) {
            throw new EmptyStackException();
        }
        return data[idx--];
    }

    boolean isEmpty() {
        return idx == -1;
    }

    int size() {
        return idx + 1;
    }
}
