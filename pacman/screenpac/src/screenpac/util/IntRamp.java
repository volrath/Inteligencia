package screenpac.util;

public class IntRamp {
    int n;
    public int[] a;
    int cur;
    int ix;

    public static void main(String[] args) {
        IntRamp ir = new IntRamp(1024, 128);
        ir.prog(127, 1);
        ir.prog(257, 0);
        ir.prog(255, -1);
        ir.prog(385, 0);
    }

    public IntRamp(int n, int start) {
        this.n = n;
        cur = start;
        ix = 0;
        a = new int[n];
    }

    public void prog(int ns, int inc) {
        for (int i=0; i<ns; i++) {
            a[ix] = cur;
            // System.out.println(ix + "\t " + cur);
            ix++; cur += inc;
        }
    }
}
