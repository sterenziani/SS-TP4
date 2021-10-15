import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import back.Particle;
import back.Simulator;
import back.SimulatorBeeman;
import back.SimulatorGear;
import back.SimulatorOscillation;
import back.SimulatorVerlet;
import back.SpaceReport;
import back.SpaceSimulator;
import front.Input;
import front.Output;
import front.Parser;

public class App {

	private static final double SECONDS_IN_DAY = 24 * 3600;
	private static final double SECONDS_IN_YEAR = 365 * SECONDS_IN_DAY;
	private static final int DAYS_IN_2_YEARS = 730;
	private static final int MINUTES_MARGIN = 5;

	private static double getV(double sx, double sy, double px, double py) {
		double dx = sx - px;
		double dy = sy - py;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public static void main(String[] args) throws IOException {
		Input input = Parser.ParseInputFile("input.txt");
		if (input.getExercise() == 1)
			Exercise1(input);
		else if (input.getExercise() == 2)
			Exercise2(input, false);
		else if (input.getExercise() == 3)
			Exercise2(input, true);
		else if (input.getExercise() == 4)
			launchAtPerfectTime(input, false, true);
		else if (input.getExercise() == 5)
			launchAtPerfectTime(input, false, false);
		else if (input.getExercise() == 6)
			launchAtPerfectTime(input, true, false);
		else if (input.getExercise() == 7)
			compareVelocities(input);
		else if (input.getExercise() == 8)
			calculateErrors();
		else
			System.out.println("There is no exercise #" + input.getExercise());
	}

	private static void Exercise1(Input input) {
		// Parametros
		double m = 70;
		double k = Math.pow(10, 4);
		double gamma = 100;
		double tf = 5;

		// Condiciones iniciales
		double r0 = 1;
		double v0 = -r0 * gamma / (2 * m); // r0 = A * e^0 * cos(0) ==> A = 1
		Particle ball = new Particle(1, r0, 0, v0, 0, m, 0);

		// Preparar algoritmo
		Simulator simulator = null;
		switch (input.getAlgorithm()) {
			case "B":
				simulator = new SimulatorBeeman(ball, input.getDeltaT(), k, gamma, false);
				break;
			case "BPC":
				simulator = new SimulatorBeeman(ball, input.getDeltaT(), k, gamma, true);
				break;
			case "V":
				simulator = new SimulatorVerlet(ball, input.getDeltaT(), k, gamma);
				break;
			case "G":
				simulator = new SimulatorGear(ball, input.getDeltaT(), k, gamma);
				break;
			case "O":
				simulator = new SimulatorOscillation(ball, input.getDeltaT(), k, gamma, r0);
				break;
			default:
				System.out.println("Invalid algorithm " + input.getAlgorithm());
		}

		// Simulación
		double t = 0.0;
		Map<Double, Double> map = new HashMap<>();
		while (t < tf) {
			map.put(t, ball.getX());
			simulator.updateParticle();
			t += input.getDeltaT();
		}
		Output.outputOscillatorToFile(input.getAlgorithm(), input.getDeltaT(), map);
		System.out.println("Simulation complete!");
	}

	private static void Exercise2(Input input, boolean toVenus) {
		boolean launched = false;
		boolean crashed = false;
		boolean died = false;
		double t, minDistance, minTime;
		double minGlobalDistance = Double.MAX_VALUE;
		double bestLaunchTime = 0;
		System.out.println("DeltaT is " + input.getDeltaT() + " and speed is " + input.getShipVelocity());

		// Units are in KM to simplify distance math
		List<Particle> particles = createPlanets();
		SpaceSimulator simulator;
		List<SpaceReport> reports = new ArrayList<>();
		for (int i = 0; i < DAYS_IN_2_YEARS; i++) {
			double departureTime = i * SECONDS_IN_DAY;
			System.out.println("Day " + i + " (departureTime = " + departureTime + ")\tStarting new launch!");
			particles = createPlanets();
			t = 0.0;
			minDistance = Integer.MAX_VALUE;
			minTime = Integer.MAX_VALUE;
			launched = false;
			crashed = false;
			died = false;
			simulator = new SpaceSimulator(particles, input.getDeltaT());

			while (!crashed && !died && t <= departureTime + SECONDS_IN_YEAR / 2) {
				if (t >= departureTime && !launched) {
					simulator.launchSpaceship(input.getShipVelocity(), toVenus);
					launched = true;
				}
				simulator.updateParticles();

				if (launched) {
					if (toVenus) {
						if (Math.abs(simulator.getShipToSunDistance()) < Math
								.abs(simulator.getVenusToSunDistance() / 2))
							died = true;
						if (minDistance > Math.max(0, simulator.getShipToVenusDistance())) {
							minDistance = Math.max(0, simulator.getShipToVenusDistance());
							minTime = t - departureTime;
						}
						if (simulator.getShipToVenusDistance() <= 0)
							crashed = true;
					} else {
						if (minDistance > Math.max(0, simulator.getShipToMarsDistance())) {
							minDistance = Math.max(0, simulator.getShipToMarsDistance());
							minTime = t - departureTime;
						}
						if (simulator.getShipToMarsDistance() <= 0)
							crashed = true;
					}
				}
				t += input.getDeltaT();
			}
			SpaceReport report = new SpaceReport(departureTime, simulator.getShipVelocity(), minDistance, minTime);
			reports.add(report);
			if (report.getMinDistance() < minGlobalDistance) {
				minGlobalDistance = report.getMinDistance();
				bestLaunchTime = report.getDepartureTime();
			}
			System.out.println(report.getDepartureTime() + "\t" + report.getShipVelocity() + "\t"
					+ report.getMinDistance() + "\t" + report.getTime());
		}
		System.out.println("Mission complete! Best day was " + bestLaunchTime / SECONDS_IN_DAY + "\n\n");
		Output.outputShipReports(reports, false);

		// Analyze best launch date (Find best time in time frame around that launch)
		reports.clear();
		particles = createPlanets();
		t = 0.0;
		launched = false;
		crashed = false;
		died = false;
		double launchInterval = 60 * MINUTES_MARGIN;
		double launchDay = bestLaunchTime;
		simulator = new SpaceSimulator(particles, input.getDeltaT());
		minGlobalDistance = Double.MAX_VALUE;
		for (double departureTime = launchDay - SECONDS_IN_DAY; departureTime <= launchDay
				+ SECONDS_IN_DAY; departureTime += launchInterval) {
			particles = createPlanets();
			t = 0.0;
			minDistance = Integer.MAX_VALUE;
			minTime = Integer.MAX_VALUE;
			launched = false;
			crashed = false;
			died = false;
			simulator = new SpaceSimulator(particles, input.getDeltaT());

			while (!crashed && !died && t <= departureTime + SECONDS_IN_YEAR / 2) {
				if (t >= departureTime && !launched) {
					simulator.launchSpaceship(input.getShipVelocity(), toVenus);
					launched = true;
				}
				simulator.updateParticles();
				if (launched) {
					if (toVenus) {
						if (Math.abs(simulator.getShipToSunDistance()) < Math
								.abs(simulator.getVenusToSunDistance() / 2))
							died = true;
						if (minDistance > Math.max(0, simulator.getShipToVenusDistance())) {
							minDistance = Math.max(0, simulator.getShipToVenusDistance());
							minTime = t - departureTime;
						}
						if (simulator.getShipToVenusDistance() <= 0)
							crashed = true;
					} else {
						if (minDistance > Math.max(0, simulator.getShipToMarsDistance())) {
							minDistance = Math.max(0, simulator.getShipToMarsDistance());
							minTime = t - departureTime;
						}
						if (simulator.getShipToMarsDistance() <= 0)
							crashed = true;
					}
				}
				t += input.getDeltaT();
			}
			SpaceReport report = new SpaceReport(departureTime, simulator.getShipVelocity(), minDistance, minTime);
			reports.add(report);
			if (report.getMinDistance() < minGlobalDistance) {
				minGlobalDistance = report.getMinDistance();
				bestLaunchTime = report.getDepartureTime();
			}
			System.out
					.println(departureTime + "\t" + simulator.getShipVelocity() + "\t" + minDistance + "\t" + minTime);
		}
		System.out.println("Mission complete! Best time for launch is " + bestLaunchTime);
		Output.outputShipPreciseReports(reports, false);

		// Analyze velocity
		particles = createPlanets();
		t = 0.0;
		launched = false;
		crashed = false;
		Map<Double, Double> velocityMap = new HashMap<>();
		simulator = new SpaceSimulator(particles, input.getDeltaT());
		int frame = 0;
		Output.resetFolder(Output.OUTPUT_DIR + "/animation");
		while (!crashed && t <= bestLaunchTime + SECONDS_IN_YEAR / 2) {
			if (frame % 1000 == 0)
				Output.outputAnimationData(frame, simulator, false);
			if (t >= bestLaunchTime && !launched) {
				simulator.launchSpaceship(input.getShipVelocity(), toVenus);
				launched = true;
			}
			simulator.updateParticles();
			if (launched)
				velocityMap.put(t, simulator.getShipVelocity());
			if (toVenus) {
				if (launched && simulator.getShipToVenusDistance() <= 0) {
					crashed = true;
					System.out.println("Crashed with speed of " + simulator.getShipVelocity());
					System.out.println(
							"Ship was going at V=(" + simulator.getShipVx() + ", " + simulator.getShipVy() + ")");
					System.out.println(
							"Venus was going at V=(" + simulator.getVenusVx() + ", " + simulator.getVenusVy() + ")");
				}
			} else {
				if (launched && simulator.getShipToMarsDistance() <= 0) {
					crashed = true;
					System.out.println("Crashed with speed of " + simulator.getShipVelocity());
					System.out.println(
							"Ship was going at V=(" + simulator.getShipVx() + ", " + simulator.getShipVy() + ")");
					System.out.println(
							"Mars was going at V=(" + simulator.getMarsVx() + ", " + simulator.getMarsVy() + ")");
				}
			}
			t += input.getDeltaT();
			frame++;
		}
		Output.outputAnimationData(frame, simulator, false);
		Output.outputShipVelocity(velocityMap, false);
		if (!toVenus && crashed)
			MarsToEarth(input, bestLaunchTime, t - bestLaunchTime);
	}

	private static void MarsToEarth(Input input, double originalLaunch, double travelTime) {
		System.out.println("\n\nMISSION TO EARTH\n");
		boolean launched = false;
		boolean crashed = false;
		boolean died = false;
		double t, minDistance, minTime;
		double minGlobalDistance = Double.MAX_VALUE;
		double minLaunchTime = originalLaunch + travelTime;
		// Round up to next midnight
		if ((int) minLaunchTime % (int) SECONDS_IN_DAY != 0)
			minLaunchTime = SECONDS_IN_DAY * (1 + (int) minLaunchTime / (int) SECONDS_IN_DAY);
		List<Particle> particles = createPlanets();
		SpaceSimulator simulator = new SpaceSimulator(particles, input.getDeltaT());

		// Repeat Exercise2 but from Mars
		List<SpaceReport> reports = new ArrayList<>();
		double bestLaunchTime = 0;
		int firstLaunchDay = (int) (minLaunchTime / SECONDS_IN_DAY);
		int finalLaunchDay = firstLaunchDay + DAYS_IN_2_YEARS;
		for (int i = firstLaunchDay; i < finalLaunchDay; i++) {
			double departureTime = i * SECONDS_IN_DAY;
			System.out.println("Day " + i + " (departureTime = " + departureTime + ")\tStarting new launch!");
			particles = createPlanets();
			t = 0.0;
			minDistance = Integer.MAX_VALUE;
			minTime = Integer.MAX_VALUE;
			launched = false;
			crashed = false;
			died = false;
			simulator = new SpaceSimulator(particles, input.getDeltaT());

			while (!crashed && !died && t <= departureTime + SECONDS_IN_YEAR / 2) {
				if (t >= departureTime && !launched) {
					simulator.launchSpaceshipFromMars(input.getShipVelocity());
					launched = true;
				}
				simulator.updateParticles();

				if (launched) {
					if (Math.abs(simulator.getShipToSunDistance()) < Math.abs(simulator.getEarthToSunDistance() / 2))
						died = true;
					if (minDistance > Math.max(0, simulator.getShipToEarthDistance())) {
						minDistance = Math.max(0, simulator.getShipToEarthDistance());
						minTime = t - departureTime;
					}
					if (simulator.getShipToEarthDistance() <= 0)
						crashed = true;
				}
				t += input.getDeltaT();
			}
			SpaceReport report = new SpaceReport(departureTime, simulator.getShipVelocity(), minDistance, minTime);
			reports.add(report);
			if (report.getMinDistance() < minGlobalDistance) {
				minGlobalDistance = report.getMinDistance();
				bestLaunchTime = report.getDepartureTime();
			}
			System.out.println(report.getDepartureTime() + "\t" + report.getShipVelocity() + "\t"
					+ report.getMinDistance() + "\t" + report.getTime());
		}
		System.out.println("Mission complete! Best day was " + bestLaunchTime / SECONDS_IN_DAY + "\n\n");
		Output.outputShipReports(reports, true);

		// Analyze best launch date (Find best time in time frame around that launch)
		reports.clear();
		particles = createPlanets();
		t = 0.0;
		launched = false;
		crashed = false;
		double launchInterval = 60 * MINUTES_MARGIN;
		double launchDay = bestLaunchTime;
		System.out.println("Launch Day is " + launchDay);
		simulator = new SpaceSimulator(particles, input.getDeltaT());
		minGlobalDistance = Double.MAX_VALUE;
		for (double departureTime = launchDay - SECONDS_IN_DAY; departureTime <= launchDay
				+ SECONDS_IN_DAY; departureTime += launchInterval) {
			particles = createPlanets();
			t = 0.0;
			minDistance = Integer.MAX_VALUE;
			minTime = Integer.MAX_VALUE;
			launched = false;
			crashed = false;
			died = false;
			simulator = new SpaceSimulator(particles, input.getDeltaT());

			while (!crashed && !died && t <= departureTime + SECONDS_IN_YEAR / 2) {
				if (t >= departureTime && !launched) {
					simulator.launchSpaceshipFromMars(input.getShipVelocity());
					launched = true;
				}
				simulator.updateParticles();
				if (launched) {
					if (Math.abs(simulator.getShipToSunDistance()) < Math.abs(simulator.getEarthToSunDistance() / 2))
						died = true;
					if (minDistance > Math.max(0, simulator.getShipToEarthDistance())) {
						minDistance = Math.max(0, simulator.getShipToEarthDistance());
						minTime = t - departureTime;
					}
					if (simulator.getShipToEarthDistance() <= 0)
						crashed = true;
				}
				t += input.getDeltaT();
			}
			SpaceReport report = new SpaceReport(departureTime, simulator.getShipVelocity(), minDistance, minTime);
			reports.add(report);
			if (report.getMinDistance() < minGlobalDistance) {
				minGlobalDistance = report.getMinDistance();
				bestLaunchTime = report.getDepartureTime();
			}
			System.out
					.println(departureTime + "\t" + simulator.getShipVelocity() + "\t" + minDistance + "\t" + minTime);
		}
		System.out.println("Mission complete! Best time for launch is " + bestLaunchTime);
		Output.outputShipPreciseReports(reports, true);

		// Analyze velocity
		particles = createPlanets();
		t = 0.0;
		launched = false;
		crashed = false;
		Map<Double, Double> velocityMap = new HashMap<>();
		simulator = new SpaceSimulator(particles, input.getDeltaT());
		int frame = 0;
		Output.resetFolder(Output.OUTPUT_DIR + "/animation-return");
		while (!crashed && t <= bestLaunchTime + SECONDS_IN_YEAR / 2) {
			if (frame % 1000 == 0)
				Output.outputAnimationData(frame, simulator, true);
			if (t >= bestLaunchTime && !launched) {
				simulator.launchSpaceshipFromMars(input.getShipVelocity());
				launched = true;
			}
			simulator.updateParticles();
			if (launched)
				velocityMap.put(t, simulator.getShipVelocity());
			if (launched && simulator.getShipToEarthDistance() <= 0) {
				crashed = true;
				System.out.println("Crashed with speed of " + simulator.getShipVelocity());
				System.out
						.println("Ship was going at V=(" + simulator.getShipVx() + ", " + simulator.getShipVy() + ")");
				System.out.println(
						"Earth was going at V=(" + simulator.getEarthVx() + ", " + simulator.getEarthVy() + ")");
			}
			t += input.getDeltaT();
			frame++;
		}
		Output.outputAnimationData(frame, simulator, true);
		Output.outputShipVelocity(velocityMap, true);
	}

	private static void compareVelocities(Input input) {
		Input temp;
		for (double v = 6; v <= input.getShipVelocity() + 1; v++) {
			temp = new Input(0, input.getDeltaT(), v);
			EarthToMars(temp);
		}
	}

	private static void EarthToMars(Input input) {
		boolean launched = false;
		boolean crashed = false;
		boolean died = false;
		double t, minDistance, minTime;
		double minGlobalDistance = Double.MAX_VALUE;
		double bestLaunchTime = 0;
		System.out.println("DeltaT is " + input.getDeltaT() + " and speed is " + input.getShipVelocity());
		List<Particle> particles = createPlanets();
		SpaceSimulator simulator;
		List<SpaceReport> reports = new ArrayList<>();
		for (int i = 0; i < DAYS_IN_2_YEARS; i++) {
			double departureTime = i * SECONDS_IN_DAY;
			System.out.println("Day " + i + " (departureTime = " + departureTime + ")\tStarting new launch!");
			particles = createPlanets();
			t = 0.0;
			minDistance = Integer.MAX_VALUE;
			minTime = Integer.MAX_VALUE;
			launched = false;
			crashed = false;
			died = false;
			simulator = new SpaceSimulator(particles, input.getDeltaT());

			while (!crashed && !died && t <= departureTime + SECONDS_IN_YEAR / 2) {
				if (t >= departureTime && !launched) {
					simulator.launchSpaceship(input.getShipVelocity(), false);
					launched = true;
				}
				simulator.updateParticles();
				if (launched) {
					if (minDistance > Math.max(0, simulator.getShipToMarsDistance())) {
						minDistance = Math.max(0, simulator.getShipToMarsDistance());
						minTime = t - departureTime;
					}
					if (simulator.getShipToMarsDistance() <= 0)
						crashed = true;
				}
				t += input.getDeltaT();
			}
			SpaceReport report = new SpaceReport(departureTime, simulator.getShipVelocity(), minDistance, minTime);
			reports.add(report);
			if (report.getMinDistance() < minGlobalDistance) {
				minGlobalDistance = report.getMinDistance();
				bestLaunchTime = report.getDepartureTime();
			}
			System.out.println(report.getDepartureTime() + "\t" + report.getShipVelocity() + "\t"
					+ report.getMinDistance() + "\t" + report.getTime());
		}
		Output.outputShipReports(reports, false, "output/spaceships-V" + input.getShipVelocity() + ".txt");

		reports.clear();
		particles = createPlanets();
		t = 0.0;
		launched = false;
		crashed = false;
		died = false;
		double launchInterval = 60 * MINUTES_MARGIN;
		double launchDay = bestLaunchTime;
		simulator = new SpaceSimulator(particles, input.getDeltaT());
		minGlobalDistance = Double.MAX_VALUE;
		for (double departureTime = launchDay - SECONDS_IN_DAY; departureTime <= launchDay
				+ SECONDS_IN_DAY; departureTime += launchInterval) {
			particles = createPlanets();
			t = 0.0;
			minDistance = Integer.MAX_VALUE;
			minTime = Integer.MAX_VALUE;
			launched = false;
			crashed = false;
			died = false;
			simulator = new SpaceSimulator(particles, input.getDeltaT());

			while (!crashed && !died && t <= departureTime + SECONDS_IN_YEAR / 2) {
				if (t >= departureTime && !launched) {
					simulator.launchSpaceship(input.getShipVelocity(), false);
					launched = true;
				}
				simulator.updateParticles();
				if (launched) {
					if (minDistance > Math.max(0, simulator.getShipToMarsDistance())) {
						minDistance = Math.max(0, simulator.getShipToMarsDistance());
						minTime = t - departureTime;
					}
					if (simulator.getShipToMarsDistance() <= 0)
						crashed = true;
				}
				t += input.getDeltaT();
			}
			SpaceReport report = new SpaceReport(departureTime, simulator.getShipVelocity(), minDistance, minTime);
			reports.add(report);
			if (report.getMinDistance() < minGlobalDistance) {
				minGlobalDistance = report.getMinDistance();
				bestLaunchTime = report.getDepartureTime();
			}
			// System.out.println(departureTime +"\t" + simulator.getShipVelocity() +"\t"
			// +minDistance +"\t" +minTime);
		}
		System.out.println("Mission complete! Best time for launch is " + bestLaunchTime);
		Output.outputShipPreciseReports(reports, false,
				"output/spaceships-precise-V" + input.getShipVelocity() + ".txt");

		// Analyze velocity
		particles = createPlanets();
		t = 0.0;
		launched = false;
		crashed = false;
		Map<Double, Double> velocityMap = new HashMap<>();
		simulator = new SpaceSimulator(particles, input.getDeltaT());
		int frame = 0;
		bestLaunchTime = 3.03777E7;
		while (!crashed && t <= bestLaunchTime + SECONDS_IN_YEAR / 2) {
			if (frame % 10 == 0)
				Output.outputAnimationData(frame, simulator, false);
			if (t >= bestLaunchTime && !launched) {
				simulator.launchSpaceship(input.getShipVelocity(), false);
				launched = true;
			}
			simulator.updateParticles();
			if (launched)
				velocityMap.put(t, simulator.getShipVelocity());
			if (launched && simulator.getShipToMarsDistance() <= 0) {
				crashed = true;
				System.out.println("Crashed with speed of " + simulator.getShipVelocity());
				System.out.println("Crashed in instant " + t);
				System.out
						.println("Ship was going at V=(" + simulator.getShipVx() + ", " + simulator.getShipVy() + ")");
				System.out
						.println("Mars was going at V=(" + simulator.getMarsVx() + ", " + simulator.getMarsVy() + ")");
				System.out.println("Relative V = " + getV(simulator.getShipVx(), simulator.getShipVy(),
						simulator.getMarsVx(), simulator.getMarsVy()));
			}
			t += input.getDeltaT();
			frame++;
		}
		Output.outputShipVelocity(velocityMap, false, "output/spaceship-velocity-V" + input.getShipVelocity() + ".txt");
		System.out.println("Mission complete! Best day was " + bestLaunchTime / SECONDS_IN_DAY + "\n\n");
	}

	private static void launchAtPerfectTime(Input input, boolean toVenus, boolean toEarth) {
		boolean launched = false;
		boolean crashed = false;
		double t = 0.0;
		double bestLaunchTime;
		if (toVenus) {
			bestLaunchTime = 1178100;
		} else {
			if( toEarth ) {
				bestLaunchTime = 9.16854E7;
			} else {
				bestLaunchTime = 3.0168E7;
			}
		}
		double minDistance = Double.MAX_VALUE;
		double minT = Double.MAX_VALUE;
		List<Particle> particles = createPlanets();
		SpaceSimulator simulator;
		simulator = new SpaceSimulator(particles, input.getDeltaT());
		Output.resetFolder(Output.OUTPUT_DIR + "/animation");
		while (!crashed && t <= bestLaunchTime + SECONDS_IN_YEAR / 2) {
			if (t >= bestLaunchTime && !launched) {
				if( toEarth ) {
					simulator.launchSpaceshipFromMars(input.getShipVelocity());
				} else {
					simulator.launchSpaceship(input.getShipVelocity(), true);
				}
				launched = true;
			}
			simulator.updateParticles();
			if (launched) {
				if (toVenus) {
					if (simulator.getShipToVenusDistance() <= 0) {
						minDistance = 0;
						crashed = true;
						minT = t - bestLaunchTime;
					} else if (simulator.getShipToVenusDistance() <= minDistance) {
						minDistance = simulator.getShipToVenusDistance();
						minT = t - bestLaunchTime;
					}
				} else {
					if( toEarth ) {
						if (simulator.getShipToEarthDistance() <= 0) {
							minDistance = 0;
							crashed = true;
							minT = t - bestLaunchTime;
						} else if (simulator.getShipToEarthDistance() <= minDistance) {
							minDistance = simulator.getShipToEarthDistance();
							minT = t - bestLaunchTime;
						}
					} else {
						if (simulator.getShipToMarsDistance() <= 0) {
							minDistance = 0;
							crashed = true;
							minT = t - bestLaunchTime;
						} else if (simulator.getShipToMarsDistance() <= minDistance) {
							minDistance = simulator.getShipToMarsDistance();
							minT = t - bestLaunchTime;
						}
					}
				}
			}
			t += input.getDeltaT();
		}
		System.out.println(input.getShipVelocity() + "\t" + minT + "\t" + minDistance);
	}

	private static void calculateErrors() {
		List<Particle> particles;
		Map<Double, Double> map = new HashMap<>();
		for (double dt = 0.1; dt <= 10000; dt *= 10) {
			System.out.println("Starting dt = " + dt);
			particles = createPlanets();
			double t = 0.0;
			SpaceSimulator simulator = new SpaceSimulator(particles, dt);
			double initialEnergy = simulator.getSystemEnergy();
			double error = 0.0;
			while (t <= 30 * SECONDS_IN_DAY) {
				simulator.updateParticles();
				t += dt;
			}
			error = Math.abs((simulator.getSystemEnergy() - initialEnergy) / initialEnergy);
			System.out.println(dt + "\t" + error);
			map.put(dt, error);
		}
		Output.outputRocketError(map);
	}

	private static List<Particle> createPlanets() {
		Particle sun = new Particle(1, 0, 0, 0, 0, 1988500 * Math.pow(10, 24), 696000);
		Particle earth = new Particle(2, 1.500619962348151E+08, 2.288499248197072E+06, -9.322979134387409E-01,
				2.966365033636722E+01, 5.97219 * Math.pow(10, 24), 6371.01);
		Particle mars = new Particle(3, -2.426617401833969E+08, -3.578836154354768E+07, 4.435907910045917E+00,
				-2.190044178514185E+01, 6.4171 * Math.pow(10, 23), 3389.92);
		Particle venus = new Particle(4, 5.014551876053274E+07, -9.659685434359106E+07, 3.084680652374887E+01,
				1.601542153811616E+01, 48.685 * Math.pow(10, 23), 6051.84);
		List<Particle> particles = new ArrayList<>();

		particles.add(sun);
		particles.add(earth);
		particles.add(mars);
		particles.add(venus);
		return particles;
	}

}
