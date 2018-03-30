package simple;

public class Test {

    public IntPair update(int pedalPos, int bSwitch, int pedalCmd) {
        if (pedalPos == 0) {
            pedalCmd = pedalCmd + 1;
        } else if (pedalPos == 1) {
            pedalCmd = pedalCmd + 2;
        } else {
            pedalCmd = pedalPos;
        }
        pedalCmd = pedalCmd + 1;

        int meter = 2;
        if (bSwitch == 0) {
            meter = 1;
        } else if (bSwitch == 1) {
            meter = 2;
        }

        int altpress = 0;
        if (pedalCmd == 2) {
            altpress = 0;
        } else if (pedalCmd == 3) {
            altpress = 2;
        } else {
            altpress = 3;
        }
        return new IntPair(meter, altpress);
    }

}