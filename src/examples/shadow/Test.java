package shadow;

public class Test {
    int foo(int x) {
        int y = x - 1;
        if (y > 7) {
            int z = x - 8;
            if (z < 4) {
                int w = 2;
            }
            return 0;
        }
        return 1;
    }

    int fooOriginal(int x) {
        int y = x + 1;
        if (y > 7) {
            int z = x - 8;
            if (z < 4) {
                int w = 2;
            }
            return 0;
        }
        return 1;
    }
}
