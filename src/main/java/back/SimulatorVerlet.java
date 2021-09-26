package back;

public class SimulatorVerlet implements Simulator {

	private double deltaT;
	private double k;
	private double gamma;
	private Particle p;
	
	private double a;
	private double prev_r;
	
	public SimulatorVerlet(Particle p, double deltaT, double k, double gamma)
	{
		this.p = p;
		this.deltaT = deltaT;
		this.k = k;
		this.gamma = gamma;
		this.a = Particle.getAcceleration(p.getX(), p.getVx(), p.getMass(), k, gamma);
		this.prev_r = getPrevPositionEuler();
	}
	
	@Override
	public void updateParticle()
	{
		double nextR = getNextPosition();
		prev_r = p.getX();
		p.setX(nextR);
		p.setVx(getCurrentVelocity());
		a = Particle.getAcceleration(p.getX(), p.getVx(), p.getMass(), k, gamma);
	}
	
	private double getNextPosition() {
		return 2*p.getX() - prev_r + a*Math.pow(deltaT, 2);
	}

	private double getCurrentVelocity() {
		return (getNextPosition() - prev_r) / (2*deltaT);
	}
	
	// TODO: Preguntar si es así
	private double getPrevPositionEuler() {
		return p.getX() - getPrevVelocityEuler()*deltaT + 0.5*a*Math.pow(-deltaT, 2);
	}
	
	// TODO: Preguntar si es así
	private double getPrevVelocityEuler() {
		return p.getVx() - a*deltaT;
	}
}
