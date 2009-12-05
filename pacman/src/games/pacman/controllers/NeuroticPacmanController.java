package games.pacman.controllers;

import games.pacman.core.FullGame;
import games.pacman.core.GameData;
import games.pacman.core.GameFrame;
import games.pacman.ghost.Ghost;
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
    public boolean verbose = false;


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

    public int getDirection() {
        Vector<Double> inputs = new Vector<Double>();
        GameData gamdat = new GameData(game.maze, game.ghosts, game.pacman);
        int i = 0;
        for (Ghost ghost:game.ghosts){
            inputs.add(new Double((double)game.maze.dist[game.pacman.current.ix][ghost.current.ix]/MAXDIST));
            if(verbose) System.out.println("Dist. de Pacman a Ghost "+ i + " : "+(double)game.maze.dist[game.pacman.current.ix][ghost.current.ix]/MAXDIST);
            if(ghost.edible()){
                inputs.add(new Double((double)game.maze.dist[game.pacman.current.ix][ghost.current.ix]/MAXDIST));
                if(verbose) System.out.println("Dist. de Pacman a Ghost Comestible"+ i + " : "+(double)game.maze.dist[game.pacman.current.ix][ghost.current.ix]/MAXDIST);
            }else{
                inputs.add(new Double(0));
            }
            i++;
        }
        inputs.add(new Double((double)game.pacman.current.x/MAXDIST));
        if(verbose) System.out.println("Posicion en x de Pacman: "+game.pacman.current.x+" div: "+((double)game.pacman.current.x/MAXDIST));
        inputs.add(new Double((double)game.pacman.current.y/MAXDIST));
        if(verbose) System.out.println("Posicion en y de Pacman: "+game.pacman.current.y+" div: "+((double)game.pacman.current.y/MAXDIST));
        inputs.add(new Double(gamdat.pillDist(game.pacman.current,game.maze.pills)/MAXDIST));
        if(verbose) System.out.println("Posicion de la pastilla mas cercana a Pacman: "+gamdat.pillDist(game.pacman.current,game.maze.pills)/MAXDIST);
        inputs.add(new Double(gamdat.pillDist(game.pacman.current,game.maze.power)/MAXDIST));
        if(verbose) System.out.println("Posicion de la power pill mas cercana a Pacman: "+gamdat.pillDist(game.pacman.current,game.maze.power)/MAXDIST);
        inputs.add(new Double(game.pacman.current.junctionDist/MAXDIST));
        if(verbose) System.out.println("Posicion del cruce mas cercano a Pacman: "+game.pacman.current.junctionDist/MAXDIST);

        Vector<Double> outputs = net.feedForward(inputs); 
        Double output = outputs.get(0);
        if(verbose) System.out.println("NET Output: "+output+" Size:"+outputs.size());
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
