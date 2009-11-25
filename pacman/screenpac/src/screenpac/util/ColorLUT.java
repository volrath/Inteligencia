package screenpac.util;

import utilities.Range;
import utilities.JEasyFrame;

import java.awt.*;

public class ColorLUT {
    // share the color map between all instances for now
    static int nColors = 1024;
    static int[] color;
    static Color[] cols;

    static {
        setColors();
    }

    public static int intensity(double x) {
        // x assumed to be between 0 and 1;
        return color[(int) (x * (nColors-1))];
    }

//    public static int intensity(double x) {
//        // x assumed to be between 0 and 1;
//        return color[(int) (x * (nColors-1))];
//    }



    public static void setColors() {
        nColors = 1024;
        // System.out.println("Red");
        IntRamp rr = new IntRamp(nColors, 0);
        rr.prog(384, 0); // set 0 .. 383
        rr.prog(255, 1);
        rr.prog(257, 0);
        rr.prog(128, -1);

        // System.out.println("Green");
        IntRamp gr = new IntRamp(nColors, 0);
        gr.prog(128, 0);
        gr.prog(255, 1);
        gr.prog(257, 0);
        gr.prog(255, -1);
        gr.prog(129, 0);

        // System.out.println("Blue");
        IntRamp br = new IntRamp(nColors, 128);
        br.prog(127, 1);
        br.prog(257, 0);
        br.prog(255, -1);
        br.prog(385, 0);

        color = new int[nColors];
        for (int i=0;i<nColors; i++) {
            int x = rr.a[i];
            x <<= 8;
            x |= gr.a[i];
            x <<= 8;
            x |= br.a[i];
            x |= 0xFF000000;
            color[i] = x;
            // System.out.println(i + "\t " + gr.a[i]);
        }
    }

    private static void setColors2() {
        nColors = 256 * 3;
        IntRamp rr = new IntRamp(nColors, 0);
        rr.prog(512, 0); // set 0 .. 383
        rr.prog(256, 1);

        IntRamp gr = new IntRamp(nColors, 0);
        gr.prog(256, 0);
        gr.prog(255, 1);
        gr.prog(257, 0);

        IntRamp br = new IntRamp(nColors, 0);
        br.prog(255, 1);
        br.prog(513, 0);

        color = new int[nColors];
        for (int i=0;i<nColors; i++) {
            int x = rr.a[i];
            x <<= 8;
            x |= gr.a[i];
            x <<= 8;
            x |= br.a[i];
            x |= 0xFF000000;
            color[i] = x;
        }
    }
}
