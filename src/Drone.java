import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Drone {
    public Point startPoint;
    public List<Lidar> lidars;
    public Map realMap;
    boolean initPaint;
    BufferedImage mImage;
    int j;
    private double gyroRotation;
    private Point sensorOpticalFlow;
    private Point pointFromStart;
    private final String drone_img_path = "D:\\Tests\\drone_3_pixels.png";
    private double rotation;
    private double speed;
    private final CPU cpu;


    public Drone(final Map realMap) {
        this.realMap = realMap;

        startPoint = realMap.drone_start_point;
        this.pointFromStart = new Point();
        this.sensorOpticalFlow = new Point();
        this.lidars = new ArrayList<>();

        this.speed = 0.2;

        this.rotation = 0;
        this.gyroRotation = this.rotation;

        this.cpu = new CPU(100, "Drone");
    }

    public static double formatRotation(double rotationValue) {
        rotationValue %= 360;
        if (rotationValue < 0) {
            rotationValue = 360 - rotationValue;
        }
        return rotationValue;
    }

    public void play() {
        this.cpu.play();
    }

    public void stop() {
        this.cpu.stop();
    }

    public void addLidar(final int degrees) {
        final Lidar lidar = new Lidar(this, degrees);
        this.lidars.add(lidar);
        this.cpu.addFunction(lidar::getSimulationDistance);
    }

    public Point getPointOnMap() {
        final double x = this.startPoint.x + this.pointFromStart.x;
        final double y = this.startPoint.y + this.pointFromStart.y;
        return new Point(x, y);
    }

    public void update(final int deltaTime) {

        final double distancedMoved = (this.speed * 100) * ((double) deltaTime / 1000);

        this.pointFromStart = Tools.getPointByDistance(this.pointFromStart, this.rotation, distancedMoved);

        final double noiseToDistance = Tools.noiseBetween(WorldParams.min_motion_accuracy, WorldParams.max_motion_accuracy, false);
        this.sensorOpticalFlow = Tools.getPointByDistance(this.sensorOpticalFlow, this.rotation, distancedMoved * noiseToDistance);

        final double noiseToRotation = Tools.noiseBetween(WorldParams.min_rotation_accuracy, WorldParams.max_rotation_accuracy, false);
        final double milli_per_minute = 60000;
        this.gyroRotation += (1 - noiseToRotation) * deltaTime / milli_per_minute;
        this.gyroRotation = Drone.formatRotation(this.gyroRotation);
    }

    public double getRotation() {
        return this.rotation;
    }

    public double getGyroRotation() {
        return this.gyroRotation;
    }

    public Point getOpticalSensorLocation() {
        return new Point(this.sensorOpticalFlow);
    }

    public void rotateLeft(final int deltaTime) {
        final double rotationChanged = WorldParams.rotation_per_second * deltaTime / 1000;

        this.rotation += rotationChanged;
        this.rotation = Drone.formatRotation(this.rotation);

        this.gyroRotation += rotationChanged;
        this.gyroRotation = Drone.formatRotation(this.gyroRotation);
    }

    public void rotateRight(final int deltaTime) {
        final double rotationChanged = -WorldParams.rotation_per_second * deltaTime / 1000;

        this.rotation += rotationChanged;
        this.rotation = Drone.formatRotation(this.rotation);

        this.gyroRotation += rotationChanged;
        this.gyroRotation = Drone.formatRotation(this.gyroRotation);
    }

    public void speedUp(final int deltaTime) {
        this.speed += (WorldParams.accelerate_per_second * deltaTime / 1000);
        if (this.speed > WorldParams.max_speed) {
            this.speed = WorldParams.max_speed;
        }
    }

    public void slowDown(final int deltaTime) {
        this.speed -= (WorldParams.accelerate_per_second * deltaTime / 1000);
        if (this.speed < 0) {
            this.speed = 0;
        }
    }

    public void paint(final Graphics g) {
        if (!this.initPaint) {
            try {
                final File f = new File(this.drone_img_path);
                this.mImage = ImageIO.read(f);
                this.initPaint = true;
            } catch (final Exception ex) {

            }
        }

        for (int i = 0; i < this.lidars.size(); i++) {
            final Lidar lidar = this.lidars.get(i);
            lidar.paint(g);
        }
    }

    public String getInfoHTML() {
        final DecimalFormat df = new DecimalFormat("#.####");

        String info = "<html>";
        info += "Rotation: " + df.format(this.rotation) + "<br>";
        info += "Location: " + this.pointFromStart + "<br>";
        info += "gyroRotation: " + df.format(this.gyroRotation) + "<br>";
        info += "sensorOpticalFlow: " + this.sensorOpticalFlow + "<br>";
        info += "</html>";
        return info;
    }
}
