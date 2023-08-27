package org.example;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import java.awt.Color;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main extends JFrame implements ActionListener {
    JMenu fileMenu, searchMenu, viewMenu, manageMenu, helpMenu;
    JMenuItem newItem, openItem, saveItem, saveAsItem, exitItem, findItem, aboutItem, dateAndTimeItem, printItem;
    JLabel timeDateLabel;
    private static JTextArea area;
    private static Highlighter high;
    private final Color highLighterColor = Color.ORANGE;
    private File openedFile = null;
    private static int areaHash = 0;

    public static void main(String[] args) { new Main(); }

    public Main() {
        /// Creating window
        setTitle("Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(650, 700)); /// can change

        /// Setting layout
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

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
        saveItem = new JMenuItem("Save");
        saveAsItem = new JMenuItem("Save as");
        exitItem = new JMenuItem("Exit");
        findItem = new JMenuItem("Find");
        aboutItem = new JMenuItem("About");
        dateAndTimeItem = new JMenuItem("Date and Time");
        printItem = new JMenuItem("Print");

        /// Create Action listeners
        newItem.addActionListener(this);
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        saveAsItem.addActionListener(this);
        exitItem.addActionListener(this);
        findItem.addActionListener(this);
        aboutItem.addActionListener(this);
        dateAndTimeItem.addActionListener(this);
        printItem.addActionListener(this);

        /// Adding items to menus
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(exitItem);
        fileMenu.add(printItem);
        searchMenu.add(findItem);
        helpMenu.add(aboutItem);
        viewMenu.add(dateAndTimeItem);

        /// Adding menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(searchMenu);
        menuBar.add(viewMenu);
        menuBar.add(manageMenu);
        menuBar.add(helpMenu);

        /// Create label for time and date
        timeDateLabel = new JLabel();
        menuBar.add(Box.createHorizontalGlue()); /// Ensure it is on right side of menu bar
        menuBar.add(timeDateLabel);

        // Set the menu bar
        setJMenuBar(menuBar);

        /// Adding text field with scroll pane
        area = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(area);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        area.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) { /// If right click
                    showSCPCmenu(e.getX(), e.getY()); /// Pass mouse coordinates to method
                }
            }
        });


        pack();
        setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent event) {
        JComponent source = (JComponent) event.getSource(); /// Get source of action
        if (source.equals(newItem)){ New(); }
        if (source.equals(saveAsItem)){ SaveAs(); }
        if (source.equals(saveItem)){ Save(); }
        if (source.equals(openItem)){ Open(); }
        if (source.equals(exitItem)){ dispose(); }
        if (source.equals(findItem)){
            high = area.getHighlighter();
            high.removeAllHighlights(); // clear any preexisting highlights
            Search(JOptionPane.showInputDialog(this, "Find"), area.getText().toLowerCase(), 0);
        }
        if (source.equals(aboutItem)){
            String message = """
                        Team Members:
                        - Gerard Kuriger
                        - Jessica Lang\s
                         This is a text editor."""; /// Creating the message
            JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE); /// Displaying the information message
        }
        if (source.equals(dateAndTimeItem)){ insertDateAndTime(); }
        if (source.equals(printItem)) { printText(); }
    }

    private void New(){
        // If Test Field is not empty prompt to save
        if ( !area.getText().isEmpty() ) {
            if (areaHash == area.getText().hashCode()) { // if the file has been saved
                area.setText("");
            } else {
                int res = JOptionPane.showConfirmDialog(this, "Save Changes?");
                if (res == 0){ // Yes
                    SaveAs();
                    New();
                }
                if (res == 1){ // No clear area and set openFile to null
                    openedFile = null;
                    area.setText("");
                    areaHash = 0;
                }
            }
        }
    }

    private void Open(){
        JFileChooser fileChooser = new JFileChooser(); // Open the file chooser dialog and allow the user to select the file they want to view
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog( null );

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                File file = new File( selectedFile.getAbsolutePath() );
                StringBuilder data = new StringBuilder(); // define string builder
                Scanner myReader = new Scanner(file); // Init Scanner

                while (myReader.hasNextLine()){
                    data.append(myReader.nextLine()).append('\n'); // get line and append new line to maintain formatting
                }
                areaHash = data.toString().hashCode();
                area.setText( data.toString() ); // set text area
                openedFile = selectedFile; /// Informs save method
            } catch (FileNotFoundException e) {
                System.err.println( "Error "+ e.getMessage() );
            }
        }
    }

    private void Search( String searchTerm, String searchText, int b ){ // finds first instance of searchTerm
        if ( searchText.contains(searchTerm.toLowerCase()) ) { // not case-sensitive
            System.out.println(searchTerm + " Found");
            try{
                int s = searchText.indexOf(searchTerm);

                Highlighter.HighlightPainter DefaultHighlightPainter = new DefaultHighlightPainter( highLighterColor );
                // Apply Beginning offset to account for removed searched section
                high.addHighlight( b+s, b+s+searchTerm.length(), DefaultHighlightPainter );
                // Remove searched section from searchText
                searchText = searchText.substring( s+searchTerm.length() );
                // Recurse
                Search( searchTerm, searchText, b+s+searchTerm.length() );

            }catch (BadLocationException e){
                System.err.println( "Error "+ e.getMessage() );
            }
        } else {
            System.out.println("No Items found");
        }
    }

    private void SaveAs() {
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
                openedFile = selectedFile;
                areaHash = textToSave.hashCode();
            } catch (IOException e) {
                System.err.println("Error saving file:" + e.getMessage());
            }
        }
    }

    private void Save() {
        if (openedFile != null) { /// Check if file has been opened
            try (PrintWriter writer = new PrintWriter(openedFile)) {
                String textToSave = area.getText(); /// Get text
                writer.write(textToSave); /// Save to file
                writer.flush();
                String message = "File saved successfully";
                JOptionPane.showMessageDialog(this, message, "Save success", JOptionPane.INFORMATION_MESSAGE);
                areaHash = textToSave.hashCode();
            } catch (IOException e) {
                System.err.println("Error saving file: " + e.getMessage());
            }
        } else { /// If file has not been opened
            JOptionPane.showMessageDialog(this, "Please open a file or create a new one before saving.", "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void insertDateAndTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss\n\n");
        String currentDateAndTime = dateFormat.format(new Date());
        timeDateLabel.setText("Time and Date: " + currentDateAndTime + "  "); /// Display date and time in textarea
    }

    private void showSCPCmenu(int x, int y) {

        /// Create popupmenu
        JPopupMenu scpcMenu = new JPopupMenu();

        /// Create menu items
        JMenuItem cutItem = new JMenuItem(new DefaultEditorKit.CutAction());
        JMenuItem copyItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        JMenuItem pasteItem = new JMenuItem(new DefaultEditorKit.PasteAction());
        JMenuItem selectAllItem = new JMenuItem("Select All");
        cutItem.setText("Cut");
        copyItem.setText("Copy");
        pasteItem.setText("Paste");

        selectAllItem.addActionListener(e -> area.selectAll()); /// Select all text in textarea

        /// Add items to popupmenu
        scpcMenu.add(cutItem);
        scpcMenu.add(copyItem);
        scpcMenu.add(pasteItem);
        scpcMenu.add(selectAllItem);

        /// Show popupmenu one text area at mouse coordinates
        scpcMenu.show(area, x, y);
    }

    private void printText() {
        if (area.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nothing to print, the text area is empty.", "Print Error", JOptionPane.ERROR_MESSAGE);
            return; /// Nothing to print return
        }

        PrinterJob job = PrinterJob.getPrinterJob(); /// Create printer job
        PageFormat pageFormat = job.defaultPage();
        pageFormat.setOrientation(PageFormat.PORTRAIT); /// Portrait orientation

        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) { /// Check if page index is out of range
                    return Printable.NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY()); /// Translate graphics to printable area

                area.print(g2d); /// Print text
                return Printable.PAGE_EXISTS; /// Page printed
            }
        }, pageFormat);

        if (job.printDialog()) {
            try {
                /// Start printing
                job.print();
            } catch (PrinterException e) { /// Error
                JOptionPane.showMessageDialog(this, "Error printing: " + e.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



}