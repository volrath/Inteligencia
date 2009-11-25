package screenpac.controllers;

import screenpac.model.GameStateInterface;
import screenpac.model.Node;
import screenpac.extract.Constants;
import screenpac.features.Utilities;

import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

public class RandomNonReverseAgent implements AgentInterface, Constants {
    Node prev;
    public int action(GameStateInterface gs) {
//        try {Thread.sleep(2000);}
//        catch(Exception e) {}
        Node cur = gs.getPacman().current;
        ArrayList<Node> possibles = new ArrayList<Node>();
        for (Node n : cur.adj) {
            if (!n.equals(prev)) possibles.add(n);
        }
        Node next = possibles.get(rand.nextInt(possibles.size()));
        prev = cur;
        int action =  Utilities.getWrappedDirection(cur, next, gs.getMaze());
        return action;
    }
}
