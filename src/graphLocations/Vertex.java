package graphLocations;

public class Vertex {
	double x, y;
	double lat, lon;
	String address;
	String offense;
	
	/**
	 * parses a line from the Crime data to get x/y coords and location
	 * @param line
	 */
	public Vertex(String line) {
		String[] data = line.split(",");
		x = Double.parseDouble(data[0]);
		y = Double.parseDouble(data[1]);
		address = data[3];
		offense = data[4];
		lat = Double.parseDouble(data[7]);
		lon = Double.parseDouble(data[8]);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOffense() {
		return offense;
	}

	public void setOffense(String offense) {
		this.offense = offense;
	}
}
