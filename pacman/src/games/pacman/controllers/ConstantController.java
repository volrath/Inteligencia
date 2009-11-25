package games.pacman.controllers;

import games.pacman.controllers.PacController;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 19-Oct-2004
 * Time: 10:00:14
 * To change this template use Options | File Templates.
 */
public class ConstantController implements PacController {
    // extend this in a meaningful way next time...
    public int getDirection() {
        return CENTRE;
    }
}
