package screenpac.controllers;

import screenpac.model.GameStateInterface;
import screenpac.extract.Constants;

public class RandomAgent implements AgentInterface, Constants {
    public int action(GameStateInterface gs) {
        return rand.nextInt(dx.length); 
    }
}
