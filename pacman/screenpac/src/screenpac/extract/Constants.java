package screenpac.extract;

import games.math.Vector2d;

import java.util.Random;
import java.awt.*;

public interface Constants {

    // DIV: a rescaling parameter that rescales
    // things to a coarser resolution
    // have a seeded (or non-seeded) random number gen

    // constants in an interface are public static by default
    Random rand = new Random();
    int nLives = 3;
    int nGhosts = 4;
    
    int DIV = 2;

    int MAG = 2;

    int UP = 0;
    int RIGHT = 1;
    int DOWN = 2;
    int LEFT = 3;
    int CENTRE = 4;
    int NEUTRAL = 4;
    int[] dx = {0, 1, 0, -1, 0};
    int[] dy = {-1, 0, 1, 0, 0};

    // this is how long each ghost is delayed at the start of a level
    // or when a Pac-Man loses a life
    int[] initGhostDelay = {0, 10, 20, 30};

    int blinky = -65536;
    int pinky = -18689;
    int inky = -16711681;
    int sue = -18859;
    int pacMan = -256;
    int edible = -14408449;
    int pill = -2434305;

    public static final int BG = -16777216;

    Color[] ghostColors = new Color[]{
            new Color(blinky),
            new Color(pinky),
            new Color(inky),
            new Color(sue),
    };
    Color edibleColor = new Color(100, 149, 237);

    // this determines the proximity (in manhattan distance) at which
    // an agent/ghost is eaten
    public static final int agentOverlapDistance = 1;

    // and similarly for pills and powerpills
    public static final int pillOverlapDistance = 3;

    public static int pillScore = 10;
    public static int powerScore = 50;
    public static int edibleGhostStartingScore = 200;

}
