package front;

public class Input {
    
	private int exercise;
	private double deltaT;
	// Ej 1
	private String algorithm;
	
	// Ej 2
	private double shipVelocity;
	
	public Input(int exercise, double deltaT, String algorithm)
	{
		this.exercise = exercise;
		this.deltaT = deltaT;
		this.algorithm = algorithm;
	}
	
	public Input(int exercise, double deltaT, double shipVelocity)
	{
		this.exercise = exercise;
		this.deltaT = deltaT;
		this.shipVelocity = shipVelocity;
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

	public double getShipVelocity() {
		return shipVelocity;
	}
}
