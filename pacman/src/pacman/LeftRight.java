package pacman;

/**
 * User: Simon
 * Date: 13-Oct-2007
 * Time: 22:49:32
 */
public class LeftRight implements PacAgent, Constants {

    int i;
    static int period = 20;

    public LeftRight() {
        i = 0;
    }

    public int move(GameState gs) {
        i++;
        int test = (i / period) % 3;
        if (test == 0) {
            return NEUTRAL;
        } else if (test == 1) {
            return RIGHT;
        } else {
            return LEFT;
        }
    }
}
