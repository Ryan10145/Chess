package main;

import javax.swing.*;

public class Chess
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Chess");
        frame.setContentPane(new MainPanel());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}