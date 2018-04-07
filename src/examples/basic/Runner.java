package basic;

public class Runner {

    public static void main(String args[]) {
        Runner r = new Runner();
        r.run(0);
    }

    public void run(int i) {
        Test t = new Test();

        Integer ret = t.update(i);
        Integer ret2 = t.updateOriginal(i);

        if (ret.equals(ret2)) {
            throw new Error();
        }
    }
}
