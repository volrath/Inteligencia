package games.pacman.maze;

import utilities.JEasyFrame;

import java.util.ArrayList;

import games.pacman.view.DisplayComponent;
import games.pacman.view.Drawable;
import games.pacman.maze.OldMaze;
import games.pacman.maze.Line;

public class MazeOne {

    public static void main(String[] args) throws Exception {
        DisplayComponent dc = new DisplayComponent(w*2, h*2);
        dc.updateObjects(maze);
        new JEasyFrame(dc, "Maze 1", true).center();
        getMaze();
        MazeOne.getMaze();
    }

    public static OldMaze getMaze() {
        return new OldMaze(maze, w, h, xx, yy);
    }

//    public static Maze maze() {
//        return new Maze(maze, w, h, xx, yy);
//    }

    static int w = 114;
    static int h = 130;
    static int[] xx = { 7, 15, 27, 39, 51, 63, 75, 87, 99, 107 };
    static int[] yy = { 9, 21, 37, 49, 61, 73, 85, 97, 109, 121 };

    static ArrayList<Drawable> maze = new ArrayList<Drawable>();

    static {

        maze.add(new Line(7, 9, 7, 21));
        maze.add(new Line(27, 9, 27, 37));
        maze.add(new Line(39, 9, 39, 21));
        maze.add(new Line(75, 9, 75, 21));
        maze.add(new Line(87, 9, 87, 37));
        maze.add(new Line(107, 9, 107, 21));

        maze.add(new Line(7, 21, 107, 21));

        maze.add(new Line(27, 37, 51, 37));
        maze.add(new Line(63, 37, 87, 37));

        maze.add(new Line(7, 9, 27, 9));
        maze.add(new Line(39, 9, 75, 9));
        maze.add(new Line(87, 9, 107, 9));

        maze.add(new Line(51, 21, 51, 37));
        maze.add(new Line(63, 21, 63, 37));

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


        // false indicates a line with no pills

        maze.add(new Line(0, 73, 27, 73, false));
        maze.add(new Line(87, 73, 114, 73, false));

        maze.add(new Line(0, 37, 15, 37, false));
        maze.add(new Line(99, 37, 114, 37, false));

        maze.add(new Line(39, 37, 39, 73, false));
        maze.add(new Line(75, 37, 75, 73, false));

        maze.add(new Line(15, 49, 99, 49, false));
        maze.add(new Line(39, 73, 75, 73, false));

        maze.add(new Line(27, 73, 27, 61, false));
        maze.add(new Line(27, 61, 39, 61, false));
        maze.add(new Line(75, 61, 87, 61, false));
        maze.add(new Line(87, 61, 87, 73, false));

        maze.add(new Line(15, 85, 39, 85, true));
        maze.add(new Line(39, 85, 51, 85, false));

        maze.add(new Line(51, 85, 51, 73, false));
        maze.add(new Line(63, 73, 63, 85, false));

        maze.add(new Line(75, 85, 99, 85, true));
        maze.add(new Line(63, 85, 75, 85, false));
    }
}
