package screenpac.model;

public class GameStateSerial {
    // represents the game state in a way that's more
    // efficient for serialisation but less convenient
    // the design is deliberately rather flat
    // Nodes are stored as ints (using the nodeIndex property of a Node)

    int agentPos;
    GhostStateSerial[] ghosts;

    
}
