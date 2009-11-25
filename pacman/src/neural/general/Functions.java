package neural.general;

/**
 * User: Simon
 * Date: 26-Mar-2008
 * Time: 09:11:24
 * <p/>
 * // don't forget the biases
 */
public class Functions {
    public static double tanh(double x) {
        return 2 / (1 + Math.exp(-2 * x)) - 1;
    }

    public static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    public static double dTanh(double x) {
        return 1 - x * x;
    }

    public static void tanh(double[] x) {
        for (int i = 0; i < x.length; i++) {
            x[i] = tanh(x[i]);
        }
    }

    public static void dTanh(double[] x, double[] y) {
        for (int i = 0; i < x.length; i++) {
            y[i] = dTanh(x[i]);
        }
    }

    public static void prod(double[] x, double[][] w, double[] y) {
        for (int i = 0; i < y.length; i++) {
            y[i] = 0;
            for (int j = 0; j < x.length; j++) {
                y[i] += w[i][j] * x[j];
            }
            // System.out.println(i + "\t " + y[i]);
        }
    }

    public static void prod2(double[] x, double[][] w, double[] y) {
        for (int i = 0; i < y.length; i++) {
            y[i] = 0;
            for (int j = 0; j < x.length; j++) {
                y[i] += w[j][i] * x[j];
            }
            // System.out.println(i + "\t " + y[i]);
        }
    }

    // used for calculation of weight updates
    public static void prod(double[] x, double[] y, double[][] w) {
//        System.out.println("GOT HERE!!!");
//        System.out.println(x + " : " + y + " : " + w);
//        System.out.format("%d \t %d \t %d,%d\n", x.length, y.length, w.length, w[0].length);
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < y.length; j++) {
                w[i][j] = x[i] * y[j];
            }
        }
    }

    public static void prod(double[] x, double[] y, double[] z) {
        for (int i = 0; i < x.length; i++) {
            z[i] = x[i] * y[i];
        }
    }

    public static void add(double[] x, double[] y, double[] z) {
        for (int i = 0; i < x.length; i++) {
            z[i] = x[i] + y[i];
        }
    }

    public static void add(double[] x, double[] y, double[] z, double w) {
        for (int i = 0; i < x.length; i++) {
            z[i] = x[i] + y[i] * w;
        }
    }

    // scalar version
    public static void add(double s, double[] y, double[] z, double w) {
        for (int i = 0; i < y.length; i++) {
            z[i] = s + y[i] * w;
        }
    }

    public static void add(double[][] x, double[][] y, double[][] z, double w) {
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                z[i][j] = x[i][j] + y[i][j] * w;
            }
        }
    }

    public static void sub(double[] x, double[] y, double[] z) {
        for (int i = 0; i < x.length; i++) {
            z[i] = x[i] - y[i];
        }
    }

    public static void copy(double[] ipv, double[] ip) {
        for (int i = 0; i < ipv.length; i++) {
            ip[i] = ipv[i];
        }
    }

    public static void normalise(double[] v) {
        // calculate squared total then divid through by sqrt of that
        double tot = 0;
        for (int i = 0; i < v.length; i++) {
            tot += v[i] * v[i];
        }
        double div = Math.sqrt(tot);
        if (div > 0) {
            for (int i = 0; i < v.length; i++) {
                v[i] /= div;
            }
        }
    }

    public static double dist2(double[] a, double[] b) {
        double tot = 0;
        for (int i = 0; i < a.length; i++) {
            tot += (a[i] - b[i]) * (a[i] - b[i]);
        }
        return tot;
    }

    public static double dist(double[] a, double[] b) {
        return Math.sqrt(dist2(a, b));
    }

    public static double[] clone(double[] v) {
        double[] a = new double[v.length];
        copy(v, a);
        return a;
    }

    public static double max(double[] vec) {
        double m = vec[0];
        for (int i = 1; i < vec.length; i++) {
            m = Math.max(m, vec[i]);
        }
        return m;
    }

    public static double min(double[] vec) {
        double m = vec[0];
        for (int i = 1; i < vec.length; i++) {
            m = Math.min(m, vec[i]);
        }
        return m;
    }

    public static double max(double[][] vec) {
        double m = vec[0][0];
        for (int i = 0; i < vec.length; i++) {
            for (int j = 0; j < vec[0].length; j++) {
                m = Math.max(m, vec[i][j]);
            }
        }
        return m;
    }

    public static double min(double[][] vec) {
        double m = vec[0][0];
        for (int i = 0; i < vec.length; i++) {
            for (int j = 0; j < vec[0].length; j++) {
                m = Math.min(m, vec[i][j]);
            }
        }
        return m;
    }

    public static void set(double[] vec, double x) {
        for (int i = 0; i < vec.length; i++) {
            vec[i] = x;
        }
    }

    public static double dotProd(double[] vec, double[] dir) {
        double sum = 0;
        for (int i = 0; i < vec.length; i++) {
            sum += vec[i] * dir[i];
        }
        return sum;
    }
}
