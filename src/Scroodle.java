import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Scroodle extends JFrame implements ActionListener {
    JTextArea textArea;
    JFrame frame;

    public Scroodle() {
        // Create the frame
        frame = new JFrame("Scroodle");

        // Create the text area
        textArea = new JTextArea();

        // Create a menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create a menu
        JMenu fileMenu = new JMenu("File");

        // Create menu items
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        // Add action listeners to menu items
        newItem.addActionListener(this);
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        // Add menu items to menu
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);

        // Add menu to menu bar
        menuBar.add(fileMenu);

        // Add menu bar to frame
        frame.setJMenuBar(menuBar);

        // Add text area to frame
        frame.add(textArea);

        // Set frame properties
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Scroodle();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "New":
                textArea.setText("");
                break;
            case "Open":
                JFileChooser fileChooser = new JFileChooser();
                int openResult = fileChooser.showOpenDialog(frame);
                if (openResult == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        textArea.read(reader, null);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                break;
            case "Save":
                JFileChooser saveFileChooser = new JFileChooser();
                int saveResult = saveFileChooser.showSaveDialog(frame);
                if (saveResult == JFileChooser.APPROVE_OPTION) {
                    File file = saveFileChooser.getSelectedFile();
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        textArea.write(writer);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                break;
            case "Exit":
                System.exit(0);
                break;
        }
    }
}
