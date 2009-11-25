package screenpac.log;

import screenpac.extract.Constants;

 /*
  The Event class will be used prior to each state event
 */

public class Event implements Constants {
    int desiredAgentDirection;
    boolean pillConsumed;
    boolean powerConsumed;
    boolean[] ghostEaten; // one entry for each ghost

    public Event(int desiredAgentDirection, boolean pillConsumed, boolean powerConsumed, boolean[] ghostEaten) {
        this.desiredAgentDirection = desiredAgentDirection;
        this.pillConsumed = pillConsumed;
        this.powerConsumed = powerConsumed;
        this.ghostEaten = ghostEaten;
    }

    public Event() {
        ghostEaten = new boolean[nGhosts];
    }
}
