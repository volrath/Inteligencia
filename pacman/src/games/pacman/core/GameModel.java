package games.pacman.core;

import utilities.JEasyFrame;
import games.pacman.controllers.SimpleAvoidance;
import games.pacman.controllers.KeyController;
import games.pacman.controllers.PacController;
import games.pacman.ghost.Ghost;
import games.pacman.view.DisplayComponent;
import games.pacman.maze.OldMaze;
import games.pacman.maze.MazeOne;
import games.pacman.core.PacMan;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 13-Oct-2004
 * Time: 15:33:22
 * To change this template use Options | File Templates.
 */

public class GameModel {
    // the full game
    // begins with a maze and the PacMan

    static int width = 223;
    static int height = 266;
    DisplayComponent dc;
    public OldMaze maze;
    PacController controller;
    public PacMan pacman;
    int nGhosts = 0;
    public Ghost[] ghosts;

    public GameModel() {
        this(8);
    }

//    public void setState(ArrayList<GameObject> ob) {
//
//    }
//
    public GameModel(int nGhosts) {
        this.nGhosts = nGhosts;
        dc = new DisplayComponent(width, height);
        maze = MazeOne.getMaze();
        // new CloseableFrame(dc, "Simple Pac-Man", true).center();
        // JFrame frame = new JFrame();
        new JEasyFrame(dc, "Pac-Man", true).center();
        KeyController kc = new KeyController();
        controller = kc;
        pacman = new PacMan();
        ghosts = new Ghost[nGhosts];
        addGhosts();
        maze.lines.add(pacman);
        // maze.rescale(2);
        controller = new SimpleAvoidance(maze, ghosts, pacman);
        dc.addKeyListener(kc);
        dc.requestFocus();

        dc.updateObjects(maze.lines);
        maze.place(pacman);
        // maze.print(System.out);
    }

    public void addGhosts() {
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i] = new Ghost(maze, pacman, ghosts);
            maze.lines.add(ghosts[i]);
            maze.place(ghosts[i]);
        }
    }

    public void repaint() {
        dc.repaint();
    }

    public void redraw() {
        dc.redraw();
    }

}
