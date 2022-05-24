
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;



public class Painter extends JFrame implements ActionListener {

    public Painter() {
        setSize(500, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        JPanel p = new JPanel();
        // p.setBackground(Color.RED);
        p.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new java.awt.GridLayout(0, 2));

        JButton line = new JButton("Line");
        JButton circle = new JButton("Circle");

        topPanel.add(line);
        topPanel.add(circle);

        p.add(topPanel, BorderLayout.NORTH);

        JPanel gui = new JPanel(new BorderLayout(5, 5));
        // Dimension d;
        // gui.setMaximumSize(new Dimension(1000, 50));
        JTextArea chatArea = new JTextArea(5, 50);
        chatArea.setEditable(false);
        JTextField chatBox = new JTextField();
        JScrollPane scroll = new JScrollPane(chatArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        // Border border = BorderFactory.createLineBorder(Color.BLUE, 1);
        // chatBox.setBorder(border);

        // chatArea.append("Chats: \n");
        // chatBox.setText("Chat Box");

       

        gui.add(chatBox, BorderLayout.PAGE_START);
        gui.add(scroll);
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        gui.setSize(10, 50);

        p.add(gui, BorderLayout.SOUTH);

        

        // JPanel left = new JPanel();

        
        MyPanel mp = new MyPanel();
        mp.setBackground(Color.LIGHT_GRAY);
        
        p.add(mp, BorderLayout.CENTER);

        SwatchPanel swatches = new SwatchPanel();
        // Button b2 = new JButton("No! - Click Here");s
        swatches.setMinimumSize(new Dimension(100, 0));
        swatches.setPreferredSize(new Dimension(100, 0));
        swatches.setBackground(Color.BLACK);
        
        p.add(swatches, BorderLayout.WEST);
       

        InputHandler ih = new InputHandler(swatches, mp);
        p.addMouseListener(ih);
        p.addMouseMotionListener(ih);

        mp.setColor(swatches.getSelected());

        line.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mp.setLineMode(true);
            }
        });

        circle.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mp.setLineMode(false);
            }
        });

        setContentPane(p);
        setVisible(true);
        mp.repaint();
        swatches.repaint();
    }

    public static void main(String[] args) {
        new Painter();
        // System.out.println("here");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println("P!!!!!");
    }
}