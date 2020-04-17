import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Map {
    Point drone_start_point;
    private boolean[][] map;

    public Map(final String path, final Point drone_start_point) {
        try {
            this.drone_start_point = drone_start_point;
            final BufferedImage img_map = ImageIO.read(new File(path));
            map = this.render_map_from_image_to_boolean(img_map);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private boolean[][] render_map_from_image_to_boolean(final BufferedImage map_img) {
        final int w = map_img.getWidth();
        final int h = map_img.getHeight();
        final boolean[][] map = new boolean[w][h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                final int clr = map_img.getRGB(x, y);
                final int red = (clr & 0x00ff0000) >> 16;
                final int green = (clr & 0x0000ff00) >> 8;
                final int blue = clr & 0x000000ff;
                if (red != 0 && green != 0 && blue != 0) { // think black
                    map[x][y] = true;
                }
            }
        }

        return map;
    }

    boolean isCollide(final int x, final int y) {

        return !this.map[x][y];
    }

    public void paint(final Graphics g) {
        final Color c = g.getColor();
        g.setColor(Color.GRAY);
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[0].length; j++) {
                if (!this.map[i][j]) {
                    g.drawLine(i, j, i, j);
                }
            }
        }
        g.setColor(c);
    }

}

