package games.pacman.maze;

import utilities.JEasyFrame;

import java.util.ArrayList;

import games.pacman.view.DisplayComponent;
import games.pacman.maze.Line;

/**
 * Created by IntelliJ IDEA.
 * User: Owner
 * Date: 12-Oct-2004
 * Time: 22:35:40
 * To change this template use File | Settings | File Templates.
 */
public class Maze1 {

    public static void main(String[] args) throws Exception {
        int w = 114;
        int h = 130;
        DisplayComponent dc = new DisplayComponent(w, h);
        dc.updateObjects(maze);
        new JEasyFrame(dc, "Maze 1", true).center();

    }

    static ArrayList maze = new ArrayList();

    static {

        maze.add(new Line(7, 9, 7, 21));
        maze.add(new Line(27, 9, 27, 37));
        maze.add(new Line(39, 9, 39, 21));
        maze.add(new Line(75, 9, 75, 21));
        maze.add(new Line(87, 9, 87, 37));
        maze.add(new Line(107, 9, 107, 21));

        maze.add(new Line(7, 21, 107, 21));

        maze.add(new Line(0, 37, 15, 37));
        maze.add(new Line(27, 37, 51, 37));
        maze.add(new Line(63, 37, 87, 37));
        maze.add(new Line(99, 37, 114, 37));

        // maze.add(new Line());


        maze.add(new Line(99, 21, 99, 97));
        maze.add(new Line(75, 85, 75, 97));
        maze.add(new Line(39, 85, 39, 97));
        maze.add(new Line(15, 21, 15, 97));


        maze.add(new Line(7, 97, 7, 121));
        maze.add(new Line(27, 97, 27, 121));
        maze.add(new Line(87, 97, 87, 121));
        maze.add(new Line(107, 97, 107, 121));

        maze.add(new Line(39, 109, 39, 121));
        maze.add(new Line(51, 97, 51, 109));
        maze.add(new Line(63, 97, 63, 109));
        maze.add(new Line(75, 109, 75, 121));

        maze.add(new Line(7, 97, 107, 97));
        maze.add(new Line(39, 109, 51, 109));
        maze.add(new Line(63, 109, 75, 109));
        maze.add(new Line(7, 121, 107, 121));
    }

}
