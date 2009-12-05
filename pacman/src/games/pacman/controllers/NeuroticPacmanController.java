package games.pacman.controllers;

import games.pacman.core.FullGame;
import games.pacman.core.GameData;
import games.pacman.core.GameFrame;
import games.pacman.ghost.Ghost;
import games.pacman.maze.MazeNode;
import neuralj.networks.feedforward.FeedForwardNeuralNetwork;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kristoffer
 * Date: Dec 4, 2009
 * Time: 12:08:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class NeuroticPacmanController implements PacController{
    public static final double MAXDIST = 158; 
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
                inputs.add(new Double((double)game.maze.dist[pacman_node.ix][ghost.current.ix]));
                if(verbose == 2) System.out.println("Dist. de Pacman a Ghost Comestible"+ i + " : "+(double)game.maze.dist[pacman_node.ix][ghost.current.ix]);
            }else{
                inputs.add(new Double((double)game.maze.dist[pacman_node.ix][ghost.current.ix]));
                inputs.add(new Double(0));
                if(verbose == 2) System.out.println("Dist. de Pacman a Ghost "+ i + " : "+(double)game.maze.dist[pacman_node.ix][ghost.current.ix]);
            }
            i++;
        }
        
        inputs.add(new Double((double)pacman_node.x));
        if(verbose == 2) System.out.println("Posicion en x de Pacman: "+pacman_node.x+" div: "+((double)pacman_node.x));
        inputs.add(new Double((double)pacman_node.y));
        if(verbose == 2) System.out.println("Posicion en y de Pacman: "+pacman_node.y+" div: "+((double)pacman_node.y));
        inputs.add(new Double(gamdat.pillDist(pacman_node,game.maze.pills)));
        if(verbose == 2) System.out.println("Posicion de la pastilla mas cercana a Pacman: "+gamdat.pillDist(pacman_node,game.maze.pills));
        inputs.add(new Double(gamdat.pillDist(pacman_node,game.maze.power)));
        if(verbose == 2) System.out.println("Posicion de la power pill mas cercana a Pacman: "+gamdat.pillDist(pacman_node,game.maze.power));
        inputs.add(new Double(pacman_node.junctionDist));
        if(verbose == 2) System.out.println("Posicion del cruce mas cercano a Pacman: "+pacman_node.junctionDist);

        return inputs;
    }

    public int getDirection() {
        Vector<Double> inputs, outputs = new Vector<Double>();
//        if (verbose > 0) System.out.println("----------------------------------------------");
//        for (int i = 0; i < game.pacman.current.next.length; i++) {
//            inputs = this.getInputs(game, game.pacman.current.next[i]);
//            outputs.add(net.feedForward(inputs).get(0));
//            if(verbose > 0) System.out.println("NET Output: " + outputs.get(i));
//        }
//        if (verbose > 0) System.out.println("----------------------------------------------");
        Double output = net.feedForward(this.getInputs(game, game.pacman.current)).get(0);
        if(output <= 0.25){
            return LEFT;
        }else if(output > 0.25 && output <= 0.5){
            return RIGHT;
        }else if(output > 0.5 && output <= 0.75){
            return UP;
        }else if(output > 0.75 && output <= 1){
            return DOWN;
        }
        return RIGHT;
    }
}
