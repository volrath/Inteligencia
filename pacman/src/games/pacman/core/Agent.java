package games.pacman.core;

import games.pacman.view.Mobile;
import games.pacman.maze.MazeNode;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 29-Sep-2004
 * Time: 12:10:13
 * To change this template use Options | File Templates.
 */
public class Agent {
    int color;
    int minPix;
    int maxPix;
    String name;
    Mobile proxy;

    public Agent(int color, int minPix, int maxPix, String name) {
        this.color = color;
        this.minPix = minPix;
        this.maxPix = maxPix;
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public void setCurrent(MazeNode current) {
        // System.out.println(name + " : " + current);
        if (proxy != null) {
            proxy.current = current;
        }
    }
}
