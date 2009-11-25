package screenpac.controllers;

import screenpac.model.GameStateSetter;
import screenpac.model.GameState;
import screenpac.model.GameStateInterface;

public interface AgentInterface {
    // the return value specifies the direction of the joystick
    public int action(GameStateInterface gs);
}
