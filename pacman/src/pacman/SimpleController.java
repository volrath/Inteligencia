package pacman;

/**
 * User: Simon
 * Date: 09-Mar-2007
 * Time: 11:39:26
 * This class finds the closest pill, and tries to eat it.
 * Start very simple:
 *
 *  find velocity by comparing current position with previous position
 *  also consider previous output
 *
 *  if the previous output led to zero velocity, might want to do something else?
 *
 *  or even simpler, find the direction to the closest pill 
 *
 */
public class SimpleController {
    int prevAction = -1;
    public int getAction(GameState gs, PacMover pm) {
        Agent pac = null;
        // get the position of the agent
        return 0;
    }
}
