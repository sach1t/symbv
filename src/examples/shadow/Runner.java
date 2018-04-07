package shadow;

public class Runner {

    public static void main(String args[]) {
        Runner r = new Runner();
        r.run(0);
    }

    public void run(int x) {
        Test t = new Test();

        int ret = t.foo(x);
        int ret2 = t.fooOriginal(x);

        if (ret == ret2) {
            throw new Error();
        }
    }
}
