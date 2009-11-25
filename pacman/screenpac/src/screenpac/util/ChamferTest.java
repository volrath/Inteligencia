package screenpac.util;

import screenpac.extract.ENode;

import javax.swing.*;

import utilities.JEasyFrame;
import utilities.ElapsedTimer;

import java.awt.*;

public class ChamferTest extends JComponent  {
    ChamferDistance cd;

    public ChamferTest(ChamferDistance cd) {
        this.cd = cd;
    }

    public void paintComponent(Graphics g) {
        cd.draw(g);
    }

    public static void main(String[] args) {
        ElapsedTimer t = new ElapsedTimer();

        ENode[][] na = new ENode[100][100];
        na[50][50] = new ENode(50, 50, Color.white);
        ChamferDistance cd = new ChamferDistance();
        cd.setDistances(na);
        System.out.println(t);
        cd.setDistances(na);
        System.out.println(t);
        new JEasyFrame(new ChamferTest(cd), "ChamferTest", true);
    }

    public Dimension getPreferredSize() {
        return new Dimension(cd.w,  cd.h);
    }
}
