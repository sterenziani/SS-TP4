package front;

public class Input {
    
	private int exercise;
	private double deltaT;
	private String algorithm;
	
	public Input(int exercise, double deltaT, String algorithm)
	{
		this.exercise = exercise;
		this.deltaT = deltaT;
		this.algorithm = algorithm;
	}
	
	public int getExercise() {
		return exercise;
	}
	
	public double getDeltaT() {
		return deltaT;
	}
	
	public String getAlgorithm() {
		return algorithm;
	}
}
