import java.text.DecimalFormat;

public class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        x = p.x;
        y = p.y;
    }

    public Point() {
        x = 0;
        y = 0;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.###");

        return "(" + df.format(x) + "," + df.format(y) + ")";
    }

}
