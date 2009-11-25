package screenpac.control;

import screenpac.extract.GameObjects;
import screenpac.extract.Constants;

import java.util.Random;

public class SimpleHunter implements DirController, Constants {
    static Random r = new Random();

    public static void main(String[] args) {
        System.out.println(260 * 260);
    }
    int curDir = 0;

    public int getDirection(GameObjects gs) {

        if (gs.agent == null)  {
            if (r.nextDouble() < 0.1) {
                curDir = r.nextInt(dx.length);
            }
            return curDir;
        }
        return curDir;
    }
}