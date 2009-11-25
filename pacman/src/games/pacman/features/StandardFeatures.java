package games.pacman.features;

import games.pacman.ghost.Ghost;
import games.pacman.maze.MazeNode;
import games.pacman.core.GameData;

/**
 * User: Simon
 * Date: 14-May-2008
 * Time: 22:32:27
 */

public class StandardFeatures implements FeatureExtractor {

    double[] vec;

    public static int nFeatures = 13;

    // enumerate the other extra features
    static int fx = 8;
    static int fy = 9;
    static int fPill = 10;
    static int fPower = 11;
    static int fJunction = 12;


    public StandardFeatures() {
        vec = new double[nFeatures];
    }

    public StandardFeatures(double[] vec) {
        this.vec = vec;
    }

    public double[] setVec(MazeNode node, GameData vc) {
        // ok - set the features one by one
        // predator ghosts
        // System.out.println("Node: " + node);
        // System.out.println("Vec: " + vec);
        for (int i=0; i<Math.min(vec.length, vc.ghosts.length * 2); i++) {
            vec[i] = 2 * vc.maze.h;
        }
        for (int i = 0; i < vc.ghosts.length; i++) {
            Ghost g = vc.ghosts[i];
            if (!g.edible() && g.current != null) {
                vec[i] = vc.maze.dist[node.ix][g.current.ix];
            }
        }
        if (nFeatures > 4) {
            for (int i = vc.ghosts.length; i < 2 * vc.ghosts.length; i++) {
                Ghost g = vc.ghosts[i - vc.ghosts.length];
                if (g.edible() && g.current != null) {
                    vec[i] = vc.maze.dist[node.ix][g.current.ix];
                }
            }
            vec[fx] = node.x;
            vec[fy] = node.y;
            // vec[10] = node.next.length; // number of neighbours
            vec[fPill] = vc.pillDist(node, vc.maze.pills);
            vec[fPower] = vc.pillDist(node, vc.maze.power);
            vec[fJunction] = node.junctionDist;
            // vec[fJunction] *= 25; // boost it to range of other params
            // now normalise it
        }
        for (int i = 0; i < vec.length; i++) {
            vec[i] = vec[i] / vc.maze.h;
        }
        // VecMat.print(vec);
        return vec;
    }

    public int size() {
        return nFeatures;
    }
}
