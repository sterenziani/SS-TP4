package back;

public class SimulatorOscillation implements Simulator {
	private double deltaT;
	private double k;
	private double gamma;
	private Particle p;
	private double A;
	private double time;
	
	public SimulatorOscillation(Particle p, double deltaT, double k, double gamma, double amplitude)
	{
		this.p = p;
		this.deltaT = deltaT;
		this.k = k;
		this.gamma = gamma;
		this.A = amplitude;
		this.time = 0;
	}
	
	@Override
	public void updateParticle()
	{
		time += deltaT;
		p.setX(getPosition(time));
	}
	
	public double getPosition(double t)
	{
		double m = p.getMass();
		return A * Math.exp(-gamma*t / (2*m)) * Math.cos(t * Math.sqrt(k/m - gamma*gamma/(4*m*m)));
	}
}
