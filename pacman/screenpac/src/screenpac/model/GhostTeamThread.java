package screenpac.model;

import screenpac.ghosts.GhostTeamController;

public class GhostTeamThread extends Thread {
    GameStateInterface gs;
    GameThread gt;
    GhostTeamController team;
    boolean alive;

    public GhostTeamThread(GhostTeamController team) {
        this.team = team;
        // start our own thread to handle the communication
        alive = true;
        start();
    }

    public synchronized void action(GameStateInterface gs, GameThread gt) {
        this.gs = gs;
        this.gt = gt;
        notify();
    }

    public synchronized void die() {
        alive = false;
        notify();
    }

    public void run() {
        while (alive) {
            try {
                // wait for the call to action to wake up
                synchronized (this) {
                    // only put the wait inside the synchronized block
                    wait();
                }
                int[] dir = team.getActions(gs);
                gt.setGhostTeamDirs(dir);
                // now call the
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Ghost team thread died");
    }
}