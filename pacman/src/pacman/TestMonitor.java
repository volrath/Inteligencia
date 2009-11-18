
package pacman;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * User: Simon
 * Date: 13-Oct-2007
 * Time: 18:51:28
 *
 * The purpose of this class is to consistently take in the game state,
 * including the position of the agent, and also the selected move to
 * each point in time, and work out the delays between each
 *
 */
public class TestMonitor {

    long start;
    int limit = 30000;
    int n = 0;

    PrintWriter bw;

    public TestMonitor() throws Exception {
        start = System.currentTimeMillis();
        bw = new PrintWriter(new FileWriter("log.txt"));
    }

    public void log(int dir, GameState gs) throws Exception {
        long elapsed = System.currentTimeMillis() - start;
        bw.println(dir + "\t " + gs.agent.x + "\t " + elapsed);
        start = System.currentTimeMillis();
        n++;
        if (n > limit) {
            bw.close();
            System.exit(0);
        }

    }


//    public int getMove(GameState gs) {
//
//    }
//

}
