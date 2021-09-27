package back;
import java.util.List;

public class SpaceSimulator
{
	private List<Particle> particles;
	private double deltaT;
	private SpaceGearAlgorithm gearSystem;
	
	public SpaceSimulator(List<Particle> particles, double deltaT)
	{
		this.particles = particles;
		this.deltaT = deltaT;
		this.gearSystem = new SpaceGearAlgorithm(particles, deltaT, null);
	}

	public void launchSpaceship(double shipVelocity)
	{
		Particle sun = particles.get(0);
		Particle earth = particles.get(1);
		
		double spaceStationHeight = 1500;
		double orbitalVelocity = 7.12;
		double shipMass = 2*Math.pow(10,5);
		double shipRadius = 0.1;

		double dx = earth.getX() - sun.getX();
		double dy = earth.getY() - sun.getY();
		double dist = Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));

		double enx = dx/dist;
		double eny = dy/dist;

		double x = earth.getX() + enx*(spaceStationHeight + earth.getRadius());
		double y = earth.getY() + eny*(spaceStationHeight + earth.getRadius());
		double vx = earth.getVx() - eny*(orbitalVelocity + shipVelocity);
		double vy = earth.getVy() + enx*(orbitalVelocity + shipVelocity);

		Particle ship = new Particle(4, x, y, vx, vy, shipMass, shipRadius);
		particles.add(ship);
		gearSystem = new SpaceGearAlgorithm(particles, deltaT, gearSystem);
	}

	public void updateParticles() {
		gearSystem.updateParticles();
	}
	
	public double getShipToMarsDistance() {
		Particle mars = particles.get(2);
		Particle ship = particles.get(3);
		return ship.getEdgeDistance(mars);
	}
	
	public double getShipVelocity() {
		Particle ship = particles.get(3);
		return ship.getV();
	}
}
