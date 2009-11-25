package games.pacman.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import utilities.RangeMapper;
import games.pacman.view.Drawable;
import games.pacman.maze.MazeNode;
import games.pacman.features.NodeScore;
import games.pacman.features.DirectionalScore;
import neural.general.Functions;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 29-Sep-2004
 * Time: 09:34:19
 * To change this template use Options | File Templates.
 */
public class DisplayComponent extends JComponent {
    int w, h;
    Dimension d;
    public ArrayList<Drawable> objects;

    // set both of these to enable Node score evaluation painting
    MazeNode[] nodes = null;
    NodeScore vc;
    double[] act;

    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, w, h);
        draw(g);
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, w, h);
        draw(g);
        System.out.println("Painted");
    }

    public void redraw() {
        // draws without overwriting
        draw(getGraphics());
    }

    // public void

    public void draw(Graphics g) {
        if (objects != null && g != null) {
            synchronized (DisplayComponent.class) {
                drawActivation(g);
                for (Drawable d : objects) {
                    // System.out.println("Trying to draw " + d + " on " + g);
                    d.draw(g, w, h) ;
                }
            }
        }
    }

    public void enableActivations(MazeNode[] nodes, NodeScore vc) {
        this.nodes = nodes;
        this.vc = vc;
        act = new double[nodes.length];
    }

    private void drawActivation(Graphics g) {
        if (nodes == null || vc == null) {
            return;
        }
        if (vc instanceof DirectionalScore) {
            DirectionalScore ds = (DirectionalScore) vc;
            ds.updateScores();
        }
        // must of be ok to proceed - evaluate each one
        // find the activaations
        for (int i=0; i<nodes.length; i++) {
            act[i] = vc.score(nodes[i]);
        }
        RangeMapper range = new RangeMapper(Functions.min(act), Functions.max(act), 0, 1);
        // System.out.println("Range: " + range);
        int r = 3;
        for (int i=0; i<nodes.length; i++) {
            float grey = (float) range.map(act[i]);
            g.setColor(new Color(grey, grey, grey));
            g.fillOval(2*nodes[i].x-r, 2*nodes[i].y-r, 2*r, 2*r);
        }
    }

    public void updateObjects(ArrayList<Drawable> objects) {
        // System.out.println("Objects: " + objects);
        synchronized (DisplayComponent.class) {
            this.objects = objects;
        }
        draw(getGraphics());
    }

    public DisplayComponent(int w, int h) {
        this.w = w;
        this.h = h;
        d = new Dimension(w, h);
        setBackground(Color.black);
        setFocusable(true);
    }

    public Dimension getPreferredSize() {
        return d;
    }
}
