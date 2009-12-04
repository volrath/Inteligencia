package games.pacman.controllers;

import games.pacman.core.FullGame;
import games.pacman.core.GameData;
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
    FullGame game;
    FeedForwardNeuralNetwork net;
    public NeuroticPacmanController(FullGame game, FeedForwardNeuralNetwork net){
        this.game = game;
        this.net = net;
    }

    public int getDirection() {
        Vector<Double> inputs = new Vector<Double>();
        GameData gamdat = new GameData(game.maze, game.ghosts, game.pacman);
        for (Ghost ghost:game.ghosts){
            inputs.add(new Double(game.maze.dist[game.pacman.current.ix][ghost.current.ix]));
            if(ghost.edible()){
                inputs.add(new Double(game.maze.dist[game.pacman.current.ix][ghost.current.ix]));    
            }else{
                inputs.add(new Double(9999));
            }
        }
        inputs.add(new Double(game.pacman.current.x));
        inputs.add(new Double(game.pacman.current.y));
        inputs.add(new Double(gamdat.pillDist(game.pacman.current,game.maze.pills)));
        inputs.add(new Double(gamdat.pillDist(game.pacman.current,game.maze.power)));
        inputs.add(new Double(game.pacman.current.junctionDist));

        Double output = net.getPrediction(inputs).get(0);
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
