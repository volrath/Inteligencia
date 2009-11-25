package games.pacman.features;

import games.pacman.maze.MazeNode;
import games.pacman.ghost.Ghost;
import games.pacman.core.GameData;
import utilities.StatSummary;

/**
 * User: Simon
 * Date: 14-May-2008
 * Time: 22:32:27
 */

public class SparseFeatures implements FeatureExtractor {

    double[] vec;
    StatSummary ss;
    public static boolean normalise = true;

    public static int nFeatures = 4;

    // enumerate the other extra features
    static int ghost = 0;
    static int edGhost = 1;
    static int fPill = 2;
    static int fPower = 3;

    public SparseFeatures() {
        vec = new double[nFeatures];
        ss = new StatSummary();
    }

    public SparseFeatures(double[] vec) {
        this.vec = vec;
    }

    public double[] setVec(MazeNode node, GameData vc) {
        double max = vc.maze.h;
        for (int i = 0; i < nFeatures; i++) {
            vec[i] = max;
        }
        for (int i = 0; i < vc.ghosts.length; i++) {
            Ghost g = vc.ghosts[i];
            if (g.current != null) {
                double dist = vc.maze.dist[node.ix][g.current.ix];
                int ix = g.edible() ? edGhost : ghost;
                vec[ix] = Math.min(dist, vec[ix]);
            }
        }
        vec[fPill] = vc.pillDist(node, vc.maze.pills);
        vec[fPower] = vc.pillDist(node, vc.maze.power);
        for (int i = 0; i < vec.length; i++) {
            vec[i] = vec[i] / vc.maze.h;
            if (vec[i] > 1) vec[i] = 1;
        }
        if (normalise) {
            normalise(vec);
        }
        return vec;
    }

    private void normalise(double[] vec) {
        ss.reset();
        for (int i=0; i<vec.length; i++) {
            ss.add(vec[i]);
        }
        for (int i=0; i<vec.length; i++) {
            vec[i] = (vec[i] - ss.min()) / (ss.max() - ss.min());
        }

    }

    public int size() {
        return nFeatures;
    }
}