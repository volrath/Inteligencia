package pacman;

import java.util.*;


public class SimpleExtractor {

    // this will hunt through a pixel array
    // adding all the connected components to
    // itself

    // the background - components of this will not be created

    // this needs to be modified in order to keep track of existing agents,
    // rather than making a new agent every time that consume() is called

    static int BG = 0;
    static int scoreColor = 14342911;
    static int powers = 0;

    int w, h;
    IntStack stack;
    HashSet uniques;
    // Agent agent;
    GameState gs;

    public SimpleExtractor(int w, int h) {
        this.w = w;
        this.h = h;
        int size = 4 * w * h;
        stack = new IntStack(size);
        uniques = new HashSet();
        // agent = new Agent();
        gs = new GameState();
    }

    public ArrayList<Drawable> consume(int[] pix, Set<Integer> colors) {
        ArrayList<Drawable> objects = new ArrayList<Drawable>();

        for (int p = 0; p < pix.length; p++) {
            if ((pix[p] & 0xFFFFFF) != BG && colors.contains(pix[p])) {
		System.out.println(" adding " + pix[p]);
                ConnectedSet cs = consume(pix, p, pix[p]);
                objects.add(cs);
                gs.update(cs, pix);
            }
        }
        objects.add(gs);
        return objects;
    }


    public ConnectedSet consume(int[] pix, int p, int fg) {
        ConnectedSet cs = new ConnectedSet(p % w, p / w, fg);
// push the current pixel on the stack
        stack.reset();
// int p = x + y * w;
        stack.push(p);
// int count = 0;
// System.out.println( stack );
        while (!stack.isEmpty()) {
// count++;
            p = stack.pop();
            if (pix[p] == fg) {
// System.out.println(cx + " : " + cy + " : " + pix[p] );
// System.in.read();
                cs.add(p % w, p / w, p, pix[p]);
                pix[p] = 0;
                int cx = p % w;
                int cy = p / w;
                if (cx > 0) {
                    stack.push(p - 1);
                }
                if (cy > 0) {
                    stack.push(p - w);
                }
                if (cx < (w - 1)) {
                    stack.push(p + 1);
                }
                if (cy < (h - 1)) {
                    stack.push(p + w);
                }
            }
        }
        return cs;
    }
}
