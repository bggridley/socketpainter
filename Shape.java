import java.awt.Color;
import java.io.Serializable;

public class Shape implements Serializable {

    // i hate encapsulation because it means i have to make getters O - O
    public int[] positions;
    public Color color;
    public boolean line;

    public Shape(int[] pos, Color c, boolean b) {
        positions = new int[4];
        for (int i = 0; i < pos.length; i++) {
            positions[i] = pos[i];
        }

        this.color = c;
        this.line = b;
    }
}