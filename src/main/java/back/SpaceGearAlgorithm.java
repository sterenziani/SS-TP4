package back;
import java.util.List;

public class SpaceGearAlgorithm
{
	private static final double G = 6.693*Math.pow(10, -20); // for km³, it's 10^-20 instead of 10^-11
	private static final int X = 0;
	private static final int Y = 1;
	
	private List<Particle> particles;
	private double deltaT;
	private double alpha[] = {3.0/20.0, 251.0/360.0, 1.0, 11.0/18.0, 1.0/6.0, 1.0/60.0}; // Forces don't depend on velocities
	private double r[][][];
	
	public SpaceGearAlgorithm(List<Particle> particles, double deltaT, SpaceGearAlgorithm oldGearSystem)
	{
		this.particles = particles;
		this.deltaT = deltaT;
		r = new double[particles.size()][2][6];

        if(oldGearSystem == null)
        {
            for(Particle p : particles)
            {
                int index = p.getId() - 1;
                r[index][X][0] = p.getX();
                r[index][X][1] = p.getVx();
                r[index][Y][0] = p.getY();
                r[index][Y][1] = p.getVy();
                for(int i = 3; i < 6; i++){
                    r[index][X][i] = 0;
                    r[index][Y][i] = 0;
                }
            }
            for(Particle p : particles)
            {
            	int index = p.getId() - 1;
                r[index][X][2] = getAcceleration(r, index, X);
                r[index][Y][2] = getAcceleration(r, index, Y);
            }
        }
        else
        {
            double [][][] old_r = oldGearSystem.getR();
            for(Particle p : particles)
            {
                int index = p.getId() - 1;
                if(index != particles.size()-1)
                {
                    for(int i = 0; i < 6; i++){
                        r[index][X][i] = old_r[index][X][i];
                        r[index][Y][i] = old_r[index][Y][i];
                    }
                }
                else
                {
                    r[index][X][0] = p.getX();
                    r[index][Y][0] = p.getY();
                    r[index][X][1] = p.getVx();
                    r[index][Y][1] = p.getVy();
                    r[index][X][2] = getAcceleration(r, index, X);
                    r[index][Y][2] = getAcceleration(r, index, Y);

                    for(int i = 3; i<6; i++){
                        r[index][X][i] = 0;
                        r[index][Y][i] = 0;
                    }
                }
            }
        }
	}
	
    public double[][][] getR() {
        return r;
    }
    
    private double getAcceleration(double[][][] r, int targetIndex, int axis)
    {
        double a = 0;
        for (Particle p : particles)
        {
            int index = p.getId() -1;
            if (index != targetIndex)
            {
                double dx = r[index][X][0] - r[targetIndex][X][0];
                double dy = r[index][Y][0] - r[targetIndex][Y][0];
                double dist = Math.sqrt(dx*dx + dy*dy);
                if(axis == X)
                	a += p.getMass() * dx / Math.pow(dist, 3);
                else
                	a += p.getMass() * dy / Math.pow(dist, 3);
            }
        }
        return G * a;
    }

	public void updateParticles()
	{
		double nextRp[][][] = new double[particles.size()][2][6];
		for(Particle p : particles)
		{
            int particleIndex = p.getId() - 1;
            for(int axis=X; axis <= Y; axis++)
            {
        		// Step 1: Predict
        		for(int i=0; i < 6; i++)
        			nextRp[particleIndex][axis][i] = predict(particleIndex, axis, i);
            }
		}
		for(Particle p : particles)
		{
            int particleIndex = p.getId() - 1;
            for(int axis=X; axis <= Y; axis++)
            {        		
        		// Step 2: Evaluate
                double nextA;
                nextA = getAcceleration(nextRp, particleIndex, axis);
                double nextAp = nextRp[particleIndex][axis][2];
                double deltaR2 = (nextA - nextAp) * Math.pow(deltaT, 2) * 0.5;
                
                // Step 3: Correct
                for(int q = 0; q < 6; q++)
                    r[particleIndex][axis][q] = nextRp[particleIndex][axis][q] + (alpha[q]*deltaR2*factorial(q)) / Math.pow(deltaT, q);
            }
            p.setX(r[particleIndex][X][0]);
            p.setY(r[particleIndex][Y][0]);
            p.setVx(r[particleIndex][X][1]);
            p.setVy(r[particleIndex][Y][1]);
		}
	}
	
	private double predict(int particle, int axis, int index)
	{
		double value = r[particle][axis][index];
		for(int i=1; index+i < 6; i++)
			value += r[particle][axis][index+i] * Math.pow(deltaT, i)/factorial(i);
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
