package games.pacman.controllers;

import games.pacman.controllers.PacController;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 19-Oct-2004
 * Time: 10:00:14
 * To change this template use Options | File Templates.
 */
public class RandomController implements PacController {
    // extend this in a meaningful way next time...
    static Random r = new Random();

    // choose one of the possible directions at random
    public int getDirection() {
        return r.nextInt(dx.length);
    }
}
