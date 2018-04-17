package genBasic;

public class math {

    public int fibonacci(int n) {
        int prev = 0, next = 1, result = 0;
        for (int i = 0; i < n; i++) {
            result = prev + next;
            prev = next;
            next = result;
        }
        return result;
    }

    public int fibonacci___original(int n) {
        if (n <= 0)
            return 0;
        else if (n == 1)
            return 1;
        else
            return fibonacci(n - 1) + fibonacci(n - 2);
    }
}
