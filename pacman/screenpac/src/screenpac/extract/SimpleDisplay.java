package screenpac.extract;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SimpleDisplay extends JComponent {
    int w, h;
    Dimension d;
    ArrayList<Drawable> objects;
    GameObjects gs;

    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, w, h);
        draw(g);
    }

    public void redraw() {
        // draws without overwriting
        draw(getGraphics());
    }

    public void draw(Graphics g) {
        if (gs != null && g != null) {
            // ensure only one thread gets through this at any one time
            synchronized (SimpleDisplay.class) {
//                System.out.println("Drawing: " + objects.size());
                gs.draw(g);
//                for (Drawable d : objects) {
//                    // System.out.println("Trying to draw " + d + " on " + g);
//                    d.draw(g);
//                }
            }
        }
    }

    public void updateObjects(GameObjects gs) {
        // System.out.println("Objects: " + objects);
        synchronized (SimpleDisplay.class) {
            this.gs = gs;
        }
        repaint();
        // draw(getGraphics());
    }

    public SimpleDisplay(int w, int h) {
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
