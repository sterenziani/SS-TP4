import java.io.IOException;
import front.Input;
import front.Output;
import front.Parser;

public class App {
	
	public static void main(String[] args) throws IOException
	{
		Input input = Parser.ParseInputFile("input.txt");
		Output.resetFolder(Output.OUTPUT_DIR);
		System.out.println("Parsed input and reset output folder!");
	}
}
