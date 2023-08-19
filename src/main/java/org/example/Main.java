package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Main extends JFrame {
    public static void main(String[] args) { new Main(); }

    public Main() {
        /// Creating window
        setTitle("Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(650, 700)); /// can change
        setLayout(null); /// can also change
        // test commit change 2

        /// Adding menu to JFrame
        MenuBar menu = new MenuBar();
        setJMenuBar(menu.getMenu());

        pack();
        setVisible(true);

    }

}