package screenpac.extract;

import java.util.ArrayList;
import java.awt.*;

public class GameObjects implements Drawable {
    public ENode agent;
    public ArrayList<ENode> powers;
    public ArrayList<ENode> nodes;
    public ArrayList<ENode> pills;
    public ArrayList<ENode> ghosts;


    public GameObjects() {
        powers = new ArrayList<ENode>();
        pills = new ArrayList<ENode>();
        ghosts = new ArrayList<ENode>();
        nodes = new ArrayList<ENode>();
    }

    public void update(ConnectedSet cs) {
        if (cs.pill()) {
            // System.out.println(cs.c);
            pills.add(new ENode(cs.x(), cs.y(), cs.c));
        } else if (cs.isPacMan()) {
            agent = new ENode(cs.x()-3, cs.y(), cs.c);
        }
        else if (cs.ghostLike()) {
            ghosts.add(new ENode(cs.x(), cs.y(), cs.c));
        } else if (cs.powerPill()) {
            // +1 corrects a systematic error in power pill extraction
            powers.add(new ENode(cs.x(), cs.y(), Color.blue));
        }
    }

    public void reset() {
        powers.clear();
        pills.clear();
        ghosts.clear();
    }

    public void draw(Graphics g) {
        if (nodes != null) {
            for (ENode n : nodes) {
                n.draw(g);
            }
            // System.out.println("Drew nodes: " + nodes.size());
        }
        if (agent != null) agent.draw(g);
        if (powers != null) {
            for (ENode n : powers) {
                n.draw(g);
            }
        }
    }

    public void setNodes(ArrayList<ENode> nodes) {
        this.nodes = nodes;
    }
}
