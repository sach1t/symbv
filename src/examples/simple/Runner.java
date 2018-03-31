package simple;

public class Runner {

    public static void main(String args[]) {
        Runner r = new Runner();
        r.run(0,0,0);
    }

    public void run(int pedalPos, int bSwitch, int pedalCmd) {
        Test original = new Test();
        TestPatched patched = new TestPatched();

        IntPair ret = original.update(pedalPos, bSwitch, pedalCmd);
        IntPair ret2 = patched.update(pedalPos, bSwitch, pedalCmd);

        if (ret.equals(ret2)) {
            throw new Error();
        }
    }
}
