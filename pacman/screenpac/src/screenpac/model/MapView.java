package screenpac.model;

import utilities.JEasyFrame;

import javax.swing.*;
import java.awt.*;

import screenpac.extract.Constants;

public class MapView extends JComponent implements Constants {

    public static void test(Maze maze) {
        new JEasyFrame(new MapView(maze), "Map Test", true);
    }

    Maze maze;

    public MapView(Maze maze) {
        this.maze = maze;
    }

    public void paintComponent(Graphics g) {
        // for now paint just the map
        g.setColor(Color.blue);
        g.fillRect(0, 0, maze.getWidth() * MAG, maze.getHeight() * MAG);
        g.setColor(Color.cyan);
        for (Node n : maze.getMap()) {
            g.fillRect(n.x * MAG - 9, n.y * MAG - 9, 18, 18);
        }
        g.setColor(Color.black);
        for (Node n : maze.getMap()) {
            g.fillRect(n.x * MAG - 8, n.y * MAG - 8, 16, 16);
        }

        // only both with this to show the true graph nodes
//        g.setColor(Color.red);
//        for (Node n : maze.getMap()) {
//            g.fillRect(n.x * MAG - 0, n.y * MAG - 1, 1, 1);
//        }

        // these are part of the game state rather than the map
        // so need to consider which pills are still there
        g.setColor(Color.blue);
        for (Node n : maze.getPills()) {
            // g.fillOval(n.x*mag-1, n.y*mag-1, 3, 3);
        }
        g.setColor(Color.white);
        for (Node n : maze.getPowers()) {
            // g.fillOval(n.x * mag - 3, n.y * mag - 3, 6, 6);
        }
        // now need to check out the power pills etc
        for (Node n : maze.getMap()) {
            if (n.col != Color.black) {
                g.setColor(n.col);
                g.fillRect(n.x * MAG - 8, n.y * MAG - 8, 16, 16);
                // reset the color ready for next time
                n.col = Color.black;
            }
        }

    }

    public Dimension getPreferredSize() {
        return new Dimension(maze.getWidth() * MAG, maze.getHeight() * MAG);
    }
}
