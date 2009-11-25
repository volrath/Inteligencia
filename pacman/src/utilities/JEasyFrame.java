package utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JEasyFrame extends JFrame {
    public Component comp;
    public boolean exitOnClose;
    public static boolean EXIT_DEFAULT = false;
    public static String DEFAULT_TITLE = "Closeable Frame";

    static int nFrames = 0; // used to displace successive frames
    static int maxCount = 20;
    static int perFrame = 15;

    public void cleanup() {
        // override this to perform any tidying up necessary
    }

    /**

     Moves the frame to the center of the screen, offset
     each time by perFrame pixels

     */

    public JEasyFrame center() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frame = getSize();

        nFrames = (nFrames + 1) % maxCount;
        int offset = nFrames * perFrame;
        setBounds(screen.width / 2 - frame.width / 2 + offset,
                screen.height / 2 - frame.height / 2 + offset,
                frame.width, frame.height);

        return this;

    }

    public JEasyFrame fullScreen() {
        // method is buggy
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        // reshape( 0, 0, screen.width, screen.height );
        super.setState(Frame.MAXIMIZED_BOTH);
        return this;
    }

    public boolean canClose() {
        // override this to query the user etc before closing
        return true;
    }

    public JEasyFrame() {
        this(DEFAULT_TITLE, EXIT_DEFAULT);
    }

    public JEasyFrame(String title) {
        this(title, EXIT_DEFAULT);
    }

    public JEasyFrame(String title, boolean exit) {
        super(title);
        exitOnClose = exit;
        addWindowListener(new Closer());
    }

    class Closer extends WindowAdapter {
        public Closer() {
        }

        public void windowClosing(WindowEvent e) {
            tryClose();
        }
    }



    public JEasyFrame(Component comp, String title, boolean exit) {
        this(title, exit);
        this.comp = comp;
        getContentPane().add(BorderLayout.CENTER, comp);
        pack();
        // show();
        this.setVisible(true);
        repaint();
    }

    public void tryClose() {
        if (canClose()) {
            cleanup();
            if (exitOnClose)
                System.exit(0);
            else
                dispose();
        }
    }
}
