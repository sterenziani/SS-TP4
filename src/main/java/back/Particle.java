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
    
}
