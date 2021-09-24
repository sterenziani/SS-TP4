package front;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {
	
    @SuppressWarnings("resource")
	public static Input ParseInputFile(String filename) throws FileNotFoundException
    {
		FileInputStream file = new FileInputStream(filename);  
        Scanner scanner = new Scanner(file);
        /*
        int N = scanner.nextInt();
        double width = scanner.nextDouble();
        double height = scanner.nextDouble();
        double gap = scanner.nextDouble();
        Collection<Particle> particles = new LinkedList<>();
        for (int i=0; i < N; i++)
        {
        	double x = scanner.nextDouble();
        	double y = scanner.nextDouble();
        	double vx = scanner.nextDouble();
        	double vy = scanner.nextDouble();
        	double mass = scanner.nextDouble();
        	double radius = scanner.nextDouble();
        	Particle p = new Particle(i, x, y, vx, vy, mass, radius);    
        	particles.add(p);
        }
        */
		return new Input();  
    }
}
