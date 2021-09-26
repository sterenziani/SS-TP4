import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import back.Particle;
import back.Simulator;
import back.SimulatorBeeman;
import back.SimulatorGear;
import back.SimulatorOscillation;
import back.SimulatorVerlet;
import front.Input;
import front.Output;
import front.Parser;

public class App {
	
	public static void main(String[] args) throws IOException
	{
		Input input = Parser.ParseInputFile("input.txt");
		//Output.resetFolder(Output.OUTPUT_DIR);
		if(input.getExercise() == 1)
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
			int round = 0;
			Map<Double, Double> map = new HashMap<>();
			while(t < tf)
			{
				//if(round % 10 == 0)
					map.put(t, ball.getX());
				simulator.updateParticle();
				t += input.getDeltaT();
				round++;
			}
			Output.outputOscillatorToFile(input.getAlgorithm(), input.getDeltaT(), map);
			System.out.println("Simulation complete!");
		}
		else if(input.getExercise() == 2)
		{
			
		}
		else
		{
			System.out.println("There is no exercise #" +input.getExercise());
		}
	}
}
