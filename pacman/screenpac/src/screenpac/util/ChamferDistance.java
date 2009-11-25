package screenpac.util;

import screenpac.util.ColorLUT;
import screenpac.extract.Constants;
import screenpac.extract.NodeDist;
import screenpac.extract.ENode;
import screenpac.extract.MapMatcher;

import java.awt.*;
import java.awt.image.BufferedImage;

import utilities.StatSummary;

public class ChamferDistance implements Constants {
    public NodeDist[][] nd;
    public ENode[][] na;
    int[] pix;
    int minDist = 0;
    int maxDist = 1;
    int w, h;
    BufferedImage bi;
    public int limit = 20;
    StatSummary sx, sy;
    public int left;
    public int top;

    public void setDistances(ENode[][] na) {
        this.na = na;
        w = na.length;
        h = na[0].length;
        nd = new NodeDist[w][h];
        pix = new int[w * h];
        bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        // step over each of the nodes in the current array
        // and perform a recursive search from that point
        // outwards
        sx = new StatSummary();
        sy = new StatSummary();
        // note: this only works when the tunnels go out horizontally
        ENode first = null, last = null;
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                if (na[i][j] != null) {
                    if (first == null) first = na[i][j];
                    last = na[i][j];
                    prop(i, j, na[i][j], 0);
                    sx.add(i);
                    sy.add(j);
                }
            }
        }
//        System.out.println("Top left: " + first);
//        System.out.println("Bottom Right: " + last);
//        System.out.println("maxDist = " + maxDist);
//        System.out.println(sx.min() + "\t " + sx.max());
//        System.out.println(sy.min() + "\t " + sy.max());
//        System.out.println(sx.max() - sx.min());
//        System.out.println(sy.max() - sy.min());
//        System.out.println(sx.mean() + "\t " + sy.mean());
        left = MapMatcher.sigLeft(na);
        top = MapMatcher.sigTop(na);
        System.out.println("MapMatch: " + left+ "\t " + top);

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                double x = 1;
                if (nd[i][j] != null) {
                    x = nd[i][j].dist / (double) maxDist;
                }
                pix[i + w * j] = ColorLUT.intensity(x);
            }
        }
        bi.setRGB(0, 0, w, h, pix, 0, w);
    }

    public void prop(int i, int j, ENode node, int dist) {
        if (dist < limit && (nd[i][j] == null || dist < nd[i][j].dist)) {
            nd[i][j] = new NodeDist(node, dist);
            maxDist = Math.max(dist, maxDist);
            // try propagating in all directions
            // dx[0],dy[0] = neutral, so start at 1
            for (int d = 0; d < dx.length-1; d++) {
                prop((i + w + dx[d]) % w, (j + h + dy[d]) % h, node, dist + 1);
            }
        }
    }

    public void draw(Graphics g) {
        g.drawImage(bi, 0, 0, w, h, null);
    }
}
