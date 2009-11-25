package games.pacman.controllers;

import games.pacman.core.GameModel;
import games.math.Vector2d;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 13-Oct-2004
 * Time: 15:41:09
 * To change this template use Options | File Templates.
 */
public interface PacController {

//    public final static int NEUTRAL = 0;
//    public final static int CENTRE = 0;
//    public final static int UP = 1;
//    public final static int RIGHT = 2;
//    public final static int DOWN = 3;
//    public final static int LEFT = 4;
//    public final static int[] dx = {0, 0, 1, 0, -1};
//    public final static int[] dy = {0, -1, 0, 1, 0};

    // old values below
    public final static int UP = 0;
    public final static int RIGHT = 1;
    public final static int DOWN = 2;
    public final static int LEFT = 3;
    public final static int CENTRE = 4;
    public final static int[] dx = {0, 1, 0, -1, 0};
    public final static int[] dy = {-1, 0, 1, 0, 0};

    // extend this in a meaningful way next time...
    // public int getDirection(GameModel model);

    public int getDirection();

}
