package screenpac.control;

import screenpac.extract.GameObjects;
import screenpac.extract.Constants;

public class LeftRight implements DirController, Constants {
    public static void main(String[] args) {
        System.out.println(260 * 260);
    }
    int count = 0;
    public int getDirection(GameObjects gs) {

        if (gs.agent == null)  {
            count++;
            return count % 2 == 0 ? LEFT : RIGHT; 
        }
        if (gs.agent.x > 130) {
            return LEFT;
        } else {
            return RIGHT;
        }
    }
}
