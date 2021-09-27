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
	
	private static final int SECONDS_IN_DAY = 24*3600;
	private static final int SECONDS_IN_YEAR = 365*SECONDS_IN_DAY;
	private static final int DAYS_IN_4_YEARS = 1460;
	
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
		boolean blast_off = false;
		double t, minDistance, minTime, shipVelocity;
		
		// Units are in KM to simplify distance math
		List<Particle> particles = createPlanets();
		SpaceSimulator simulator;
		List<SpaceReport> reports = new ArrayList<>();
		for(int i=0; i < DAYS_IN_4_YEARS; i++)
		{
			System.out.println("Day " +i +"\tStarting new launch!");
			int departureTime = i*SECONDS_IN_DAY;
			particles = createPlanets();
			t = 0.0;
			minDistance = Integer.MAX_VALUE;
			minTime = Integer.MAX_VALUE;
			shipVelocity = 0;
			blast_off = false;
			simulator = new SpaceSimulator(particles, input.getDeltaT());
			
			while(t <= departureTime + SECONDS_IN_YEAR/2)
			{
				if(t >= departureTime && !blast_off)
				{
					simulator.launchSpaceship(input.getShipVelocity());
					blast_off = true;
				}
				simulator.updateParticles();
				
				if (blast_off)
				{
					if (minDistance >= simulator.getShipToMarsDistance())
					{
						minDistance = simulator.getShipToMarsDistance();
						minTime = t - departureTime;						
					}
					if (simulator.getShipToMarsDistance() <= 0)
						shipVelocity = simulator.getShipVelocity();
				}
				t += input.getDeltaT();
			}
			reports.add(new SpaceReport(departureTime, shipVelocity, minDistance, minTime));
		}
		Output.outputShipReports(reports);
		System.out.println("Mission complete!");
	}
	
	private static List<Particle> createPlanets()
	{
		Particle sun = new Particle(1, 0, 0, 0, 0, 1988500*Math.pow(10, 24), 696000);
		Particle earth = new Particle(2, 1.501206577633135E+08, -2.745908419719997E+05, -4.256971057184570E-0, 2.966576927417303E+01, 5.97219*Math.pow(10, 24), 6371.01);
		Particle mars = new Particle(3, -2.430368627109727E+08, -3.389498425155429E+07, 4.247475113528548E+00, -2.192748577413463E+01, 6.4171*Math.pow(10, 23), 3389.92);
		List<Particle> particles = new ArrayList<>();
		particles.add(sun);
		particles.add(earth);
		particles.add(mars);
		return particles;
	}
}
