import java.util.Random;

public class Tools {

	/*
	 * distance in CM
	 */
	public static Point getPointByDistance(Point fromPoint, double rotation, double distance) {
		final double radians = Math.PI * (rotation / 180);

		final double i = distance / WorldParams.CMPerPixel;
		final double xi = fromPoint.x + Math.cos(radians) * i;
		final double yi = fromPoint.y + Math.sin(radians) * i;

		return new Point(xi, yi);
	}

	/*
	 *
	 */
	public static double noiseBetween(final double min, final double max, final boolean isNegative) {
		Random rand = new Random();
		double noiseToDistance = 1;
		final double noise = (min + rand.nextFloat() * (max - min)) / 100;
		if (!isNegative) {
			return noiseToDistance + noise;
		}

		if (rand.nextBoolean()) {
			return noiseToDistance + noise;
		} else {
			return noiseToDistance - noise;
		}

	}

	public static double getRotationBetweenPoints(final Point from, final Point to) {
		double y1 = from.y - to.y;
		double x1 = from.x - to.x;


		final double radians = Math.atan(y1 / x1);

		final double rotation = radians * 180 / Math.PI;
		return rotation;
	}


	public static double getDistanceBetweenPoints(Point from, Point to) {
		final double x1 = (from.x - to.x) * (from.x - to.x);
		final double y1 = (from.y - to.y) * (from.y - to.y);
		return Math.sqrt(x1 + y1);

	}

}
