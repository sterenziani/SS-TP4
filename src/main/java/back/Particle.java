package back;

public class Particle {
    private int id;
    private double x;
    private double y;
    private double radius;
    private double mass;
    private double vx;
    private double vy;

    public Particle(int id, double x, double y, double vx, double vy, double mass, double radius)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.mass = mass;
        this.vx = vx;
        this.vy = vy;
    }

    public int getId() {
        return id;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }
    
    public double getV() {
    	double v2 = Math.pow(vx,2) + Math.pow(vy, 2);
    	return Math.sqrt(v2);
    }
    
    public double getRadius() {
        return radius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getMass() {
        return mass;
    }

    public double getCenterDistance(Particle p) {
    	double diffX = p.getX() - x;
    	double diffY = p.getY() - y;
    	double diff2 = Math.pow(diffX, 2) + Math.pow(diffY, 2);
    	return Math.sqrt(diff2);
    }
    
    public double getEdgeDistance(Particle p) {
        return getCenterDistance(p) - p.getRadius() - radius;
    }
    
    public static double getAcceleration(double position, double velocity, double mass, double k, double gamma) {
    	// Diapo 36: 	m*a = -k*r -gamma*v
        return -(k*position + gamma*velocity) / mass;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setVx(double vx){
        this.vx = vx;
    }

    public void setVy(double vy){
        this.vy = vy;
    }
    
    public String toString()
    {
    	return "x: " +x +"\ty:" +y +"\tvx: " +vx +"\tvy: " +vy +"\tr: " +radius +"\tm:" +mass;
    }
}
