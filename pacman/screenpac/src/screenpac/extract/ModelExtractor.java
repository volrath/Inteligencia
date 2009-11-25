package screenpac.extract;

import utilities.JEasyFrame;
import screenpac.model.*;
import screenpac.model.GameStateSetter;
import screenpac.util.ChamferDistance;
import games.pacman.maze.*;

import java.awt.*;

public class ModelExtractor {
    // the analysis will be like this

    /*
      1. Create the CaptureScreen
      2. Extract the old maze into the Chamfer Array
      3. And into the GameState and Map
      4. Process the extracted object into the GameState
    */

    CaptureTest ct;
    public GameStateSetter gs;
    GameStateView gsv;
    ChamferDistance cd;
    ChamferDistance cdPills;

    public ModelExtractor() throws Exception {
        OldMaze oldMaze = MazeOne.getMaze();
        makeChamfer(oldMaze);
        ct = new CaptureTest(400, 70, 260, 260);
        Maze maze = new Maze();
        maze.processOldMaze(oldMaze);
        gs = new GameStateSetter(maze);
        gsv = new GameStateView(gs);
        JEasyFrame fr = new JEasyFrame(gsv, "Game State", true);
        fr.setLocation(5, 300);
    }

    private void makeChamfer(OldMaze maze) {
        int nNodes = 0;
        int mag = 1;
        ENode[][] na = new ENode[maze.w * mag][maze.h * mag];
        ENode[][] naPill = new ENode[maze.w * mag][maze.h * mag];
        for (MazeNode n : maze.na) {
            nNodes++;
            // System.out.println(nNodes + "\t " + n.pill + "\t " + n.getPill());
            int x = n.x * mag;
            int y = n.y * mag;
            na[x][y] = new ENode(x, y, Color.blue);
            if (n.getPill() != MazeNode.NO_PILL) {
                // the color has no significance at the moment
                naPill[x][y] = new ENode(x, y, Color.white);
            }
        }
        System.out.println(nNodes);
        cd = new ChamferDistance();
        cd.setDistances(na);
        cdPills = new ChamferDistance();
        cdPills.limit = 3;
        cdPills.setDistances(naPill);
        // new JEasyFrame(new ChamferTest(cdPills), "CDPills", true);

    }

    public void cycle() {
        ct.analyse();
        gs.reset();
        // now what to do with what!
        // System.out.println("Offset: " + ct.ex.left + "\t " + ct.ex.top);
        // now take the agent and try to locate it in the other maze
        GameObjects gobs = ct.ex.gs;
        int xOff = cd.left - ct.ex.left;
        int yOff = cd.top - ct.ex.top;
        ENode ea = gobs.agent;
        boolean stateChanged = false;
        if (ea != null) {
            // System.out.println("Agent: " + ea.x + "\t " + ea.y);
            int x = ea.x + xOff;
            int y = ea.y + yOff;
            ENode node = cd.nd[x][y].node;
            // System.out.println("Mapped: " + current.x + "\t " + current.y );
            gs.setAgent(node.x, node.y);
            stateChanged = true;
        }
        // now set the ghosts
        int ix = 0;
        for (ENode en : gobs.ghosts) {
            int x = en.x + xOff;
            int y = en.y + yOff;
            ENode node = cd.nd[x][y].node;
            // System.out.println("Mapped: " + current.x + "\t " + current.y );
            gs.setGhost(node.x, node.y, ix);
            ix++;
            stateChanged = true;
        }
        // now set the pills
        // System.out.println("cdPills: " + cdPills.nd);
        for (ENode en : gobs.pills) {
            int x = en.x + xOff;
            int y = en.y + yOff;
            // check they're in range
            if (x >= 0 && y >= 0 && x < cd.nd.length && y < cd.nd[0].length
                    && cdPills.nd[x][y] != null) {
                ENode node = cdPills.nd[x][y].node;
                // System.out.println("Mapped: " + current.x + "\t " + current.y );
                gs.setPill(node.x, node.y);
                stateChanged = true;
            }
        }

        if (stateChanged) {
            gsv.repaint();
        }
    }

    public static void main(String[] args) throws Exception {
        int delay = 200;
        ModelExtractor extractor = new ModelExtractor();
        // System.exit(0);
        while (true) {
            try {
                Thread.sleep(delay);
                extractor.cycle();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
