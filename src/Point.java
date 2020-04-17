import java.text.DecimalFormat;

public class Point {
    public double x;
    public double y;

    public Point(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public Point(final Point p) {
        x = p.x;
        y = p.y;
    }

    public Point() {
        this.x = 0;
        this.y = 0;
    }

    @Override
    public String toString() {
        final DecimalFormat df = new DecimalFormat("#.###");

        return "(" + df.format(this.x) + "," + df.format(this.y) + ")";
    }

}
