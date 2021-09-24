package front;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Output {
	public static String OUTPUT_DIR = "output";
	
	public static void outputToFile()
	{
		outputToFile("output.txt");
	}
	
    public static void outputToFile(String outputFileName)
    {
    	File file = new File(outputFileName);
		try
		{
			if(file.createNewFile())
			{
				FileWriter writer = new FileWriter(outputFileName, true);
				writer.write("Hola\n");
				writer.close();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
        try (FileWriter writer = new FileWriter(outputFileName, true))
        {
			writer.write("forEach\n");
        	writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void resetFolder(String folderName)
    {
    	File folder = new File(folderName);
        File[] files = folder.listFiles();
        if(files!=null)
        {
            for(File f: files)
                f.delete();
        }
        folder.delete();
        folder.mkdir();        
    }
    
}
