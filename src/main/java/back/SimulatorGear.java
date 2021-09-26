package back;
public class SimulatorGear implements Simulator {
	private double deltaT;
	private double k;
	private double gamma;
	private Particle p;
	
	private double alpha[] = {3.0/16.0, 251.0/360.0, 1.0, 11.0/18.0, 1.0/6.0, 1.0/60.0};
	private double r[] = new double[6];
	
	public SimulatorGear(Particle p, double deltaT, double k, double gamma)
	{
		this.p = p;
		this.deltaT = deltaT;
		this.k = k;
		this.gamma = gamma;
		this.r[0] = p.getX();
		this.r[1] = p.getVx();
		for(int i = 2; i < 6; i++)
			r[i] = Particle.getAcceleration(r[i-2], r[i-1], p.getMass(), k, gamma);
	}
	
	@Override
	public void updateParticle() {
		// Step 1: Predict
		double nextRp[] = new double[6];
		for(int i=0; i < nextRp.length; i++)
			nextRp[i] = predict(i);
		
		// Step 2: Evaluate
		double nextA = Particle.getAcceleration(nextRp[0], nextRp[1], p.getMass(), k, gamma);
		double nextAp = nextRp[2];
		double deltaR2 = (nextA - nextAp) * Math.pow(deltaT, 2) * 0.5;
		
		// Step 3: Correct
		for(int q=0; q < 6; q++)
			r[q] = nextRp[q] + (alpha[q]*deltaR2*factorial(q)) / Math.pow(deltaT, q);
		p.setX(r[0]);
		p.setVx(r[1]);
	}
	
	private double predict(int index)
	{
		double value = r[index];
		for(int i=1; index+i < 6; i++)
			value += r[index+i] * Math.pow(deltaT, i)/factorial(i);
		return value;
	}
	
	private double factorial(int num)
	{
		int base = 1;
		for(int j = 2; j<=num; j++)
			base = base*j;
		return base;
	}
}
