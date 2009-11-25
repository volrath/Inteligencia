package screenpac.model;

import screenpac.controllers.AgentInterface;

public class AgentThread extends Thread {
    GameStateInterface gs;
    GameThread gt;
    AgentInterface agent;
    boolean alive;

    public AgentThread(AgentInterface agent) {
        this.agent = agent;
        alive = true;
        // start our own thread to handle the communication
        start();
    }

    // note: calls to wait and notify should be
    // in synchronized methods or blocks
    public synchronized void die() {
        alive = false;
        notify();
    }

    public synchronized void action(GameStateInterface gs, GameThread gt) {
        this.gs = gs;
        this.gt = gt;
        // waiting = false;
        notify();
    }

    public void run() {
        while (alive) {
            try {
                synchronized (this) {
                    wait();
                }
                int dir = agent.action(gs);
                gt.setAgentDir(dir);
                // now call the
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Agent thread died");
    }
}
