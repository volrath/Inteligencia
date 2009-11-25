package games.pacman.view;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 29-Sep-2004
 * Time: 09:36:55
 * To change this template use Options | File Templates.
 */
public interface Drawable {
    public void draw(Graphics g, int w, int h);
    public void rescale(double fac);
    public final static int scale = 2;
}
