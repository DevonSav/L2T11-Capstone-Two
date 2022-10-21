public class FoodDriver {
	// === ATTRIBUTES ===
	private String name;
	private String location;
	private int load;

	// === GETTERS and SETTERS ===
	// Name
	public String getName() {
		return name;
	}
	public void setName(String newName) {
		this.name = newName;
	}

	// Location
	public String getLocation() {
		return location;
	}
	public void setLocation(String newLocation) {
		this.location = newLocation;
	}

	// Load
	public Integer getLoad() {
		return load;
	}
	public void setLoad(Integer newLoad) {
		this.load = newLoad;
	}

	// === METHODS ===
	// Used to declare a driver object without initial values
	public FoodDriver() {}

	public FoodDriver(String name, String location, int load) {
		this.name = name;
		this.location = location;
		this.load = load;
	}

	/**
	 * Returns a formatted string with all the driver details.
	 */
	public String toString() {
		String output = "Name: " + name;
		output += "\nLocation: " + location;
		output += "\nLoad: " + load;

		return output;
	}
}
