package pacman;

import games.math.Vector2d;

/**
 * User: Simon
 * Date: 13-Oct-2007
 * Time: 19:14:34
 */
public interface Constants {
    public final static int NEUTRAL = 0;
    public final static int UP = 1;
    public final static int RIGHT = 2;
    public final static int DOWN = 3;
    public final static int LEFT = 4;
    public final static int[] dx = {0, 0, 1, 0, -1};
    public final static int[] dy = {0, -1, 0, 1, 0};


    public final Vector2d vCentre = new Vector2d(0, 0);
    public final Vector2d vUp = new Vector2d(0, -1);
    public final Vector2d vRight = new Vector2d(1, 0);
    public final Vector2d vDown = new Vector2d(0, 1);
    public final Vector2d vLeft = new Vector2d(-1, 0);

    

}
