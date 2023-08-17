package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class MenuBar implements ActionListener  {
    JMenu fileMenu, searchMenu, viewMenu, manageMenu, helpMenu;
    JMenuItem newItem, openItem, saveItem, exitItem;
    private JMenuBar menuBar;

    public void actionPerformed(ActionEvent event) {
        JComponent source = (JComponent) event.getSource(); /// Get source of action

        if (source == newItem) {
            /// Operations for new
        }

        if (source == saveItem) {
            /// Operations to save
        }

        if (source == openItem) {
            /// Operations to open text file
        }

        if (source == exitItem) {
            System.exit(0);
        }

    }


    public MenuBar() {
        menuBar = new JMenuBar();

        /// Creating menus
        fileMenu = new JMenu("File");
        searchMenu = new JMenu("Search");
        viewMenu = new JMenu("View");
        manageMenu = new JMenu("Manage");
        helpMenu = new JMenu("Help");

        /// Adding items and action listeners
        newItem = new JMenuItem("New");
        newItem.addActionListener(this);
        openItem = new JMenuItem("Open");
        openItem.addActionListener(this);
        saveItem = new JMenuItem("Save");
        saveItem.addActionListener(this);
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(this);

        /// Adding items to menus
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);

        /// Adding menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(searchMenu);
        menuBar.add(viewMenu);
        menuBar.add(manageMenu);
        menuBar.add(helpMenu);

    }

    public JMenuBar getMenu() { return menuBar; } /// Getter for main to retrieve menu

}
