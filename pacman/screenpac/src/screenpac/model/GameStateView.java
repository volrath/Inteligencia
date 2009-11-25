package screenpac.model;

import screenpac.extract.Constants;

import javax.swing.*;
import java.awt.*;

import games.pacman.maze.MazeOne;
import utilities.JEasyFrame;

public class GameStateView extends JComponent implements Constants {

    GameState gs;
    MapView mv;

    public static void main(String[] args) throws Exception {
        Maze maze = new Maze();
        maze.processOldMaze(MazeOne.getMaze());
        GameState gs = new GameState(maze);
        gs.reset();
        GameStateView gsv = new GameStateView(gs);
        JEasyFrame fr = new JEasyFrame(gsv, "Game State", true);
        KeyController kc = new KeyController();
        fr.addKeyListener(kc);
        int dir = NEUTRAL;
        while (true) {
            int d = kc.getDirection(gs);
            if (d != NEUTRAL) dir = d;
            gs.next(dir, null);
            gsv.repaint();
            Thread.sleep(20);
        }

//        Random r = new Random();
//        for (int i=0; i<1000; i++) {
//            gs.next(r.nextInt(5), null);
//            gsv.repaint();
//            Thread.sleep(100);
//        }
    }

    public static GameStateView test(GameStateSetter gs) {
        GameStateView gsv = new GameStateView(gs);
        JEasyFrame fr = new JEasyFrame(gsv, "Game State", true);
        return gsv;
    }

//    public static GameStateView test(GameState gs) {
//        // create a JEasyFrame with a view component
//        GameStateView gsv = new GameStateView(gs);
//        new JEasyFrame(gsv, "Game State", true);
//        return gsv;
//    }

    public GameStateView(GameState gs) {
        this.gs = gs;
        mv = new MapView(gs.maze);
    }

    public void paintComponent(Graphics g) {
        // paint the map first
        mv.paintComponent(g);

        // then overlay the agents
        g.setColor(Color.yellow);
        Node pac = gs.pacMan.current;
        g.fillOval(pac.x * MAG - 6, pac.y * MAG - 6, 12, 12);
        int ix = 0;
        for (GhostState ghs : gs.ghosts) {
            if (ghs != null) {
                drawGhost(g, ghs, ix++);
            }
        }
        g.setColor(Color.white);
        for (Node p : gs.maze.getPills()) {
            // for each pill p, test to see if it's on
            if (gs.pills.get(p.pillIndex)) {
                g.fillOval(p.x * MAG - 1, p.y * MAG - 1, 3, 3);
            }
        }
        g.setColor(Color.green);
        for (Node p : gs.maze.getPowers()) {
            // for each pill p, test to see if it's on
            if (gs.powers.get(p.powerIndex)) {
                g.fillOval(p.x * MAG - 5, p.y * MAG - 5, 10, 10);
            }
        }
    }

    private void drawGhost(Graphics g, GhostState ghs, int ix) {
        if (ghs.returning()) {
            drawEyes(g, ghs);
        } else {
            if (ghs.edible()) {
                g.setColor(edibleColor);
            } else {
                g.setColor(ghostColors[ix]);
            }
            Node ng = ghs.current;
            if (ng != null) {
                g.fillRect(ng.x * MAG - 6, ng.y * MAG - 6, 12, 12);
            }
        }
    }

    public void drawEyes (Graphics g, GhostState ghs) {
        Node n = ghs.current;
        g.setColor(Color.white);
        g.fillOval(n.x * MAG - 6, n.y * MAG - 3, 6, 6);
        g.fillOval(n.x * MAG, n.y * MAG - 3, 6, 6);
        g.setColor(Color.blue);
        g.fillOval(n.x * MAG - 4, n.y * MAG - 1, 2, 2);
        g.fillOval(n.x * MAG + 2, n.y * MAG - 1, 2, 2);
    }

    public Dimension getPreferredSize
            () {
        return new Dimension(gs.maze.getWidth() * MAG, gs.maze.getHeight() * MAG);
    }
}
