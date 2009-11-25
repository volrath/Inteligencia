package games.pacman.features;

import games.pacman.ghost.Ghost;
import games.pacman.features.NodeScore;
import games.pacman.maze.MazeNode;

import java.util.HashMap;

public class DirectionalScore implements NodeScore {
    Ghost[] ghost;
    HashMap<MazeNode, Double> scores;

    static int maxTime = 50;

    public DirectionalScore(Ghost[] ghost) {
        this.ghost = ghost;
        scores = new HashMap<MazeNode,Double>();
    }

    public double score(MazeNode node) {
        Double score = scores.get(node);
        if (score == null) {
            return maxTime;
        } else {
            return score;
        }
    }

    public void updateScores() {
        scores.clear();
        // for each ghost, see where it can visit
        for (Ghost g : ghost) {
            crawl(new Crawler(g.current, g.previous), 0);
        }
    }

    private void crawl(Crawler c, int t) {
        if (t < maxTime) {
            update(c.cur, t);
            // and crawl the neighbours
            for (MazeNode n : c.cur.succ()) {
                if (!n.equals(c.prev)) {
                    // crawl the successor
                    crawl(new Crawler(n, c.cur), t+1);
                }
            }
        }
    }

    private void update(MazeNode n, double t) {
        Double score = scores.get(n);
        if (score == null || t < score) {
            scores.put(n, t);
        }
    }

    static class Crawler {
        MazeNode cur;
        MazeNode prev;

        Crawler(MazeNode cur, MazeNode prev) {
            this.cur = cur;
            this.prev = prev;
        }
    }
}
