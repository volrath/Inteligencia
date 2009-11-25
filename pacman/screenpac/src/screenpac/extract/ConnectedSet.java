package screenpac.extract;


import java.awt.*;

public class ConnectedSet implements Constants, Drawable {
    // private ArrayList pixels;
    int x, y;
    int width, height;
    int fg; // the value of the FG pixels
    int xMin, xMax, yMin, yMax;
    int pTot;
    int tot;
    Color c;
    public int px, py;
    boolean valid = false;

    public void draw(Graphics g) {
        // int div = 1;
        validate();
        g.setColor(c);
        if (ghostLike() || isPacMan()) {
            g.fillRect(xMin, yMin, width, height);
            // System.out.println(width + " : " + height);
        } else {
            if (powerPill() || pill() && true) {
                g.drawRect(xMin, yMin, width + 1, height + 1);
            }
        }
    }

    public void validate() {
        if (!valid) {
            width = xMax - xMin;
            height = yMax - yMin;
            // px = xMin + (xMax - xMin) / div;
            // py = yMin + (yMax - yMin) / div;
            valid = true;
        }
    }

    public boolean ghostLike() {
        validate();
        return (edible() || ghostColor(fg)) && width >= 10 && height >= 10;
        // return width == 13 && height == 13; // fg == MsPacInterface.inky;
    }

    public boolean edible() {
        validate();
        return fg == edible && width >= 10 && height >= 10;
        // return width == 13 && height == 13; // fg == MsPacInterface.inky;
    }

    public boolean ghostColor(int c) {
        return c == blinky ||
                c == pinky ||
                c == inky ||
                c == sue;
    }

    public boolean isPacMan() {
        validate();
        return fg == pacMan && width >= 8 && height >= 8;
        // return width == 13 && height == 13; // fg == MsPacInterface.inky;
    }

    public boolean pill() {
        validate();
        return width == 1 && height == 1; // && fg == pill; // between(width, 2, 3) && between(height, 2, 3);
    }

    public boolean powerPill() {
        validate();
        return width == 7 && height == 7; // between(width, 2, 3) && between(height, 2, 3);
    }

    public boolean gameObject() {
        return ghostLike() || pill() || powerPill() || isPacMan();
    }

    public static boolean between(int x, int low, int high) {
        return x >= low && x <= high;
    }

    public int x() {
        return (xMin + (xMax - xMin) / 2) / DIV;
    }

    public int y() {
        return (yMin + (yMax - yMin) / 2) / DIV;
    }

    public void rescale(double fac) {
        // do nothing
    }

    public ConnectedSet(int x, int y, int fg) {
        // pixels = new ArrayList();
        this.x = x;
        this.y = y;
        xMin = x;
        xMax = x;
        yMin = y;
        yMax = y;
        this.fg = fg;
        c = new Color((fg & 0xFF0000) >> 16, (fg & 0xFF00) >> 8, (fg & 0xFF));
        // System.out.println("Color: " + c + " : " + fg);
    }

    public void add(int px, int py, int pos, int val) {
        xMin = Math.min(px, xMin);
        xMax = Math.max(px, xMax);
        yMin = Math.min(py, yMin);
        yMax = Math.max(py, yMax);
        pTot += (1 + px - x) * (1 + py - y);
        tot++;
        valid = false;
    }

    public void freeze() {
    }

    public int hashCode() {
        return pTot;
    }

    public boolean equals(Object obj) {
        ConnectedSet cs = (ConnectedSet) obj;
        return cs.pTot == pTot;
    }

    public String toString() {
        return x + " : " + y + " : " + pTot;
    }

//    public int dist(Position p) {
//        return (int) Math.sqrt(sqr(x - p.x) + sqr(y - p.y));
//    }

    //
    public static int sqr(int x) {
        return x * x;
    }
}
