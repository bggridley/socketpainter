import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class MyPanel extends JPanel {

    private boolean draw = false;
    private int x, y;
    private int startX, startY;
    private Color currentColor;
    private boolean lineMode = true;
    private ArrayList<Shape> shapeArray = new ArrayList<>();
    private Painter painter;
    private int[] positions = new int[4]; // stores the previous values


    public MyPanel(Painter p) {
        this.painter = p;
    }

    public ArrayList<Shape> getShapeArray() {
        return shapeArray;
    }


    public void setColor(Color c) {
        this.currentColor = c;
    }

    public void setLineMode(boolean b) {
        this.lineMode = b;
        // System.out.println('A');
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Shape s : shapeArray) {
            g.setColor(s.color);
            if (s.line) {
                g.drawLine(s.positions[0], s.positions[1], s.positions[2], s.positions[3]);
            } else {
                g.drawOval(s.positions[0], s.positions[1], s.positions[2], s.positions[3]);
            }
        }

        g.setColor(currentColor);

        if (draw) {

            if (lineMode) {
                g.drawLine(startX, startY, x, y);

                positions[0] = startX;
                positions[1] = startY;
                positions[2] = x;
                positions[3] = y;
            } else {
                int minX = Math.min(x, startX);
                int minY = Math.min(y, startY);
                int maxX = Math.max(x, startX);
                int maxY = Math.max(y, startY);

                int x1 = minX;
                int y1 = minY;
                int width = maxX - minX;
                int height = maxX - minX;

                g.drawOval(x1, y1, width, height);

                positions[0] = x1;
                positions[1] = y1;
                positions[2] = width;
                positions[3] = height;
            }

            // System.out.println(startX);
        }

        // System.out.println("PC called");
    }

    public void beginDraw(int x, int y) {
        draw = true;
        this.startX = x;
        this.startY = y;

        // System.out.println("draw:" + draw);
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        // System.out.println("okay");
        repaint();
    }

    public void stopDraw() {
        if (draw) {
            draw = false;

            Shape shape = new Shape(positions, currentColor, lineMode);
            shapeArray.add(shape);
            painter.sendShape(shape);
        }
    }

  
}