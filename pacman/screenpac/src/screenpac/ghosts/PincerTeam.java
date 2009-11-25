package screenpac.ghosts;

import screenpac.model.*;
import screenpac.extract.Constants;
import screenpac.ghosts.GhostTeamController;
import screenpac.features.NodeScore;
import screenpac.features.Utilities;
import games.pacman.ghost.Ghost;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

public class PincerTeam implements GhostTeamController, Constants {

    static int off = 15;
    static int[][] da = {{off, 0}, {0, off}, {-off, 0}, {0, -off}};

    int[] dirs;
    ArrayList<Node> junctions;
    MazeInterface maze;

    public PincerTeam() {
        this.dirs = new int[nGhosts];
        // set up the junctions

    }

    public int[] getActions(GameStateInterface gs) {
        // work out where each ghost should be
        // in order to pincer the Pac-Man
        // find the closest junctions to pac-man
        // in each direction
        if (!gs.getMaze().equals(maze)) setMaze(gs.getMaze());
        // now calculate an aim point for each ghost
        // by finding the closest junction within a certain
        // distance of the pac-man in each direction
        Node pac = gs.getPacman().current;
        Set<Node> targets = new HashSet<Node>();
        targets.addAll(junctions);
        for (int i=0; i<nGhosts; i++) {
            GhostState ghost = gs.getGhosts()[i];
            Node next;
            ArrayList<Node> possibles = ghost.getPossibles();
            if (possibles.size() == 1) {
                // if there's only one then set the direction for it
                next = possibles.get(0);
            } else {
                // aim for the junction closest to the aim point
                Node target = getClosest(targets, new AimPoint(pac.x + da[i][0], pac.y+ da[i][1]));
                // now select the next node as the *possible* node with
                // the smallest shortest path distance
                next = Utilities.getClosest(possibles, target, gs.getMaze());
                targets.remove(target);
            }
            dirs[i] = Utilities.getWrappedDirection(ghost.current, next, gs.getMaze());
        }
        return dirs;
    }

    private void setMaze(MazeInterface maze) {
        this.maze = maze;
        // set up the junctions
        junctions = new ArrayList<Node>();
        for (Node n : maze.getMap()) {
            if (n.adj.size() > 2) {
                junctions.add(n);
            }
        }
    }

    public Node getClosest(Collection<Node> nodes, AimPoint p) {
        double best = Double.MAX_VALUE;
        Node sel = null;
        for (Node n : nodes) {
            double s = p.score(null, n);
            if (s < best) {
                best = s;
                sel = n;
            }
        }
        return sel;
    }



}
