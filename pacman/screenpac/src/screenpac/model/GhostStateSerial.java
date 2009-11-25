package screenpac.model;

public class GhostStateSerial {

    public int edibleTime;
    int current, previous;
    int curDir;
    int delay;
    int delayCounter;
    int returnNode;

    public GhostStateSerial(GhostState gs) {
        edibleTime = gs.edibleTime;
        current = gs.current.nodeIndex;
        previous = gs.previous.nodeIndex;
        curDir = gs.curDir;
        delay = gs.delay;
        delayCounter = gs.delayCounter;
        returnNode = gs.returnNode.nodeIndex;
    }

    public GhostState getGhostState(MazeInterface maze) {
        GhostState gs = new GhostState();
        gs.edibleTime = edibleTime;
        gs.current = maze.getNode(current);
        gs.previous = maze.getNode(previous);
        gs.curDir = curDir;
        gs.delay = delay;
        gs.delayCounter = delayCounter;
        gs.returnNode = maze.getNode(returnNode);
        return gs;
    }
}
