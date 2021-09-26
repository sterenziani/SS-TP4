package back;

public class SimulatorBeeman implements Simulator {
	
	private double deltaT;
	private double k;
	private double gamma;
	private Particle p;

	private double a;
	private double prev_a;
	private boolean predictor;
	
	public SimulatorBeeman(Particle p, double deltaT, double k, double gamma, boolean predictor)
	{
		this.p = p;
		this.deltaT = deltaT;
		this.k = k;
		this.gamma = gamma;
		double r = p.getX();
		double v = p.getVx();
		this.a = Particle.getAcceleration(r, v, p.getMass(), k, gamma);
		this.prev_a = Particle.getAcceleration(r - v*deltaT, v - a*deltaT, p.getMass(), k, gamma);
		this.predictor = predictor;
	}
	
	@Override
	public void updateParticle() {
		p.setX(getNextPosition());
		if(predictor)
			p.setVx(getNextVelocityPredictorCorrector());
		else
			p.setVx(getNextVelocityNormal());
		prev_a = a;
		a = Particle.getAcceleration(p.getX(), p.getVx(), p.getMass(), k, gamma);
	}
	
	private double getNextPosition() {
		return p.getX() + p.getVx()*deltaT + (2.0/3.0)*a*Math.pow(deltaT, 2) - (1.0/6.0)*prev_a*Math.pow(deltaT, 2);
	}

	private double getNextVelocityPredictorCorrector() {
		double predictedV = p.getVx() + (3.0/2.0)*a*deltaT - 0.5*prev_a*deltaT;
		double next_a = Particle.getAcceleration(p.getX(), predictedV, p.getMass(), k, gamma);
		return p.getVx() + (1.0/3.0)*next_a*deltaT + (5.0/6.0)*a*deltaT - (1.0/6.0)*prev_a*deltaT;
	}
	
	private double getNextVelocityNormal() {
		// Calculates nextA using currentV instead of nextV
		double nextA = Particle.getAcceleration(p.getX(), p.getVx(), p.getMass(), k, gamma);
		return (p.getVx() + deltaT*(2*nextA + 5*a - prev_a)/6);
	}

}
