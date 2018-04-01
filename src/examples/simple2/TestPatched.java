package simple2;

public class TestPatched {

    public int update(int i) {
        if (i < 10) {
            if (i < 5) {
                return 11;
            } else {
                return 10;
            }
        } else {
            return 0;
        }
    }

}
