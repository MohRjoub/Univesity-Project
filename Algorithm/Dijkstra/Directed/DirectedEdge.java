package Directed;

// Directed Edge class 
public class DirectedEdge implements Comparable<DirectedEdge> {
	private int v; // edge source
	private int w; // edge destination
	private double cost;
	private double time;
	private double destance;
	private String filter;
	public DirectedEdge(int v, int w, double cost, double time, double destance, String filter) {
		this.v = v;
		this.w = w;
		this.cost = cost;
		this.time = time;
		this.destance = destance;
		this.filter = filter;
	}


	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double getDestance() {
		return destance;
	}

	public double getWeight(String filter) {
		if (filter.equalsIgnoreCase("Cost"))
			return this.cost;
		if (filter.equalsIgnoreCase("Time"))
			return this.time;
		return destance;
	}

	public void setDestance(double destance) {
		this.destance = destance;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public int source() {
		return v;
	}

	public int destination() {
		return w;
	}

	public String toString() {
		return String.format("%d->%d cost %d, time %d, distance %0.2f", v, w, cost, time, destance);
	}

	@Override
	public int compareTo(DirectedEdge o) {
		if (this.filter.equalsIgnoreCase("Cost"))
			return (int) (this.cost - o.cost);
		if (this.filter.equalsIgnoreCase("Time"))
			return (int) (this.time - o.time);
		return (int) (this.destance - o.destance);
	}
}
