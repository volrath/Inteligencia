package games.pacman.test;

import utilities.JEasyFrame;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ButtonTest implements ActionListener  {
    String name;

    public ButtonTest(String name) {
        this.name = name;
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(name);
    }

    public static void main(String[] args) {
        JButton b1 = new JButton("Harry");
        b1.addActionListener(new ButtonTest("Harry"));
        JButton b2 = new JButton("Sally");
        b2.addActionListener(new ButtonTest("Sally"));
        JPanel p = new JPanel();
        p.add(b1);
        p.add(b2);
        new JEasyFrame(p, "Button Test", true);
    }
}
