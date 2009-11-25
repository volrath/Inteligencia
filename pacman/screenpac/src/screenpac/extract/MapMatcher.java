package screenpac.extract;

public class MapMatcher {
    // static int
    public static int match(int[] a, int[] b) {
        // for (int off = 0
        // aim here is to calculate the offset between a and
        // b that produces the best macth
        return 0;
    }

    static int thresh = 10;

    public static int sigLeft(ENode[][] na) {
        // return the first value of x with a large enough col count
        for (int x = 0; x < na.length; x++) {
            int tot = 0;
            for (int y = 0; y < na[0].length; y++) {
                if (na[x][y] != null) tot++;
                if (tot > thresh) return x;
            }
        }
        return 0;
    }

    public static int sigTop(ENode[][] na) {
        // return the first value of x with a large enough col count
        for (int y = 0; y < na[0].length; y++) {
            int tot = 0;
            for (int x = 0; x < na.length; x++) {
                if (na[x][y] != null) tot++;
                if (tot > thresh) return y;
            }
        }
        return 0;
    }
}
