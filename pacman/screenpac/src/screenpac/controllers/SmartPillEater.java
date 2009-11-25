package screenpac.controllers;

import screenpac.model.Node;
import screenpac.model.GameState;
import screenpac.model.GameStateInterface;
import screenpac.features.NearestPillDistance;
import screenpac.features.Utilities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SmartPillEater implements AgentInterface {
    /*
    This aims to be a smart version of the PillEater.
    It takes into account where it is and where it was
    previously, and issues a turn command before it gets
    to the turn.
     */
    NearestPillDistance npd;
    Node prev;
    List<Node> history;
    int hLength = 10;
    int nStuck = 0;

    public SmartPillEater() {
        npd = new NearestPillDistance();
        history = new LinkedList<Node>();
    }

    private void rememberIfNew(Node cur) {
        // only add the current current to the history if it's different
        // to the last one there
        // an alternative way to see this is to
        // the most recently added item goes at the back of the history
        // (it could equally well have gone at the front - ok - let's make it do that
        if (history.size() == 0 || history.get(0) != cur) {
            history.add(0, cur);
            nStuck = 0;
            if (history.size() > hLength) {
                history.remove(history.size() - 1);
            }
        } else {
            nStuck++;
        }
    }

    public int action(GameStateInterface gs) {
        // choose the adjacent current with the
        // nearest pill
        Node current = gs.getPacman().current;
        // store it in the history
        rememberIfNew(current);
        npd.score(gs, current);
        // Node target = Util.getMin(current.adj, npd, gs);
        // now get a current list: the shortest path to this target
        PathPlanner pp = new PathPlanner(gs.getMaze());
        if (npd.closest == null) {
            // something's gone wrong
            System.out.println("Error: null target in PillEater.action");
            // prev = current;
            return 0;
        } else {
            // find the direction to this current
            ArrayList<Node> path = pp.getPath(current, npd.closest);
            System.out.println("Path length: " + path.size());
            Node next = path.get(0);
            int ret = Utilities.getDirection(current, next);
            if (distanceToTurn(path) < 4) {
                return nextDir;
            } else {
                return ret;
            }
            // prev = current;
            // return ret;
        }
    }

    static int lookAhead = 5;

    // bit sloppy: nextDir is used to pass the next direction back
    int nextDir = 0;

    public int distanceToTurn(ArrayList<Node> path) {
        // if the plan is very short don't worry about the turn
        // return the lookAhead distance, which means that there's no turn in sight
        if (path.size() < 2 || history.size() == 0) return lookAhead;
        // ok: the planned path is at least two nodes long and might
        // involve a turn: let's check
        // but note about jumps in history!!!
        int cur = Utilities.getDirection(history.get(history.size() - 1), path.get(0));
        for (int i = 1; i < Math.min(lookAhead, path.size()); i++) {
            int dir = Utilities.getDirection(path.get(i - 1), path.get(i));
            if (dir != cur) {
                nextDir = dir;
                return i;
            }
        }
        return lookAhead;
    }

    public int oldaction(GameState gs) {
        // choose the adjacent current with the
        // nearest pill
        Node current = gs.pacMan.current;
        history.add(current);
        // keep the list trimmed to its maximum length
        if (history.size() > hLength) history.remove(0);

        Node target = Utilities.getMin(current.adj, npd, gs);
        // now get a current list: the shortest path to this target
        PathPlanner pp = new PathPlanner(gs.maze);
        if (target == null) {
            // something's gone wrong
            System.out.println("Error: null target in PillEater.action");
            // prev = current;
            return 0;
        } else {
            // find the direction to this current
            ArrayList<Node> path = pp.getPath(current, target);
            Node next = path.get(0);
            int ret = Utilities.getDirection(current, next);
            System.out.println("Distance to next: " + Utilities.manhattan(current, next));
            // get the current direction of travel
            if (prev != null && Utilities.manhattan(prev, current) <= 1) {
                int curDir = Utilities.getDirection(prev, current);
                // if we're heading in the correct direction
                // then see if we can make the turn early
                // but don't turn TOO early
                int lookAhead = 5;
                if (ret == curDir) {
                    for (int i = 1; i < Math.min(lookAhead, path.size()); i++) {
                        int dir = Utilities.getWrappedDirection(path.get(i - 1), path.get(i), gs.maze);
                        if (dir != curDir) {
                            ret = dir;
                            break;
                        }
                    }
                }
            }
            prev = current;
            return ret;
        }
    }


}