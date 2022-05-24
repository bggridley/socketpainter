
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
import javax.swing.*;
import javax.swing.text.DefaultCaret;

import java.net.Socket;

public class Painter extends JFrame implements ActionListener {

    private int online = 0;
    private Socket s;
    private JLabel onlineCount;
    private MyPanel mp;
    private JTextArea chatArea;

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
        chatArea = new JTextArea(5, 50);
        chatArea.setEditable(false);
        JTextField chatBox = new JTextField(70);
        JScrollPane scroll = new JScrollPane(chatArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        DefaultCaret caret = (DefaultCaret) chatArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        // Border border = BorderFactory.createLineBorder(Color.BLUE, 1);
        // chatBox.setBorder(border);

        // chatArea.append("Chats: \n");
        // chatBox.setText("Chat Box");

        gui.add(chatBox, BorderLayout.PAGE_START);
        gui.add(scroll);
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        gui.setSize(10, 50);

        onlineCount = new JLabel(
                "Online: loading");
        gui.add(onlineCount, BorderLayout.PAGE_END);

        p.add(gui, BorderLayout.SOUTH);

        // JPanel left = new JPanel();

        mp = new MyPanel(this);
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

        String name = JOptionPane.showInputDialog(p,
                "What is your name?", null);

        if (name == null || name.equals("")) {
            return;
        }

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(name, chatBox.getText());
                chatBox.setText("");
            }
        };

        chatBox.addActionListener(action);

        setContentPane(p);
        setVisible(true);

        setLocationRelativeTo(null);

        mp.repaint();
        swatches.repaint();

        try {
            s = new Socket("localhost", 7000);
            System.out.println("Connected");

            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

            oos.writeObject(name);

            ServerConnection connection = new ServerConnection();
            Thread connectionThread = new Thread(connection);
            connectionThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ServerConnection implements Runnable {

        public void run() {
            try {
                while (true) {
                    ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                    String header = (String) ois.readObject();

                    if (header.equals("count")) {
                        online = (int) ois.readObject();
                        onlineCount.setText("Online: " + online);
                    } else if (header.equals("shape")) {
                        //System.out.println("received a shape");
                        mp.getShapeArray().add((Shape) ois.readObject());


                        
                    } else if (header.equals("message")) {
                        String string = (String) ois.readObject();
                        // System.out.println(string);

                        chatArea.append(string);
                    } else if(header.equals("repaint")) {
                        mp.repaint();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendShape(Shape shape) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject("shape");
            oos.writeObject(shape);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String name, String message) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject("message");
            oos.writeObject(name + ": " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Painter();
        // System.out.println("here");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // System.out.println("P!!!!!");
    }
}