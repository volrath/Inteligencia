package screenpac.test;

public class PrintPows {
    public static void main(String[] args) {
        printPows(4, 2);
    }

    static void printPows(int n, int p) {
        for (int i=1; i<= n; i++) {
            System.out.println(i + "    " + intPow(i, p));
        }
    }

    static int intPow(int a, int b) {
        int r = 1;
        for (int i=0; i<b; i++) {
            r *= a;
        }
        return r;
    }
}
