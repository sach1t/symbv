package basic;

public class Test {

    public int update(int i) {
        if (i < 10) {
            if (i < 1) {
                return 11;
            } else {
                return 10;
            }
        } else {
            return 0;
        }
    }

    public int updateOriginal(int i) {
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