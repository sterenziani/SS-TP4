package back;
public class SpaceReport {
	
	private double departureTime;
	private double shipVelocity;
	private double minDistance;
	private double time;
	
	public SpaceReport(double departureTime, double shipVelocity, double minDistance, double minTime)
	{
		this.departureTime = departureTime;
		this.shipVelocity = shipVelocity;
		this.minDistance = minDistance;
		this.time = minTime;
	}

	public double getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(double departureTime) {
		this.departureTime = departureTime;
	}

	public double getShipVelocity() {
		return shipVelocity;
	}

	public void setShipVelocity(double shipVelocity) {
		this.shipVelocity = shipVelocity;
	}

	public double getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(double minDistance) {
		this.minDistance = minDistance;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}
}
