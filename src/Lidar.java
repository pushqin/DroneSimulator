import java.awt.*;
import java.util.Random;

public class Lidar {
    Drone drone;
    double degrees;
    double current_distance;

    public Lidar(final Drone drone, final double degrees) {
        this.drone = drone;
        this.degrees = degrees;
    }

    public double getDistance(final int deltaTime) {
        final Point actualPointToShoot = this.drone.getPointOnMap();
        final double rotation = this.drone.getRotation() + this.degrees;

        double distanceInCM = 1;
        while (distanceInCM <= WorldParams.lidarLimit) {
            final Point p = Tools.getPointByDistance(actualPointToShoot, rotation, distanceInCM);
            if (this.drone.realMap.isCollide((int) p.x, (int) p.y)) {
                break;
            }
            distanceInCM++;
        }


        return distanceInCM;
    }

    public double getSimulationDistance(final int deltaTime) {
        final Random ran = new Random();
        double distanceInCM;
        if (ran.nextFloat() <= 0.05f) { // 5% of the time, not getting an answer
            distanceInCM = 0;
        } else {
            distanceInCM = this.getDistance(deltaTime);
            distanceInCM += ran.nextInt(WorldParams.lidarNoise * 2) - WorldParams.lidarNoise; // +- 5 CM to the final calc
        }


        current_distance = distanceInCM; // store it for instance get
        return distanceInCM;
    }


    public void paint(final Graphics g) {
        final Point actualPointToShoot = this.drone.getPointOnMap();
        final double fromRotation = this.drone.getRotation() + this.degrees;
        final Point to = Tools.getPointByDistance(actualPointToShoot, fromRotation, current_distance);


        g.drawLine((int) actualPointToShoot.x, (int) actualPointToShoot.y, (int) to.x, (int) to.y);
    }


}
