package screenpac.test;

import java.util.Queue;
import java.util.LinkedList;
import java.util.List;

public class ListTest {
    public static void main(String[] args) {
        List<Integer> q = new LinkedList<Integer>();
        for (int i=0; i<5; i++) {
            q.add(0);
        }
        for (int i=10; i<20; i++) {
            q.add(i);
            int x = q.remove(0);
            System.out.println(x);
            System.out.println(q);
        }
    }
}