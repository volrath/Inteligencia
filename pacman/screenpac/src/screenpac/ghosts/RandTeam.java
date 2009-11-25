package screenpac.ghosts;

import screenpac.model.GameState;
import screenpac.model.GameStateInterface;
import screenpac.extract.Constants;

public class RandTeam implements GhostTeamController, Constants {
    int[] dirs;
    public RandTeam() {
        // no need to create an array every time
        this.dirs = new int[nGhosts];
    }

    public int[] getActions(GameStateInterface gs) {
        // choose a random direction every time
        // the game model will enforce legality
        for (int i=0; i<dirs.length; i++) {
            dirs[i] = rand.nextInt(dx.length);
        }
        return dirs;
    }
}
