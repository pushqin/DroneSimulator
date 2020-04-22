import java.awt.*;
import java.util.ArrayList;

public class AutoAlgo1 {

	int map_size = 3000;
	PixelState[][] map;
	Drone drone;
	Point droneStartingPoint = new Point(map_size / 2, map_size / 2);
	ArrayList<Point> points = new ArrayList<Point>();
	int isRotating;
	ArrayList<Double> degrees_left = new ArrayList<>();
	ArrayList<Func> degrees_left_func = new ArrayList<>();
	boolean isSpeedUp;
	CPU ai_cpu;

	boolean is_init = true;
	boolean is_risky;
	int max_risky_distance = 150;
	boolean try_to_escape;
	double risky_dis;
	int max_angle_risky = 10;

	boolean is_lidars_max;
	double max_distance_between_points = 100;
	Point init_point;
	int counter;
	double lastGyroRotation;


	public AutoAlgo1(Map realMap) {

		drone = new Drone(realMap);
		drone.addLidar(0);
		drone.addLidar(90);
		drone.addLidar(-90);


		initMap();

		isRotating = 0;
		ai_cpu = new CPU(200, "Auto_AI");
		ai_cpu.addFunction(this::update);
	}

	public void initMap() {
		map = new PixelState[map_size][map_size];
		for (int i = 0; i < map_size; i++) {
			for (int j = 0; j < map_size; j++) {
				map[i][j] = PixelState.unexplored;
			}
		}

	}

	public void play() {
		drone.play();
		ai_cpu.play();
	}

	public void update(int deltaTime) {
		updateVisited();
		updateMapByLidars();

		ai(deltaTime);


		if (isRotating != 0) {
			updateRotating(deltaTime);
		}
		if (isSpeedUp) {
			drone.speedUp(deltaTime);
		} else {
			drone.slowDown();
		}

	}

	public void speedUp() {
		isSpeedUp = true;
	}

	public void speedDown() {
		isSpeedUp = false;
	}

	public void updateMapByLidars() {
		Point dronePoint = drone.getOpticalSensorLocation();
		Point fromPoint = new Point(dronePoint.x + droneStartingPoint.x, dronePoint.y + droneStartingPoint.y);

		for (int i = 0; i < drone.lidars.size(); i++) {
			Lidar lidar = drone.lidars.get(i);
			double rotation = drone.getGyroRotation() + lidar.degrees;
			for (int distanceInCM = 0; distanceInCM < lidar.current_distance; distanceInCM++) {
				Point p = Tools.getPointByDistance(fromPoint, rotation, distanceInCM);
				setPixel(p.x, p.y, PixelState.explored);
			}

			if (lidar.current_distance > 0 && lidar.current_distance < WorldParams.lidarLimit - WorldParams.lidarNoise) {
				Point p = Tools.getPointByDistance(fromPoint, rotation, lidar.current_distance);
				setPixel(p.x, p.y, PixelState.blocked);
			}
		}
	}

	public void updateVisited() {
		Point dronePoint = drone.getOpticalSensorLocation();
		Point fromPoint = new Point(dronePoint.x + droneStartingPoint.x, dronePoint.y + droneStartingPoint.y);

		setPixel(fromPoint.x, fromPoint.y, PixelState.visited);
	}

	public void setPixel(double x, double y, PixelState state) {
		int xi = (int) x;
		int yi = (int) y;

		if (state == PixelState.visited) {
			map[xi][yi] = PixelState.visited;
			return;
		}

		if (map[xi][yi] == PixelState.unexplored) {
			map[xi][yi] = state;
		}
	}

	public void paintBlindMap(Graphics g) {
		Color c = g.getColor();

		int i = (int) droneStartingPoint.y - (int) drone.startPoint.x;
		int startY = i;
		for (; i < map_size; i++) {
			int j = (int) droneStartingPoint.x - (int) drone.startPoint.y;
			int startX = j;
			for (; j < map_size; j++) {
				if (map[i][j] != PixelState.unexplored) {
					if (map[i][j] == PixelState.blocked) {
						g.setColor(Color.RED);
					} else if (map[i][j] == PixelState.explored) {
						g.setColor(Color.YELLOW);
					} else if (map[i][j] == PixelState.visited) {
						g.setColor(Color.BLUE);
					}
					g.drawLine(i - startY, j - startX, i - startY, j - startX);
				}
			}
		}
		g.setColor(c);
	}

	public void paintPoints(Graphics g) {
		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			g.drawOval((int) p.x + (int) drone.startPoint.x - 10, (int) p.y + (int) drone.startPoint.y - 10, 20, 20);
		}

	}

	public void paint(Graphics g) {
		if (SimulationWindow.toogleRealMap) {
			drone.realMap.paint(g);
		}

		paintBlindMap(g);
		paintPoints(g);

		drone.paint(g);

	}

	public void ai(int deltaTime) {
		if (!SimulationWindow.toogleAI) {
			return;
		}


		if (is_init) {
			speedUp();
			Point dronePoint = drone.getOpticalSensorLocation();
			init_point = new Point(dronePoint);
			points.add(dronePoint);
			is_init = false;
		}

		Point dronePoint = drone.getOpticalSensorLocation();

		if (Tools.getDistanceBetweenPoints(getLastPoint(), dronePoint) >= max_distance_between_points) {
			points.add(dronePoint);
		}

		if (!is_risky) {
			speedUp();
			Lidar lidar = drone.lidars.get(0);
			if (lidar.current_distance <= max_risky_distance) {
				is_risky = true;
				risky_dis = lidar.current_distance;

			}

			Lidar lidar1 = drone.lidars.get(1);
			if (lidar1.current_distance <= max_risky_distance / 3) {
				is_risky = true;
			}

			Lidar lidar2 = drone.lidars.get(2);
			if (lidar2.current_distance <= max_risky_distance / 3) {
				is_risky = true;
			}

		} else {
			if (!try_to_escape) {
				speedDown();
				try_to_escape = true;
				Lidar lidar1 = drone.lidars.get(1);
				double a = lidar1.current_distance;

				Lidar lidar2 = drone.lidars.get(2);
				double b = lidar2.current_distance;


				int spin_by = max_angle_risky;


				if (a > 270 && b > 270) {
					is_lidars_max = true;
					Point l1 = Tools.getPointByDistance(dronePoint, lidar1.degrees + drone.getGyroRotation(), lidar1.current_distance);
					Point l2 = Tools.getPointByDistance(dronePoint, lidar2.degrees + drone.getGyroRotation(), lidar2.current_distance);
					Point last_point = getAvgLastPoint();
					double dis_to_lidar1 = Tools.getDistanceBetweenPoints(last_point, l1);
					double dis_to_lidar2 = Tools.getDistanceBetweenPoints(last_point, l2);

					if (Tools.getDistanceBetweenPoints(getLastPoint(), dronePoint) >= max_distance_between_points) {
						points.add(dronePoint);
					}

					spin_by = 90;

					if (dis_to_lidar1 < dis_to_lidar2) {

						spin_by *= (-1);
					}
				} else {


					if (a < b) {
						spin_by *= (-1);
					}
				}


				spinBy(spin_by, true, new Func() {
					@Override
					public void method() {
						try_to_escape = false;
						is_risky = false;
					}
				});
			}
		}
	}

	public void updateRotating(int deltaTime) {

		if (degrees_left.size() == 0) {
			return;
		}

		double degrees_left_to_rotate = degrees_left.get(0);
		boolean isLeft = true;
		if (degrees_left_to_rotate > 0) {
			isLeft = false;
		}

		double curr = drone.getGyroRotation();
		double just_rotated = 0;

		if (isLeft) {

			just_rotated = curr - lastGyroRotation;
			if (just_rotated > 0) {
				just_rotated = -(360 - just_rotated);
			}
		} else {
			just_rotated = curr - lastGyroRotation;
			if (just_rotated < 0) {
				just_rotated = 360 + just_rotated;
			}
		}


		lastGyroRotation = curr;
		degrees_left_to_rotate -= just_rotated;
		degrees_left.remove(0);
		degrees_left.add(0, degrees_left_to_rotate);

		if (isLeft ? degrees_left_to_rotate >= 0 : degrees_left_to_rotate <= 0) {
			degrees_left.remove(0);

			Func func = degrees_left_func.get(0);
			if (func != null) {
				func.method();
			}
			degrees_left_func.remove(0);


			if (degrees_left.size() == 0) {
				isRotating = 0;
			}
			return;
		}

		int direction = (int) (degrees_left_to_rotate / Math.abs(degrees_left_to_rotate));
		drone.rotateLeft(deltaTime * direction);

	}

	public void spinBy(double degrees, boolean isFirst, Func func) {
		lastGyroRotation = drone.getGyroRotation();
		if (isFirst) {
			degrees_left.add(0, degrees);
			degrees_left_func.add(0, func);


		} else {
			degrees_left.add(degrees);
			degrees_left_func.add(func);
		}

		isRotating = 1;
	}

	public void spinBy(double degrees) {
		lastGyroRotation = drone.getGyroRotation();

		degrees_left.add(degrees);
		degrees_left_func.add(null);
		isRotating = 1;
	}

	public Point getLastPoint() {
		if (points.size() == 0) {
			return init_point;
		}

		Point p1 = points.get(points.size() - 1);
		return p1;
	}

	public Point getAvgLastPoint() {
		if (points.size() < 2) {
			return init_point;
		}

		Point p1 = points.get(points.size() - 1);
		Point p2 = points.get(points.size() - 2);
		return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}


	enum PixelState {blocked, explored, unexplored, visited}


}
