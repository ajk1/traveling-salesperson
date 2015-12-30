package graphLocations;

/**
 * Directional edge that holds two vertex indices and the distance between them
 * @author Alex
 *
 */
public class Edge {
	int p; //parent vertex
	int c; //child vertex
	double distance;
	public Edge(int p, int c, double distance) {
		this.p = p;
		this.c = c;
		this.distance = distance;
	}
	public int getP() {
		return p;
	}
	public void setP(int p) {
		this.p = p;
	}
	public int getC() {
		return c;
	}
	public void setC(int c) {
		this.c = c;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String toString() {
		return "("+ p +", "+ c +"):\t"+ distance;
	}
}
