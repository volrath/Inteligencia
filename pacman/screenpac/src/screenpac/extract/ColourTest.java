package screenpac.extract;

public class ColourTest {
    public static void main(String[] args) {
        System.out.println( 0xFF000000 | 222 << 16 | 222 << 8 | 222);
        System.out.println( 0xFF000000 | 33 << 16 | 33 << 8 | 222);
    }
}
