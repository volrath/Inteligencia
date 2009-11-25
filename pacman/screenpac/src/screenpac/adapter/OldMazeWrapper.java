package screenpac.adapter;

import games.pacman.maze.OldMaze;
import games.pacman.maze.MazeOne;
import games.pacman.maze.MazeNode;
import screenpac.extract.ENode;
import screenpac.util.ChamferDistance;
import screenpac.util.ChamferTest;

import java.util.ArrayList;
import java.awt.*;

import utilities.JEasyFrame;

public class OldMazeWrapper {
    public static void main(String[] args) {
        OldMaze maze = MazeOne.getMaze();
        int nNodes = 0;
        ArrayList<ENode> nodes = new ArrayList<ENode>();
        int mag = 1;
        ENode[][] na = new ENode[maze.w*mag][maze.h*mag];
        for (MazeNode n : maze.na) {
            nNodes++;
            // System.out.println(nNodes + "\t " + n.pill + "\t " + n.getPill());
            int x = n.x * mag;
            int y = n.y * mag;
            na[x][y] = new ENode(x, y, Color.blue);
        }
        System.out.println(nNodes);
        ChamferDistance cd = new ChamferDistance();
        cd.setDistances(na);

        new JEasyFrame(new ChamferTest(cd), "ChamferTest", true);

    }
}
