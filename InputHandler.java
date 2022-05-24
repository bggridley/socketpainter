import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;

public class InputHandler implements MouseInputListener {

    private SwatchPanel swatches;
    private MyPanel drawPanel;

    public InputHandler(SwatchPanel swatches, MyPanel drawPanel) {
        // swatches.getLocation
        this.swatches = swatches;
        this.drawPanel = drawPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

        if (e.getY() > swatches.getLocation().getY() && e.getY() < swatches.getLocation().getY() + swatches.getHeight()
                && e.getX() < swatches.getWidth()) {
            int index = (e.getY() - (int) swatches.getLocation().getY()) / swatches.getPanelSize();
            // System.out.println(index);
            swatches.setSelected(index);
            drawPanel.setColor(swatches.getSelected());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

        if (e.getY() > drawPanel.getLocation().getY()
                && e.getY() < drawPanel.getLocation().getY() + drawPanel.getHeight()
                && e.getX() > drawPanel.getLocation().getX()) {
            drawPanel.beginDraw(e.getX() - (int) drawPanel.getLocation().getX(), e.getY() - (int)drawPanel.getLocation().getY());
            //System.out.//println("starting ");
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        drawPanel.stopDraw();

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

        // System.out.println("DRAGG");
        drawPanel.setLocation(e.getX() - (int) drawPanel.getLocation().getX(), e.getY() - (int)drawPanel.getLocation().getY());

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}
