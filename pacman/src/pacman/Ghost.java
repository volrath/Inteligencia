package pacman;

import games.math.Vector2d;

/**
 * User: Simon
 * Date: 13-Oct-2007
 * Time: 18:44:47
 */
public class Ghost {
    Vector2d cur;
    Vector2d prev;
    int color;
    int index;


    public Ghost(int color, int index) {
        this.color = color;
        this.index = index;
    }

    

}
