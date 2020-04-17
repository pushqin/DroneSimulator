import javax.swing.*;
import java.awt.*;


public class Painter extends JComponent {
    AutoAlgo1 algo;

    public Painter(final AutoAlgo1 algo) {
        this.algo = algo;
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        this.algo.paint(g);
    }
}
