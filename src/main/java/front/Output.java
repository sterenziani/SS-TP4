package front;
import back.SpaceReport;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Output {
	public static String OUTPUT_DIR = "output";
	
	private static File createOscillatorFile(String outputFileName)
	{
    	File file = new File(outputFileName);
		try
		{
			if(!file.createNewFile())
				file.delete();
			FileWriter writer = new FileWriter(outputFileName, true);
			writer.write("t;x\n");
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return file;
	}
	
	private static File createSpaceshipFile(String outputFileName)
	{
    	File file = new File(outputFileName);
		try
		{
			if(!file.createNewFile())
				file.delete();
			FileWriter writer = new FileWriter(outputFileName, true);
			writer.write("departureTime;shipVelocity;minDistance;minTime\n");
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return file;
	}
	
	public static void outputOscillatorToFile(String algorithm, double deltaT, Map<Double, Double> map)
	{
		String outputFileName = OUTPUT_DIR +"/output" +algorithm +(int)Math.log10(deltaT) +".txt";
    	File file = createOscillatorFile(outputFileName);
    	List<Entry<Double, Double>> entries = new ArrayList<Map.Entry<Double, Double>>(map.entrySet());
    	Collections.sort(entries, new Comparator<Map.Entry<Double, Double>>()
		    	{
    		        public int compare(Entry<Double, Double> a, Entry<Double, Double> b)
    		        {	return Double.compare(a.getKey(), b.getKey());	}
		    	});
    	
        try (FileWriter writer = new FileWriter(file, true))
        {
        	for(Entry<Double, Double> e : entries)
    			writer.write(e.getKey() +";" +e.getValue() +"\n");
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

	public static void outputShipReports(List<SpaceReport> reports)
	{
		String outputFileName = OUTPUT_DIR +"/spaceships.txt";
    	File file = createSpaceshipFile(outputFileName);
        try (FileWriter writer = new FileWriter(file, true))
        {
        	for(SpaceReport r : reports)
    			writer.write(r.getDepartureTime() +";" +r.getShipVelocity() +";" +r.getMinDistance() +";" +r.getTime() +"\n");
        	writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
	}
    
}
