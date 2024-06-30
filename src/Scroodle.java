//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.*;
//
//public class Scroodle extends JFrame implements ActionListener {
//    JTextArea textArea;
//    JFrame frame;
//
//    public Scroodle() {
//        // Create the frame
//        frame = new JFrame("Scroodle");
//
//        // Create the text area
//        textArea = new JTextArea();
//
//        // Create a menu bar
//        JMenuBar menuBar = new JMenuBar();
//
//        // Create a menu
//        JMenu fileMenu = new JMenu("File");
//
//        // Create menu items
//        JMenuItem newItem = new JMenuItem("New");
//        JMenuItem openItem = new JMenuItem("Open");
//        JMenuItem saveItem = new JMenuItem("Save");
//        JMenuItem exitItem = new JMenuItem("Exit");
//
//        // Add action listeners to menu items
//        newItem.addActionListener(this);
//        openItem.addActionListener(this);
//        saveItem.addActionListener(this);
//        exitItem.addActionListener(this);
//
//        // Add menu items to menu
//        fileMenu.add(newItem);
//        fileMenu.add(openItem);
//        fileMenu.add(saveItem);
//        fileMenu.add(exitItem);
//
//        // Add menu to menu bar
//        menuBar.add(fileMenu);
//
//        // Add menu bar to frame
//        frame.setJMenuBar(menuBar);
//
//        // Add text area to frame
//        frame.add(textArea);
//
//        // Set frame properties
//        frame.setSize(800, 600);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
//    }
//
//    public static void main(String[] args) {
//        new Scroodle();
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        String command = e.getActionCommand();
//
//        switch (command) {
//            case "New":
//                textArea.setText("");
//                break;
//            case "Open":
//                JFileChooser fileChooser = new JFileChooser();
//                int openResult = fileChooser.showOpenDialog(frame);
//                if (openResult == JFileChooser.APPROVE_OPTION) {
//                    File file = fileChooser.getSelectedFile();
//                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//                        textArea.read(reader, null);
//                    } catch (IOException ioException) {
//                        ioException.printStackTrace();
//                    }
//                }
//                break;
//            case "Save":
//                JFileChooser saveFileChooser = new JFileChooser();
//                int saveResult = saveFileChooser.showSaveDialog(frame);
//                if (saveResult == JFileChooser.APPROVE_OPTION) {
//                    File file = saveFileChooser.getSelectedFile();
//                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//                        textArea.write(writer);
//                    } catch (IOException ioException) {
//                        ioException.printStackTrace();
//                    }
//                }
//                break;
//            case "Exit":
//                System.exit(0);
//                break;
//        }
//    }
//}
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class Scroodle extends JFrame {
    private JTextPane textPane;
    private Highlighter highlighter;
    private Highlighter.HighlightPainter painter;
    private DrawPanel drawPanel;

    public Scroodle() {
        // Initialize components
        textPane = new JTextPane();
        highlighter = textPane.getHighlighter();

        // Set up the JFrame
        setTitle("Scroodle - Notepad with Highlighting and Scribbling");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Add the JTextPane to the JFrame
        getContentPane().add(new JScrollPane(textPane), BorderLayout.CENTER);

        // Add a toolbar with highlight and scribble options
        JToolBar toolBar = new JToolBar();
        getContentPane().add(toolBar, BorderLayout.NORTH);

        // Add Highlight button
        JButton highlightButton = new JButton("Highlight");
        toolBar.add(highlightButton);

        // Color picker for custom colors
        JButton colorPickerButton = new JButton("Pick Color");
        toolBar.add(colorPickerButton);

        // Add Scribble button
        JButton scribbleButton = new JButton("Scribble");
        toolBar.add(scribbleButton);

        // Default highlight color
        painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);

        // Action listener for the color picker
        colorPickerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(null, "Pick a Color", Color.YELLOW);
                if (color != null) {
                    painter = new DefaultHighlighter.DefaultHighlightPainter(color);
                }
            }
        });

        // Action listener for the highlight button
        highlightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int start = textPane.getSelectionStart();
                    int end = textPane.getSelectionEnd();
                    highlighter.addHighlight(start, end, painter);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Add a drawing panel for scribbling
        drawPanel = new DrawPanel();
        getContentPane().add(drawPanel, BorderLayout.SOUTH);

        // Action listener for the scribble button
        scribbleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawPanel.setVisible(!drawPanel.isVisible());
            }
        });

        drawPanel.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Scroodle().setVisible(true);
            }
        });
    }
}

class DrawPanel extends JPanel {
    private int prevX, prevY;

    public DrawPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 200));

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                prevX = e.getX();
                prevY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                Graphics g = getGraphics();
                g.drawLine(prevX, prevY, x, y);
                prevX = x;
                prevY = y;
                g.dispose();
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }
}
