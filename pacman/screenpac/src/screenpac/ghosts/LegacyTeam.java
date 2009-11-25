package screenpac.ghosts;

import screenpac.model.GhostState;
import screenpac.model.Node;
import screenpac.model.GameState;
import screenpac.model.GameStateInterface;
import screenpac.extract.Constants;
import screenpac.ghosts.GhostTeamController;
import screenpac.features.NodeScore;
import screenpac.features.Utilities;
import screenpac.adapter.GhostProxy;
import games.pacman.ghost.Ghost;

import java.util.ArrayList;

public class LegacyTeam implements GhostTeamController, Constants {
    // this team follows the same algorithm as the simulator
    // used for Lucas, Proceedings of IEEE CIG 2005
    int[] dirs;
    NodeScore[] scorers;
    ArrayList<Node> options;
    GhostProxy[] proxies;

    public LegacyTeam() {
        this.dirs = new int[nGhosts];
        options = new ArrayList<Node>();
        scorers = new NodeScore[]{
                new PathScore(), new EuclideanScore(),
                new ManhattanScore(), new RandScore(),
        };
    }

    public int[] getActions(GameStateInterface gs) {
        // each score function dictates where each ghost should move
        for (int i=0; i<dirs.length; i++) {
            GhostState gh = gs.getGhosts()[i];
            options.clear();
            for (Node n : gh.current.adj) {
                // turning back is not a valid option
                if (!n.equals(gh.previous)) options.add(n);
            }
            dirs[i] = Utilities.getMinDir(options, gh.current, scorers[i], gs);
        }
        return dirs;
    }
}