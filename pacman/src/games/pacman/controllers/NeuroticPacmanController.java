package games.pacman.controllers;

import games.pacman.core.FullGame;
import games.pacman.core.GameData;
import games.pacman.core.GameFrame;
import games.pacman.ghost.Ghost;
import games.pacman.maze.MazeNode;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;

import java.util.Vector;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: kristoffer
 * Date: Dec 4, 2009
 * Time: 12:08:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class NeuroticPacmanController implements PacController{
    public static final double MAXDIST = 158/16; 
    FullGame game;
    FeedForwardNeuralNetwork net;
    public int verbose = 0;


    public NeuroticPacmanController(FeedForwardNeuralNetwork net){
        this.net = net;
    }
    public NeuroticPacmanController(FullGame game, FeedForwardNeuralNetwork net){
        this.game = game;
        this.net = net;
    }

    public void setGame(FullGame game){
        this.game = game;
    }

    private Vector<Double> getInputs(FullGame game, MazeNode pacman_node) {
        Vector<Double> inputs = new Vector<Double>();
        GameData gamdat = new GameData(game.maze, game.ghosts, game.pacman);
        int i = 0;
        for (Ghost ghost:game.ghosts){
            if(ghost.edible()){
                inputs.add(new Double(0));
                inputs.add(new Double((double)game.maze.dist[pacman_node.ix][ghost.current.ix]/MAXDIST));
                if(verbose == 2) System.out.println("Dist. de Pacman a Ghost Comestible"+ i + " : "+(double)game.maze.dist[pacman_node.ix][ghost.current.ix]);
            }else{
                inputs.add(new Double((double)game.maze.dist[pacman_node.ix][ghost.current.ix]/MAXDIST));
                inputs.add(new Double(0));
                if(verbose == 2) System.out.println("Dist. de Pacman a Ghost "+ i + " : "+(double)game.maze.dist[pacman_node.ix][ghost.current.ix]);
            }
            i++;
        }

        inputs.add(new Double((double)pacman_node.x/MAXDIST));
        if(verbose == 2) System.out.println("Posicion en x de Pacman: "+pacman_node.x+" div: "+((double)pacman_node.x));
        inputs.add(new Double((double)pacman_node.y/MAXDIST));
        if(verbose == 2) System.out.println("Posicion en y de Pacman: "+pacman_node.y+" div: "+((double)pacman_node.y));
        inputs.add(new Double(gamdat.pillDist(pacman_node,game.maze.pills)/MAXDIST));
        if(verbose == 2) System.out.println("Posicion de la pastilla mas cercana a Pacman: "+gamdat.pillDist(pacman_node,game.maze.pills));
        inputs.add(new Double(gamdat.pillDist(pacman_node,game.maze.power)/MAXDIST));
        if(verbose == 2) System.out.println("Posicion de la power pill mas cercana a Pacman: "+gamdat.pillDist(pacman_node,game.maze.power));
        inputs.add(new Double(pacman_node.junctionDist/MAXDIST));
        if(verbose == 2) System.out.println("Posicion del cruce mas cercano a Pacman: "+pacman_node.junctionDist);

        return inputs;
    }

    public int getDirection() {
        Double output, bestEvaluation = 0.0, currentEvaluation = 0.0;
        int bestMove = 0;

        if (verbose > 0) System.out.println("----------------------------------------------");
        String[] directions = {"UP", "RIGHT", "DOWN", "LEFT"};
        for (int i = 0; i < 4; i++) { // up, right, down, left
            if (game.pacman.current.canMove(i)) {
                output = net.feedForward(this.getInputs(game, game.pacman.current.next(i))).get(0);
                if (output > bestEvaluation) {
                    bestEvaluation = output;
                    bestMove = i;
                    if(verbose > 0) System.out.println("Best output so far: " + output + ", Direction: " + directions[i]);
                }
                if (i == game.pacman.curDir())
                    currentEvaluation = output;
            }
        }
        if(verbose > 0) System.out.println("NET Output: " + directions[bestMove] + ", old direction: " + directions[game.pacman.curDir()]);
        if (Math.abs(game.pacman.curDir() - bestMove) == 2) { // if the new move is in the opposed direction of the old one..
            if (verbose > 0) System.out.println("Reevaluating...");
            if ((2.5 * (1.0 - bestEvaluation)) > (1.0 - currentEvaluation))
                bestMove = game.pacman.curDir();
            if(verbose > 0) System.out.println("NET Output: " + directions[bestMove]);
        }
        if (verbose > 0) System.out.println("----------------------------------------------");
        //Double output = net.feedForward(this.getInputs(game, game.pacman.current)).get(0);
//        if(output <= 0.25){
//            return LEFT;
//        }else if(output > 0.25 && output <= 0.5){
//            return RIGHT;
//        }else if(output > 0.5 && output <= 0.75){
//            return UP;
//        }else if(output > 0.75 && output <= 1){
//            return DOWN;                                      
//        }
        return bestMove;
    }
}
