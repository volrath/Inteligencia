package games.pacman.core;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 29-Sep-2004
 * Time: 09:32:37
 * To change this template use Options | File Templates.
 */
public class SinglePixel {
    int pos;
    int val;

    public SinglePixel(int pos, int val) {
        this.pos = pos;
        this.val = val;
    }

    public String toString() {
        return pos + " : " + val;
    }
}
