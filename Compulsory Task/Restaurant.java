public class Restaurant {
	// === ATTRIBUTES ===
	private String name;
	private String location;
	private String contactNumber;

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

	// Contact number
	public String getContactNum() {
		return contactNumber;
	}
	public void setContactNum(String newContactNumber) {
		this.contactNumber = newContactNumber;
	}

	// === METHODS ===
	// Used to declare a customer object without initial values
	public Restaurant() {}

	public Restaurant(String name, String location, String contactNumber) {
		this.name = name;
		this.location = location;
		this.contactNumber = contactNumber;
	}

	/**
	 * Returns a formatted string with all the restaurant details.
	 */
	public String toString() {
		String output = "Name: " + name;
		output += "\nLocation: " + location;
		output += "\nContact number: " + contactNumber;

		return output;
	}
}
