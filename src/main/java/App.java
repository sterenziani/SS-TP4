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
	
	private static final double SECONDS_IN_DAY = 24*3600;
	private static final double SECONDS_IN_YEAR = 365*SECONDS_IN_DAY;
	private static final double DAYS_IN_4_YEARS = 1460;
	
	public static void main(String[] args) throws IOException
	{
		Input input = Parser.ParseInputFile("input.txt");
		//Output.resetFolder(Output.OUTPUT_DIR);
		if(input.getExercise() == 1)
			Exercise1(input);
		else if(input.getExercise() == 2)
			Exercise2(input);
		else
			System.out.println("There is no exercise #" +input.getExercise());
	}
	
	private static void Exercise1(Input input)
	{
		// Parametros
		double m = 70;
		double k = Math.pow(10, 4);
		double gamma = 100;
		double tf = 5;
		
		// Condiciones iniciales
		double r0 = 1;
		double v0 = -r0*gamma / (2*m);	// r0 = A * e^0 * cos(0)  ==>  A = 1
		Particle ball = new Particle(1, r0, 0, v0, 0, m, 0);
		
		// Preparar algoritmo
		Simulator simulator = null;
		switch(input.getAlgorithm())
		{
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
				System.out.println("Invalid algorithm " +input.getAlgorithm());
		}
		
		// Simulación
		double t = 0.0;
		Map<Double, Double> map = new HashMap<>();
		while(t < tf)
		{
			map.put(t, ball.getX());
			simulator.updateParticle();
			t += input.getDeltaT();
		}
		Output.outputOscillatorToFile(input.getAlgorithm(), input.getDeltaT(), map);
		System.out.println("Simulation complete!");
	}
	
	private static void Exercise2(Input input)
	{
		boolean launched = false;
		boolean crashed = false;
		double t, minDistance, minTime;
		double minGlobalDistance = Double.MAX_VALUE;
		double bestLaunchTime = 0;
		System.out.println("DeltaT is " +input.getDeltaT() +" and speed is " +input.getShipVelocity());
		
		// Units are in KM to simplify distance math
		List<Particle> particles = createPlanets();
		SpaceSimulator simulator;
		List<SpaceReport> reports = new ArrayList<>();
		
		for(int i=0; i < DAYS_IN_4_YEARS; i++)
		{
			System.out.println("Day " +i +"\tStarting new launch!");
			double departureTime = i*SECONDS_IN_DAY;
			particles = createPlanets();
			t = 0.0;
			minDistance = Integer.MAX_VALUE;
			minTime = Integer.MAX_VALUE;
			launched = false;
			crashed = false;
			simulator = new SpaceSimulator(particles, input.getDeltaT());
			
			while(!crashed && t <= departureTime + SECONDS_IN_YEAR/2)
			{
				if(t >= departureTime && !launched)
				{
					simulator.launchSpaceship(input.getShipVelocity());
					launched = true;
				}
				simulator.updateParticles();
				
				if (launched)
				{
					if (minDistance >= simulator.getShipToMarsDistance())
					{
						minDistance = simulator.getShipToMarsDistance();
						minTime = t - departureTime;						
					}
					if (simulator.getShipToMarsDistance() <= 0)
						crashed = true;
				}
				t += input.getDeltaT();
			}
			SpaceReport report = new SpaceReport(departureTime, simulator.getShipVelocity(), minDistance, minTime);
			reports.add(report);
			if(report.getMinDistance() < minGlobalDistance)
			{
				minGlobalDistance = report.getMinDistance();
				bestLaunchTime = report.getDepartureTime();
			}
			System.out.println(report.getDepartureTime() +"\t" + report.getShipVelocity() +"\t" +report.getMinDistance() +"\t" +report.getTime());
		}
		System.out.println("Mission complete! Best day was " +bestLaunchTime/SECONDS_IN_DAY +"\n\n");
		Output.outputShipReports(reports);
		
		// Analyze best launch date (Find best time in time frame around that launch)
		reports.clear();
		particles = createPlanets();
		t = 0.0;
		launched = false;
		crashed = false;
		double launchInterval = 60*5;
		simulator = new SpaceSimulator(particles, input.getDeltaT());
		minGlobalDistance = Double.MAX_VALUE;
		//bestLaunchTime = 705*SECONDS_IN_DAY; // Uncomment for 1b debug
		for(double departureTime = bestLaunchTime-SECONDS_IN_DAY; departureTime < bestLaunchTime+SECONDS_IN_DAY; departureTime += launchInterval)
		{
			particles = createPlanets();
			t = 0.0;
			minDistance = Integer.MAX_VALUE;
			minTime = Integer.MAX_VALUE;
			launched = false;
			crashed = false;
			simulator = new SpaceSimulator(particles, input.getDeltaT());
			
			while(!crashed && t <= departureTime + SECONDS_IN_YEAR/2)
			{
				if(t >= departureTime && !launched)
				{
					simulator.launchSpaceship(input.getShipVelocity());
					launched = true;
				}
				simulator.updateParticles();
				if (launched)
				{
					if (simulator.getShipToMarsDistance() <= minDistance)
					{
						minDistance = simulator.getShipToMarsDistance();
						minTime = t - departureTime;						
					}
					if (simulator.getShipToMarsDistance() <= 0)
						crashed = true;
				}
				t += input.getDeltaT();
			}
			SpaceReport report = new SpaceReport(departureTime, simulator.getShipVelocity(), minDistance, minTime);
			reports.add(report);
			if(report.getMinDistance() < minGlobalDistance)
			{
				minGlobalDistance = report.getMinDistance();
				bestLaunchTime = report.getDepartureTime();
			}
			System.out.println(departureTime +"\t" + simulator.getShipVelocity() +"\t" +minDistance +"\t" +minTime);
		}
		System.out.println("Mission complete! Best time for launch is " +bestLaunchTime);
		Output.outputShipPreciseReports(reports);
		
		// Analyze velocity
		//bestLaunchTime = 6.08859E7; // Uncomment for 1c debug
		particles = createPlanets();
		t = 0.0;
		launched = false;
		crashed = false;
		Map<Double, Double> velocityMap = new HashMap<>();
		simulator = new SpaceSimulator(particles, input.getDeltaT());
		while(!crashed && t <= bestLaunchTime + SECONDS_IN_YEAR/2)
		{
			if(t >= bestLaunchTime && !launched)
			{
				simulator.launchSpaceship(input.getShipVelocity());
				launched = true;
			}
			simulator.updateParticles();
			if(launched)
				velocityMap.put(t, simulator.getShipVelocity());
			if(launched && simulator.getShipToMarsDistance() <= 0)
			{
				crashed = true;
				System.out.println("Crashed with speed of " +simulator.getShipVelocity());
			}
			t += input.getDeltaT();
		}
		Output.outputShipVelocity(velocityMap);
	}
	
	private static List<Particle> createPlanets()
	{
		Particle sun = new Particle(1, 0, 0, 0, 0, 1988500*Math.pow(10, 24), 696000);
		Particle earth = new Particle(2, 1.500619962348151E+08, 2.288499248197072E+06, -9.322979134387409E-01, 2.966365033636722E+01, 5.97219*Math.pow(10, 24), 6371.01);
		Particle mars = new Particle(3, -2.426617401833969E+08, -3.578836154354768E+07, 4.435907910045917E+00, -2.190044178514185E+01, 6.4171*Math.pow(10, 23), 3389.92);
		
		// Old data
		sun = new Particle(1, 0, 0, 0, 0, 1.989e+30, 696340);
		earth = new Particle(2, 1.493188929636662E+08, 1.318936357931255E+07, -3.113279917782445, 2.955205189256462E+01, 5.972e+24, 6371);
		mars = new Particle(3, 2.059448551842169E+08, 4.023977946528339E+07, -3.717406842095575, 2.584914078301731E+01, 6.39e+23, 3389.5);
		
		List<Particle> particles = new ArrayList<>();
		particles.add(sun);
		particles.add(earth);
		particles.add(mars);
		return particles;
	}
}
