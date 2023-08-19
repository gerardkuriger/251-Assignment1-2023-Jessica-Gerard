package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
public class MenuBar implements ActionListener  {
    JMenu fileMenu, searchMenu, viewMenu, manageMenu, helpMenu;
    JMenuItem newItem, openItem, saveItem, exitItem;
    private JMenuBar menuBar;
    // File properties might change depending on weather this schema works (May move to Main class but that will result in a dependent link between MenuBar and Main)
    private String FilePath = ""; // The Directory containing the file
    private String FileName = ""; // The File name of the current file
    private String FileType = ""; // The type of file i.e. txt and java

    public void actionPerformed(ActionEvent event) {
        JComponent source = (JComponent) event.getSource(); /// Get source of action
        if (source == newItem) {
            /// Operations for new
        }
        if (source == saveItem) {
            /// Operations to save
        }
        if (source == openItem) {
            System.out.println("Open");
            JFileChooser fileChooser = new JFileChooser(); // Open the file chooser dialog and allow the user to select the file they want to view
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog( null );

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                System.out.println(file.getPath());
                System.out.println(file.getName());
            }
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
