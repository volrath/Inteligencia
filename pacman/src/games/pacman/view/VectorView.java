package games.pacman.view;

import games.pacman.maze.MazeNode;
import neural.general.Functions;
import utilities.BarChart;
import utilities.JEasyFrame;
import utilities.Range;

import javax.swing.*;
import java.awt.*;

/**
 * User: Simon
 * Date: 14-May-2008
 * Time: 08:52:41
 * This class enables us to observe the i/o of
 * a vector controller
 */

public class VectorView extends JPanel {

    public static void main(String[] args) {
        int nDim = 2;
        VectorView vv = new VectorView(nDim);
        new JEasyFrame(vv, "Vector View", true);
        vv.setValue(null, new double[]{0, 1}, 1);
        vv.setValue(null, new double[]{1, 1}, 1);
        vv.setValue(null, new double[]{1, 0}, 1);
        vv.setValue(null, new double[]{0, 0}, 1);
        vv.updateView();
    }

    MazeNode previous;
    double min, max;

    BarChart[] bc;
    double[][] vecs;
    int nCharts = 4;
    int ix;

    public VectorView(int nDim) {
        setLayout(new FlowLayout());
        bc = new BarChart[nCharts];
        for (int i=0; i<nCharts; i++) {
            bc[i] = new BarChart(new Dimension(200, 50));
            add(bc[i]);
        }
        vecs = new double[nCharts][nDim];
        reset();
    }

    public void reset() {
        min = Double.MAX_VALUE;
        max = Double.MIN_VALUE;
        ix = 0;
    }


    public void updateView() {
        Range range = new Range(min, max);
        for (int i=0; i<nCharts; i++) {
            for (int j=0; j<vecs[i].length; j++) {
                vecs[i][j] = range.map(vecs[i][j]);
            }
            bc[i].update(vecs[i]);
        }
        // set the unused ones to max
        for (int i=ix; i<nCharts; i++) {
            Functions.set(vecs[i], 1);
        }
        // System.out.println("Updated view");
    }

    public void setValue(MazeNode node, double[] vec, double op) {
        max = Math.max(max, Functions.max(vec));
        min = Math.min(min, Functions.min(vec));
        // replace this with a copy to be more efficient
        Functions.copy(vec, vecs[ix]);
        ix++;
    }

    public void setPrevious(MazeNode previous) {

    }
}
