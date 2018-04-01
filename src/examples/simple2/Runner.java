package simple2;

public class Runner {

    public static void main(String args[]) {
        Runner r = new Runner();
        r.run(0);
    }

    public void run(int i) {
        Test original = new Test();
        TestPatched patched = new TestPatched();

        Integer ret = original.update(i);
        Integer ret2 = patched.update(i);

        if (ret.equals(ret2)) {
            throw new Error();
        }
    }
}
