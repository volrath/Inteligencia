package screenpac.extract;

import java.awt.*;
import java.util.ArrayList;

public class ENode implements Drawable, Constants {
    public int x;
    public int y;
    Color c;
    ArrayList<ENode> adj;

//    public ENode(int x, int y) {
//        this.x = x / DIV;
//        this.y = y / DIV;
//        // max 4 neighbours
//        adj = new ArrayList<ENode>(4);
//    }

    static int div = 2;
    public ENode(int x, int y, Color c) {
        this.x = x;
        this.y = y;
        this.c = c;
        // max 4 neighbours
        adj = new ArrayList<ENode>(4);
    }

    public String toString() {
        return x + " : " + y;
    }

    public boolean isJunction() {
        return adj.size() > 2;
    }

    public boolean isTerminal() {
        return adj.size() == 1;
    }

    public void draw(Graphics g) {
        if (isTerminal()) {
            g.setColor(Color.white);
            g.fillRect(x - 1, y - 1, 3, 3);
        } else {
            g.setColor(c);
            // g.fillRect(x - 1, y - 1, 3, 3);
            g.fillRect(x, y, 1, 1);
        }
    }

//    public void draw(Graphics g, int w, int h) {
//        if (isJunction() && false) {
//            g.setColor(Color.cyan);
//            g.fillOval(x - 4, y - 4, 9, 9);
//        } else {
//            g.setColor(Color.green);
//            g.fillRect(x - 1, y - 1, 3, 3);
//            // g.fillRect(x, y, 1, 1);
//        }
//    }
//
}
