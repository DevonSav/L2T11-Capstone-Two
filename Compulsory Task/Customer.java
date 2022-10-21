import java.util.List;

public class Customer {
	// === ATTRIBUTES ===
	private int orderNum;
	private String name;
	private String contactNum;
	private String email;
	private String address;
	private String location;

	private List < String > meals;
	private List < Integer > mealOrderCount;

	private String specialRequests;
	private float totalPrice;

	// === GETTERS and SETTERS ===
	// Load
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer newOrderNum) {
		this.orderNum = newOrderNum;
	}

	// Name
	public String getName() {
		return name;
	}
	public void setName(String newName) {
		this.name = newName;
	}

	// Contact number
	public String getContactNum() {
		return contactNum;
	}
	public void setContactNum(String newContactNum) {
		this.contactNum = newContactNum;
	}

	// Email
	public String getEmail() {
		return email;
	}
	public void setEmail(String newEmail) {
		this.email = newEmail;
	}

	// Address
	public String getAddress() {
		return address;
	}
	public void setAddress(String newAddress) {
		this.address = newAddress;
	}

	// Location
	public String getLocation() {
		return location;
	}
	public void setLocation(String newLocation) {
		this.location = newLocation;
	}

	// meals list
	public List < String > getMeals() {
		return meals;
	}
	public void setMeals(List < String > newMeals) {
		this.meals = newMeals;
	}

	// meals order count list
	public List < Integer > getMealOrderCount() {
		return mealOrderCount;
	}
	public void setMealOrderCount(List < Integer > newMealOrderCount) {
		this.mealOrderCount = newMealOrderCount;
	}

	/**
	 * Returns the number of unique meals the customer has ordered.
	 * @return The number of unique meals the customer has ordered.
	 */
	public int getNumberOfMealsOrdered() {
		return mealOrderCount.size();
	}

	// Special request
	public String getSpecialRequest() {
		return specialRequests;
	}
	public void setSpecialRequest(String newSpecialRequest) {
		this.specialRequests = newSpecialRequest;
	}

	// Total price
	public Float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Float newTotalPrice) {
		this.totalPrice = newTotalPrice;
	}

	// === METHODS ===
	// Used to declare a customer object without initial values
	public Customer() {}

	public Customer(int orderNum,
		String name,
		String contactNum,
		String email,
		String address,
		String location,

		List < String > meals,
		List < Integer > mealOrderCount,

		String specialRequests,
		float totalPrice) {

		this.orderNum = orderNum;
		this.name = name;
		this.contactNum = contactNum;
		this.email = email;
		this.address = address;
		this.location = location;
		this.meals = meals;
		this.mealOrderCount = mealOrderCount;
		this.specialRequests = specialRequests;
		this.totalPrice = totalPrice;
	}

	/**
	 * Returns a formatted string with all the customer details.
	 */
	public String toString() {
		String output = "Order number: " + orderNum;
		output += "\nName: " + name;
		output += "\nContact number: " + contactNum;
		output += "\nEmail: " + email;
		output += "\nAddress: " + address;
		output += "\nLocation: " + location;

		output += "\nMeals: " + meals;
		output += "\nMeal order counts: " + mealOrderCount;

		output += "\nSpecial requests: " + specialRequests;
		output += "\nTotal price: " + totalPrice;

		return output;
	}
}
