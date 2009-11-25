package screenpac.controllers;

import utilities.JEasyFrame;

import javax.swing.*;
import java.awt.*;

import screenpac.extract.Constants;

/**
 * User: Simon
 * Date: 14-Mar-2007
 * Time: 22:19:47
 */

public class DirectionComponent extends JComponent implements Constants {

    public static void main(String[] args) throws Exception {
        DirectionComponent dc = easyUse();
        for (int i=0; i<100; i++) {
            int dir = i % 5;
            System.out.println("Dir: " + dir);
            dc.update(dir);
            Thread.sleep(2000);
        }
    }

    public static DirectionComponent easyUse() {
        DirectionComponent dc = new DirectionComponent();
        JPanel p = new JPanel();
        p.add(dc);
        JEasyFrame f = new JEasyFrame(p, "Test", true);
        f.setLocation(250, 320);
        return dc;
    }

    int w = 50;
    int h = 50;
    int[] x = new int[3];
    int[] y = new int[3];


    public void paintComponent(Graphics gg) {
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.black);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.cyan);

        // this is
        if (dir == NEUTRAL) {
            g.fillRect(w/3, h/3, w/3, h/3);
        } else {
            g.rotate(dir * 90 * Math.PI / 180, w/2, h / 2);
            x[0] = w/3; y[0] = h/3;
            x[1] = w/2; y[1] = 0;
            x[2] = 2 * w/3; y[2] = h/3;
            g.fillPolygon(x, y, 3);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(w, h);
    }

    int dir;

    public void update(int dir) {
        this.dir = dir;
        repaint();
    }

}
