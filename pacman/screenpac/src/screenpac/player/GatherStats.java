package screenpac.player;

import screenpac.extract.ModelExtractor;
import screenpac.controllers.AgentInterface;
import screenpac.controllers.LeftRightTest;
import screenpac.control.PacMover;

import java.io.FileWriter;
import java.io.PrintWriter;

public class GatherStats {
    public static void main(String[] args) throws Exception {
        int delay = 20;
        ModelExtractor extractor = new ModelExtractor();
        AgentInterface controller = new LeftRightTest();
        PacMover pm = new PacMover();
        // System.exit(0);
        long start = System.currentTimeMillis();
        PrintWriter pr = new PrintWriter(new FileWriter("logs/log.txt"));
        int n = 0;
        while (n<500) {
            try {
                Thread.sleep(delay);
                // pr.println("Cycle: " + n++);
                // pr.println(System.currentTimeMillis() - start);
                extractor.cycle();
                pr.format("%d\t %d", n, extractor.gs.pacMan.current.x);
                pr.println();
                n++;
                // pr.println(System.currentTimeMillis() - start);
                // pr.println();
                int dir = controller.action(extractor.gs);
                // System.out.println(dir);
                pm.move(dir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pm.finalize();
        pr.close();
    }
}