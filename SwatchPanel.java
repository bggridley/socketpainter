import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.Color;

public class SwatchPanel extends JPanel {

    private ArrayList<Color> colors;
    private int selectedIndex = 0;

    private int ps;

    public SwatchPanel() {
        super();

        colors = new ArrayList<>();

        colors.add(Color.PINK);
        colors.add(Color.RED);
        colors.add(Color.ORANGE);
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.BLUE);
        colors.add(Color.MAGENTA);
        // colors.add(Color.BLACK);
        // colors.add(Color.darkGray);
    }

    public int getPanelSize() {
        return ps;
    }

    public void setSelected(int s) {
        if (s >= colors.size())
            return;

        selectedIndex = s;

        this.repaint();
    }

    public Color getSelected() {
        return colors.get(selectedIndex);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        int width = g.getClipBounds().width;
        int height = g.getClipBounds().height;

        // this.w = w;
        // this.h = h;

        int panelHeight = height / colors.size();
        ps = panelHeight;

        for (int i = 0; i < colors.size(); i++) {
            g2.setColor(colors.get(i));
            g2.fillRect(0, i * (panelHeight), width, panelHeight);

        }

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(6));
        g2.drawRect(3, selectedIndex * (panelHeight) + 2, width - 7, panelHeight - 5);

        // System.out.println(g.getClipBounds().width);
        // System.out.println(g.getClipBounds().height);

        // g.drawLine(10, 20, 100, 300);
        // System.out.println("PC called");
    }

}
