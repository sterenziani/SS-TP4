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

	public void launchSpaceship(double shipVelocity, boolean reverse)
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
		
		// If going to Venus, ship should be launched on the side facing Venus, not the Mars side
		if(reverse)
		{
			x = earth.getX() - enx*(spaceStationHeight + earth.getRadius());
			y = earth.getY() - eny*(spaceStationHeight + earth.getRadius());
			vx = earth.getVx() + eny*(orbitalVelocity + shipVelocity);
			vy = earth.getVy() - enx*(orbitalVelocity + shipVelocity);
		}

		Particle ship = new Particle(5, x, y, vx, vy, shipMass, shipRadius);
		particles.add(ship);
		gearSystem = new SpaceGearAlgorithm(particles, deltaT, gearSystem);
	}
	
	public void launchSpaceshipFromMars(double shipVelocity)
	{
		Particle sun = particles.get(0);
		Particle mars = particles.get(2);
		
		double spaceStationHeight = 1500;
		double orbitalVelocity = 7.12;
		double shipMass = 2*Math.pow(10,5);
		double shipRadius = 0.1;

		double dx = mars.getX() - sun.getX();
		double dy = mars.getY() - sun.getY();
		double dist = Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));

		double enx = dx/dist;
		double eny = dy/dist;

		double x = mars.getX() - enx*(spaceStationHeight + mars.getRadius());
		double y = mars.getY() - eny*(spaceStationHeight + mars.getRadius());
		double vx = mars.getVx() + eny*(orbitalVelocity + shipVelocity);
		double vy = mars.getVy() - enx*(orbitalVelocity + shipVelocity);

		Particle ship = new Particle(5, x, y, vx, vy, shipMass, shipRadius);
		particles.add(ship);
		gearSystem = new SpaceGearAlgorithm(particles, deltaT, gearSystem);
	}

	public void updateParticles() {
		gearSystem.updateParticles();
	}
	
	public double getShipToSunDistance() {
		Particle sun = particles.get(0);
		Particle ship = particles.get(4);
		return ship.getEdgeDistance(sun);
	}
	
	public double getShipToEarthDistance() {
		Particle earth = particles.get(1);
		Particle ship = particles.get(4);
		return ship.getEdgeDistance(earth);
	}
	
	public double getShipToMarsDistance() {
		Particle mars = particles.get(2);
		Particle ship = particles.get(4);
		return ship.getEdgeDistance(mars);
	}
	
	public double getShipToVenusDistance() {
		Particle venus = particles.get(3);
		Particle ship = particles.get(4);
		return ship.getEdgeDistance(venus);
	}

	public double getVenusToSunDistance() {
		Particle venus = particles.get(3);
		Particle sun = particles.get(0);
		return venus.getEdgeDistance(sun);
	}
	
	public double getEarthToSunDistance() {
		Particle earth = particles.get(1);
		Particle sun = particles.get(0);
		return earth.getEdgeDistance(sun);
	}
	
	public double getShipVelocity() {
		Particle ship = particles.get(4);
		return ship.getV();
	}
	
	public List<Particle> getParticles() {
		return particles;
	}
}
