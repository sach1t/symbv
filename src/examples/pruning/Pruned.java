package pruning;

public class Pruned {
    public int myFunction(int a) {
        int result = 0;
        if (a > 100) {
            result = this.expensive(a);
            result = this.expensive(result);
        } else if (a > 10) {
            result = this.expensive(a);
        } else {
            // cheap part
            result = 4;
            // that's it.
        }
        return result;
    }

    public int expensive(int b) {
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                if (j % 2 == 0) {
                    b += i;
                } else {
                    b -= i;
                }
            }
        }
        return b;
    }
}
