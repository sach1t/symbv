package simple;

public class Runner {

    public static void main(String args[]) {
        Runner r = new Runner();
        r.run(0,0,0);
    }

    public void run(int pedalPos, int bSwitch, int pedalCmd) {
        Test t = new Test();

        int ret = t.update(pedalPos, bSwitch, pedalCmd);
        int ret2 = t.updateOriginal(pedalPos, bSwitch, pedalCmd);

        if (ret == ret2) {
            throw new Error();
        }
    }
}
