package screenpac.model;

import java.util.ArrayList;
import java.awt.*;

public class Node {
    // nodes carry pill and power pill indices which default to -1
    // meaning that there is neither a pill nor a power pill at this
    // current

    // the index is used to access a BitSet that contains the
    // state of the pills and power pills

    // in this way the game state can be cleanly separated from the
    // maze layout

    public int x,y;
    public int nodeIndex;

    public int pillIndex;
    public int powerIndex;
    public ArrayList<Node> adj;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        adj = new ArrayList<Node>();
        pillIndex = -1;
        powerIndex = -1;
    }

    public String toString() {
        return x + " \t " + y;
    }

    // colour should not really be defined here
    // it was a hack to enable display of various
    // node properties
    public Color col = Color.black;

}
