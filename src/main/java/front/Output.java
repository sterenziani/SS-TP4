package front;
import back.Particle;
import back.SpaceReport;
import back.SpaceSimulator;
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
	
	private static File createFile(String outputFileName, String header)
	{
    	File file = new File(outputFileName);
		try
		{
			if(!file.createNewFile())
				file.delete();
			FileWriter writer = new FileWriter(outputFileName, true);
			writer.write(header+"\n");
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
    	File file = createFile(outputFileName, "t;x");
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

	public static void outputShipReports(List<SpaceReport> reports, boolean fromMars)
	{
		String outputFileName = OUTPUT_DIR +"/spaceships.txt";
		if(fromMars)
			outputFileName = OUTPUT_DIR +"/spaceships-return.txt";
		outputShipReports(reports, fromMars, outputFileName);
	}
	
	public static void outputShipReports(List<SpaceReport> reports, boolean fromMars, String outputFileName)
	{
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
	
	public static void outputShipPreciseReports(List<SpaceReport> reports, boolean fromMars)
	{
		String outputFileName = OUTPUT_DIR +"/spaceships-precise.txt";
		if(fromMars)
			outputFileName = OUTPUT_DIR +"/spaceships-return-precise.txt";
		outputShipPreciseReports(reports, fromMars, outputFileName);
	}
	
	public static void outputShipPreciseReports(List<SpaceReport> reports, boolean fromMars, String outputFileName)
	{

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
	
	public static void outputShipVelocity(Map<Double, Double> map, boolean fromMars)
	{
		String outputFileName = OUTPUT_DIR +"/spaceship-velocity.txt";
		if(fromMars)
			outputFileName = OUTPUT_DIR +"/spaceship-return-velocity.txt";
		outputShipVelocity(map, fromMars, outputFileName);
	}
	
	public static void outputShipVelocity(Map<Double, Double> map, boolean fromMars, String outputFileName)
	{
		File file = createFile(outputFileName, "t;v");
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
	
	public static void outputDistanceEvolution(Map<Double, Double> map)
	{
		String outputFileName = OUTPUT_DIR +"/spaceship-distance.txt";
		File file = createFile(outputFileName, "t;d");
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
	
	public static void outputAnimationData(int frame, SpaceSimulator simulator, boolean fromMars)
	{
		File file;
		if(fromMars)
			file = createFile(OUTPUT_DIR +"/animation-return/anim" +frame +".xyz", simulator.getParticles().size()+"\n");
		else
			file = createFile(OUTPUT_DIR +"/animation/anim" +frame +".xyz", simulator.getParticles().size()+"\n");
		double[] r = {255, 25, 200, 155, 255};
		double[] g = {200, 100, 25, 103, 255};
		double[] b = {25, 255, 25, 60, 255};
		double[] multipliers = {50, 1500, 1500, 1500, 20005000};
	    try (FileWriter writer = new FileWriter(file, true))
	    {
	    	for(Particle p : simulator.getParticles())
	    	{
	    		int index = p.getId()-1;
				writer.write(p.getX()+"\t"+p.getY()+"\t"+p.getRadius()*multipliers[index]+"\t"
							+r[index]/255+"\t"+g[index]/255+"\t"+b[index]/255+"\n");
	    	}
	    	writer.close();
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	}
	
	public static void outputRocketError(Map<Double, Double> map)
	{
		String outputFileName = OUTPUT_DIR +"/spaceship-error.txt";
    	File file = createFile(outputFileName, "dt;error");
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
    
}
