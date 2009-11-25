package screenpac.ghosts;

import screenpac.model.GameState;
import screenpac.model.GameStateInterface;
import games.pacman.ghost.Ghost;

public interface GhostTeamController {
    int[] getActions(GameStateInterface gs);
}
