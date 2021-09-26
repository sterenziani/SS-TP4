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
        int exercise = scanner.nextInt();
        double deltaT = scanner.nextDouble();		// 10^-6 = 0.000001
        String algorithm = scanner.next();
		return new Input(exercise, deltaT, algorithm);  
    }
}
