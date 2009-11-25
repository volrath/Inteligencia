package screenpac.player;

import screenpac.extract.ModelExtractor;
import screenpac.controllers.*;
import screenpac.control.PacMover;

public class TestPlayer {
    public static void main(String[] args) throws Exception {
        int delay = 20;
        ModelExtractor extractor = new ModelExtractor();
        AgentInterface controller = new SimplePillEater();
        PacMover pm = new PacMover();
        DirectionComponent dc = DirectionComponent.easyUse();

        // System.exit(0);
        while (true) {
            try {
                Thread.sleep(delay);
                extractor.cycle();
                int dir = controller.action(extractor.gs);
                // System.out.println(dir);
                pm.move(dir);
                dc.update(dir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
