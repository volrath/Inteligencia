package pacman;

/**
 * User: Simon
 * Date: 09-Mar-2007
 * Time: 12:55:20
 */
public class Position {
    int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int dist(Position p) {
        return (int) Math.sqrt(sqr(x - p.x) + sqr(y - p.y));
    }

    public static int sqr(int x) {
        return x * x;
    }
}
