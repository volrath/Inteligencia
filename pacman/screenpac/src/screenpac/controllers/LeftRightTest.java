package screenpac.controllers;

import screenpac.model.Node;
import screenpac.model.GameStateInterface;
import screenpac.features.NearestPillDistance;
import screenpac.extract.Constants;

public class LeftRightTest implements AgentInterface, Constants {
    NearestPillDistance npd = new NearestPillDistance();
    boolean left = false;
    int nStuck = 0;
    Node prev = null;
    public int action(GameStateInterface gs) {
        // choose the adjacent current with the
        // nearest pill
        Node cur = gs.getPacman().current;

        if (cur == prev) nStuck++;
        else nStuck = 0;
        prev = cur;

        if (nStuck > 2) {
            left = !left;
            nStuck = 0;
        }
        if (left) return LEFT;
        else return RIGHT;
    }
}