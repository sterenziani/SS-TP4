package back;
import java.util.List;

public class SpaceSimulator
{
	private List<Particle> particles;
	private double deltaT;
	private SpaceGearAlgorithm gearSystem;
	private static final double G = 6.693*Math.pow(10, -20);
	
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
	
	public double getShipVx() {
		Particle ship = particles.get(4);
		return ship.getVx();
	}
	
	public double getShipVy() {
		Particle ship = particles.get(4);
		return ship.getVy();
	}
	
	public double getEarthVx() {
		Particle earth = particles.get(1);
		return earth.getVx();
	}
	
	public double getEarthVy() {
		Particle earth = particles.get(1);
		return earth.getVy();
	}
	
	public double getMarsVx() {
		Particle mars = particles.get(2);
		return mars.getVx();
	}
	
	public double getMarsVy() {
		Particle mars = particles.get(2);
		return mars.getVy();
	}
	
	public double getVenusVx() {
		Particle venus = particles.get(3);
		return venus.getVx();
	}
	
	public double getVenusVy() {
		Particle venus = particles.get(3);
		return venus.getVy();
	}
	
	public List<Particle> getParticles() {
		return particles;
	}
	
	public double getSystemEnergy()
	{
		double kineticEnergy = 0.0;
		double potentialEnergy = 0.0;
		for(Particle p1 : particles)
		{
			kineticEnergy += p1.getKineticEnergy();
			for(Particle p2 : particles)
			{
				if(p1.getId() != p2.getId())
					potentialEnergy += G * p1.getMass()*p2.getMass() / (Math.abs(p1.getCenterDistance(p2)));
			}
		}
		return kineticEnergy - 0.5*potentialEnergy;		
	}
}
