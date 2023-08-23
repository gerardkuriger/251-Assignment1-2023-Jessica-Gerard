package org.example;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import java.awt.Color;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main extends JFrame implements ActionListener {
    JMenu fileMenu, searchMenu, viewMenu, manageMenu, helpMenu;
    JMenuItem newItem, openItem, saveItem, exitItem, findItem, aboutItem;
    private static JTextArea area;
    private static Highlighter high;
    private Color highLighterColor = Color.ORANGE;
    private static int areaHash;

    public static void main(String[] args) { new Main(); }

    public Main() {
        /// Creating window
        setTitle("Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(650, 700)); /// can change
        setLayout(null); /// can also change

        /// Adding menu to JFrame
        JMenuBar menuBar = new JMenuBar();

        /// Creating menus
        fileMenu = new JMenu("File");
        searchMenu = new JMenu("Search");
        viewMenu = new JMenu("View");
        manageMenu = new JMenu("Manage");
        helpMenu = new JMenu("Help");

        /// Adding items and action listeners
        newItem = new JMenuItem("New");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save As");
        exitItem = new JMenuItem("Exit");
        findItem = new JMenuItem("Find");
        aboutItem = new JMenuItem("About");

        /// Create Action listeners
        newItem.addActionListener(this);
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);
        findItem.addActionListener(this);
        aboutItem.addActionListener(this);

        /// Adding items to menus
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        searchMenu.add(findItem);
        helpMenu.add(aboutItem);

        /// Adding menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(searchMenu);
        menuBar.add(viewMenu);
        menuBar.add(manageMenu);
        menuBar.add(helpMenu);

        // Set the menu bar
        setJMenuBar(menuBar);

        // Adding text field
        area = new JTextArea();
        area.setSize(650, 700);
        add(area);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JComponent source = (JComponent) event.getSource(); /// Get source of action
        if (source.equals(newItem)) {
            if ( area.getText().isEmpty() || areaHash == area.getText().hashCode() ){
                New();
            } else {
                System.out.println("Area is not blank");
                int res = JOptionPane.showConfirmDialog( this, "Save Changes?");
                System.out.println(res);
            }
            /// Operations for new
        }
        if (source.equals(saveItem)) {
            Save();
        }
        if (source.equals(openItem)) {
            Open(); // Create open function
        }
        if (source.equals(exitItem)) {
            System.exit(0);
        }
        if (source.equals(findItem)) {
            high = area.getHighlighter();
            high.removeAllHighlights(); // clear any preexisting highlights
            Search( JOptionPane.showInputDialog(this, "Find"), area.getText().toLowerCase(), 0 );
        }
        if (source.equals(aboutItem)) {
            String message = """
                    Team Members:
                    - Gerard Kuriger
                    - Jessica Lang\s
                     This is a text editor."""; /// Creating the message
            JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE); /// Displaying the information message
        }
    }

    private void New(){
        System.out.println("New");
        // If Test Field is not empty prompt to save


    }

    private void Open(){
        JFileChooser fileChooser = new JFileChooser(); // Open the file chooser dialog and allow the user to select the file they want to view
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog( null );

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                StringBuilder data = new StringBuilder(); // define string builder
                Scanner myReader = new Scanner(file); // Init Scanner

                while (myReader.hasNextLine()){
                    data.append(myReader.nextLine()).append('\n'); // get line and append new line to maintain formatting
                }
                areaHash = data.toString().hashCode();
                area.setText( data.toString() ); // set text area
                // Enable editing of text
            } catch (FileNotFoundException e) {
                System.err.println( "Error "+ e.getMessage() );
            }
        }
    }

    private void Search( String searchTerm, String searchText, int b ) { // finds first instance of
        System.out.println("Search");

        // If Editing is disabled then no new files have been started or no old ones opened

        if (searchText.contains(searchTerm.toLowerCase())) { // not case-sensitive
            System.out.println(searchTerm + " Found");
            try {
                int s = searchText.indexOf(searchTerm);

                Highlighter.HighlightPainter DefaultHighlightPainter = new DefaultHighlightPainter(highLighterColor);
                // Apply Beginning offset to account for removed searched section
                high.addHighlight(b + s, b + s + searchTerm.length(), DefaultHighlightPainter);
                // Remove searched section from searchText
                searchText = searchText.substring(s + searchTerm.length());
                // Recurse
                Search(searchTerm, searchText, b + s + searchTerm.length());

            } catch (BadLocationException e) {
                System.err.println("Error " + e.getMessage());
            }
        } else {
            System.out.println("No Items found");
        }
    }

    private void Save() {
        System.out.println("Save");

//        if ( area.getText().isEmpty() || areaHash == area.getText().hashCode() ){
//            JOptionPane.showMessageDialog(this, "No content to save", "Save error", JOptionPane.INFORMATION_MESSAGE);
//        } else {// Skip save as there have been no changes
            /// Navigate to directory
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(selectedFile)) {
                String textToSave = area.getText(); /// Get text from TextArea
                writer.write(textToSave);
                writer.flush();
                String message = "File saved successfully";
                JOptionPane.showMessageDialog(this, message, "Save success", JOptionPane.INFORMATION_MESSAGE); /// Display save success message
                System.out.println("File saved");
            } catch (IOException e) {
                System.err.println("Error saving file:" + e.getMessage());
            }
//            }
        }
    }
}