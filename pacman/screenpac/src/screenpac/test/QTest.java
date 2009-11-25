package screenpac.test;

import java.util.Queue;
import java.util.LinkedList;

public class QTest {
    public static void main(String[] args) {
        Queue<Integer> q = new LinkedList<Integer>();
        for (int i=0; i<5; i++) {
            q.add(0);
        }
        for (int i=10; i<20; i++) {
            q.add(i);
            int x = q.remove();
            System.out.println(x);
            System.out.println(q);
        }
    }
}
