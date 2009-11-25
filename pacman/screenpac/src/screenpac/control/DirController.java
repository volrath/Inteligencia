package screenpac.control;

import screenpac.extract.GameObjects;

/**
 * this controller chooses the current desired
 * direction
 */

public interface DirController {
    public int getDirection(GameObjects gs);
}
