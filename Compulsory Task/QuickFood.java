import java.io.File;
import java.util.List;
import java.util.Formatter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class QuickFood {
	public static void main(String[] args) {
		// Create scanner in main method to avoid errors, see Ref(4)
		Scanner sc = new Scanner(System.in);

		// Get a list of all the drivers in the file
		List < String > driverStringArr = fileLinesToStringList("driver-info.txt");

		// NOTE: the order number could be generated by some other method that
		// refreshed daily for example, so it is simply declared here.
		Integer orderNumber = 1234;

		// Create a new restaurant object from user input
		System.out.println("\n\n=== Enter restaurant details ===");
		Restaurant restaurantObject = askForRestaurantDetails(sc);

		// Create a new customer object from user input
		System.out.println("\n\n=== Enter customer details ===");
		Customer customerObject = askForCustomerDetails(sc, orderNumber, restaurantObject.getName());

		// Get only the drivers in the same location as the restaurant
		List < String > localDriverArr = getLocalisedDrivers(driverStringArr, restaurantObject.getLocation());

		// Check if there are no drivers near the restaurant
		if (localDriverArr.size() <= 0) {
			generateErrorInvoice("Sorry! Our drivers are not available in this location.");
		} else {
			// Sort drivers by load
			List < String > sortedLocalDriverArr = sortDriversByLoad(localDriverArr);

			// Get the localised driver with the lowest load
			String bestAvailableDriver = sortedLocalDriverArr.get(0);

			// Create FoodDriver object with info of the best driver
			FoodDriver foodDriverObject = new FoodDriver();

			//Splits the driver string into part to be stored in the object.
			// Drivers strings are in format 'Driver, Location, Load'
			foodDriverObject.setName(getStringSegment(bestAvailableDriver, 0));
			foodDriverObject.setLocation(getStringSegment(bestAvailableDriver, 1));
			foodDriverObject.setLoad(Integer.parseInt(getStringSegment(bestAvailableDriver, 2)));

			// One last check to see if the customer is in the same location as the driver
			// This could be changed if drivers are meant to travel between distant locations
			if (!(customerObject.getLocation()).equalsIgnoreCase(foodDriverObject.getLocation())) {
				generateErrorInvoice("Sorry! Our drivers are too far away from you to be able to deliver to your location.");
			} else {
				generateInvoice(customerObject, restaurantObject, foodDriverObject, orderNumber);
			}
		}

		// Close scanner
		sc.close();
	}

	// ==== FUNCTIONS ====

	/**
	 * Takes a list of restaurant meal items and prompts the user to choose from them.
	 * Asks for both the item and amount.
	 * @param restaurantItems List of food items in format (name, price).
	 * @param input The main scanner object.
	 * @return List of food items with amounts in format (name, price, amount).
	 */
	private static List < String > getMealItems(List < String > restaurantItems, Scanner input) {
		List < String > mealItemList = new ArrayList < > ();

		// Loop to prompt user for all meal items and their amounts
		boolean complete = false;
		while (!complete) {

			// Loop to pick a food item
			String chosenMealName = "INVALID"; // Make 'INVALID' default state
			do {

				// Print out all food items
				System.out.println("=== Select food item ===");
				for (int i = 0; i < restaurantItems.size(); i++) {
					String item = restaurantItems.get(i);
					System.out.println("ID: " + i + ") " + getFoodItemName(item) + ", Price: R" + getFoodItemPrice(item));
				}

				// Prompt user input for a meal ID
				System.out.println("\nInput meal ID: ");
				int idChoice = parseIntFancy(input.nextLine());

				// Check if ID is valid
				if (idChoice >= restaurantItems.size() || idChoice < 0) {
					System.out.println("ERROR: Invalid meal ID!");
				} else {
					chosenMealName = restaurantItems.get(idChoice);
					System.out.println("Chosen: " + chosenMealName);
					break;
				}
			} while (chosenMealName.equals("INVALID")); // As long as input is invalid keep asking

			// Loop to pick an amount of this item
			int count = 0;
			do {
				System.out.println("Enter how many of this item to order: ");
				count = parseIntFancy(input.nextLine());

				if (!(count > 0)) { // If count is not greater than zero
					System.out.println("Invalid amount!");
				}

			} while (!(count > 0));

			// Get the amount and add it to the previous input meal name
			String mealWithCount = (chosenMealName + ", " + count);
			mealItemList.add(mealWithCount);

			// Ask to add more meal items
			System.out.println("Add another item [y/n]: ");
			String choice = input.nextLine();

			// Anything other than yes will end the loop
			if (!choice.equalsIgnoreCase("y")) {
				complete = true;
				break;
			}
		}

		return mealItemList;
	}


	/**
	 * Shows a list of available restaurants and prompt user to pick one.
	 * When a valid restaurant is given it returns the full restaurant name (with correct casing).
	 * @param input The main scanner object.
	 * @return A full restaurant name with proper casing.
	 */
	private static String getValidRestaurantName(Scanner input) {
		// Manually declare the available restaurant files
		List < String > restaurantList = Arrays.asList("Pizza Palace", "New Restaurant");


		String chosenRestaurantName = "INVALID"; // Make 'INVALID' default state
		// Loop to keep asking user for input
		do {
			// Print out all restaurant names
			System.out.println("= Available restaurants = ");
			for (int i = 0; i < restaurantList.size(); i++) {
				String name = restaurantList.get(i);
				System.out.println("ID: " + i + ") " + name);
			}

			// Prompt user input for a meal ID
			System.out.println("\nInput restaurant ID: ");
			int idChoice = parseIntFancy(input.nextLine());

			// Check if ID is valid
			if (idChoice >= restaurantList.size() || idChoice < 0) {
				System.out.println("ERROR: Invalid restaurant ID!");
			} else {
				chosenRestaurantName = restaurantList.get(idChoice);
				System.out.println("Chosen: " + chosenRestaurantName);
				break;
			}

		} while (chosenRestaurantName.equals("INVALID")); // As long as input is invalid keep asking

		// Passed checks sucessfully
		return chosenRestaurantName;
	}


	/**
	 * Given a food item string, this method returns the name as a single string.
	 * @param foodString A string in the format 'name, price'.
	 * @return The food item name as a string.
	 */
	private static String getFoodItemName(String foodString) {
		// Split string into parts
		String[] strArray = foodString.split(", ", 3);
		// Return the first item (the name)
		return strArray[0];
	}

	/**
	 * Given a food item string, this method returns the price as a single string.
	 * @param foodString A string in the format 'name, price'.
	 * @return The food item price as a string.
	 */
	private static float getFoodItemPrice(String foodString) {
		// Split string into parts
		String[] strArray = foodString.split(", ", 3);

		// Return the second item (the price) after extracting only the float
		return extractFloat(strArray[1]);
	}

	/**
	 * Given a long food item string, this method returns the amount as a single string.
	 * @param foodString A string in the format 'name, price, amount'.
	 * @return The food item amount as a string.
	 */
	private static int getFoodItemAmount(String foodString) {
		// Split string into parts
		String[] strArray = foodString.split(", ", 3);

		// Return the third item (the amount) after extracting only the integer
		return Integer.parseInt(strArray[2]);
	}

	/**
	 * Prompts user to select a restaurant then returns it as a new 'Restaurant' object.
	 * @param input The scanner object.
	 * @return New Restaurant object with assigned values.
	 */
	private static Restaurant askForRestaurantDetails(Scanner input) {
		// Create a new restaurant object
		Restaurant restaurantObject = new Restaurant();

		// Prompt user input and then set the name
		String chosenRestaurant = getValidRestaurantName(input);
		restaurantObject.setName(chosenRestaurant);

		/* NOTE: this would has to be pre set for every restaurant.
		 * Ideally this would be stored with the restaurant file somehow
		 * (although right now that seems a bit outside the scope of this project)
		 */

		if (chosenRestaurant.equals("Pizza Palace")) {
			restaurantObject.setLocation("Cape Town");
			restaurantObject.setContactNum("0123456789");
		} else if (chosenRestaurant.equals("New Restaurant")) {
			restaurantObject.setLocation("Bloemfontein");
			restaurantObject.setContactNum("0123456789");
		} else {
			System.out.println("Invalid restaurant detected when creating object!");
		}

		// Return new object
		return restaurantObject;
	}

	// A very basic method to make sure the user entered something
	/**
	 * Takes a prompt message and a minimum length of the user response.
	 * Will return the user response only if it is long enough.
	 * @param input The main scanner object.
	 * @param prompt The message to prompt the user for input.
	 * @param minLength The minimum length of the users response.
	 * @return The users response only if long enough.
	 */
	private static String askUntilValidResponseLength(Scanner input, String prompt, int minLength) {
		String result = "";

		boolean complete = false;
		while (!complete) {
			// Prompt user for input
			System.out.println(prompt);
			// Get input as string
			String givenInput = input.nextLine();

			if (givenInput.length() < minLength) {
				System.out.println("Invalid input!");
			} else {
				result = givenInput; // Save the user input
				complete = true;
				break;
			}
		}

		return result;
	}
	/**
	 * Prompts user to manually input values for a customer then returns it as a new 'Customer' object.
	 * @param input The main scanner object.
	 * @param orderNum The current order number.
	 * @param chosenRestaurant The restaurant name (with proper casing).
	 * @return A new Customer object with assigned values.
	 */
	private static Customer askForCustomerDetails(Scanner input, int orderNum, String chosenRestaurant) {
		// Create a new customer object
		Customer customerObject = new Customer();
		customerObject.setOrderNum(orderNum);

		/* The minimum length is not a perfect solution. Ideally there would be a method to fully validate all entered data individually.
		 * Ex. methods to check if an address exists or an email is real
		 * So for now all length checks are arbitrary values based very roughly on the shortest possible input.
		 */

		// Ask user for input until it is valid and then save to customer object
		customerObject.setName(askUntilValidResponseLength(input, "Enter customer name: ", 2));

		customerObject.setContactNum(askUntilValidResponseLength(input, "Enter customer contact number: ", 7));

		customerObject.setEmail(askUntilValidResponseLength(input, "Enter customer email: ", 3));

		customerObject.setAddress(askUntilValidResponseLength(input, "Enter customer address: ", 3));

		customerObject.setLocation(askUntilValidResponseLength(input, "Enter customer location: ", 3));

		// This will store each meal item
		List < String > mealList = new ArrayList < > ();
		// This will store the amount of each meal item
		List < Integer > mealAmountList = new ArrayList < > ();

		String chosenRestaurantFile = chosenRestaurant + ".txt";
		List < String > restaurantItems = fileLinesToStringList(chosenRestaurantFile);
		List < String > chosenMealItems = getMealItems(restaurantItems, input);

		for (String item: chosenMealItems) {
			String joinedItem = getFoodItemName(item) + ", R" + getFoodItemPrice(item);
			mealList.add(joinedItem);

			int amount = getFoodItemAmount(item);
			mealAmountList.add(amount);
		}

		// Apply finalised lists to customer object
		customerObject.setMeals(mealList);
		customerObject.setMealOrderCount(mealAmountList);

		// Ask for last item
		// Length check not needed as this is optional
		System.out.println("Enter special customer request: ");
		String req = input.nextLine();
		if (req.equals("")) {
			req = "n/a";
		}
		customerObject.setSpecialRequest(req);

		// Might be unnecessary, but just to be safe
		customerObject.setTotalPrice(0.0f);

		// Return the new customer object
		return customerObject;
	}

	/**
	 * Generates an invoice text file with only the given string as its contents.
	 * @param message
	 */
	private static void generateErrorInvoice(String message) {
		// Create a list (to work with the existing file writing system)
		String[] errorStringArray = {
			message
		};
		List < String > errorList = Arrays.asList(errorStringArray);

		// Generate the invoice file with only the error message
		writeFile("invoice.txt", errorList);
	}

	/* NOTE: This function's formatting is just to make it logical to edit and to avoid
	 * any potential errors with adding the info to the list as a single long string.
	 * (As would be the case with something like backquote formatting)
	 */
	/**
	 * Creates the necessary strings for the invoice and generates an invoice text file.
	 * @param customer The customer object.
	 * @param restaurant The restaurant object.
	 * @param foodDriver The driver object.
	 * @param orderNum The order number.
	 */
	private static void generateInvoice(Customer customer, Restaurant restaurant, FoodDriver foodDriver, Integer orderNum) {
		// This will store each line of the text file
		List < String > textStringList = new ArrayList < > ();

		// DO STUFF
		textStringList.add("Order number " + orderNum);
		textStringList.add("Customer: " + customer.getName());
		textStringList.add("Email: " + customer.getEmail());
		textStringList.add("Phone number: " + customer.getContactNum());
		textStringList.add("Location: " + customer.getLocation());

		textStringList.add("\nYou have ordered the following from " + restaurant.getName() + " in " + restaurant.getLocation() + ":\n");

		//
		int length = customer.getNumberOfMealsOrdered();
		for (int i = 0; i < length; i++) {
			textStringList.add(customer.getMealOrderCount().get(i) + " x " + customer.getMeals().get(i));
		}

		textStringList.add("\nSpecial instructions: \n" + customer.getSpecialRequest());

		// Calculate the total cost
		float totalOrderCost = 0;
		int mealArrSize = customer.getMeals().size();

		// For every meal
		for (int i = 0; i < mealArrSize; i++) {
			// Extract the price from the meal item string, multiply it by the order count
			// and add it to the total
			totalOrderCost += extractFloat(customer.getMeals().get(i)) * customer.getMealOrderCount().get(i);
		}
		textStringList.add("\nTotal: R" + totalOrderCost);

		textStringList.add("\n" + foodDriver.getName() + " is nearest to the restaurant so they will be delivering your");
		textStringList.add("order to you at:\n");
		textStringList.add(customer.getAddress() + ",");
		textStringList.add(customer.getLocation());

		textStringList.add("\nIf you need to contact the restaurant, their number is " + restaurant.getContactNum() + ".");

		// Generate the actual text file
		writeFile("invoice.txt", textStringList);
	}

	/**
	 * Takes a driver string list and sorts it by load in ascending order.
	 * @param driverList The initial list of drivers.
	 * @return List of drivers sorted by load (ascending).
	 */
	private static List < String > sortDriversByLoad(List < String > driverList) {
		// Make a copy of the list
		List < String > newStringList = driverList;

		/* Removes all non-numeric characters from each string
		 * then sorts the list items numerically (ascending).
		 * See Reference(2) for source.
		 */
		Collections.sort(newStringList, new Comparator < String > () {
			public int compare(String o1, String o2) {
				return extractInt(o1) - extractInt(o2);
			}

			// Used to get only integer values in a string
			int extractInt(String s) {
				String num = s.replaceAll("\\D", "");

				// return 0 if no digits found
				return num.isEmpty() ? 0 : Integer.parseInt(num);
			}
		});

		// Return the list of drivers sorted by load
		return newStringList;
	}

	/**
	 * Takes a driver string list and a location.
	 * Filters drivers to leave only those who match the location. Returns these drivers as a new list.
	 * @param driverList The initial list of drivers.
	 * @param location The location to filter drivers by.
	 * @return A filtered list of all drivers who match the location.
	 */
	private static List < String > getLocalisedDrivers(List < String > driverList, String location) {
		List < String > newStringList = new ArrayList < > ();

		for (String driver: driverList) {
			// If this driver is in the specified location
			if (driver.toLowerCase().contains(location.toLowerCase())) {
				// Add them to the temp list
				newStringList.add(driver);
			}
		}

		// Return the list of only drivers with the matching location
		return newStringList;
	}

	// Overload method to get value with default of 5 splits.
	private static String getStringSegment(String str, int segment) {
		return getStringSegment(str, segment, 5);
	}
	/**
	 * Takes a string divided by commas with a space, an array length and which segment to return(starting at index 0).
	 * @param str The string to be split.
	 * @param arrayLength Maximum times the split function will be applied.
	 * @param segment Which segment to return starting at 0 for the first item.
	 * @return A substring at the segment position.
	 */
	private static String getStringSegment(String str, int segment, int arrayLength) {
		// Split string into parts
		String[] strArray = str.split(", ", arrayLength);
		// Return the second item (the location)
		return strArray[segment];
	}

	/**
	 * Reads file at location, converts each line into a string and returns all the strings in an list.
	 * @param location The filename/location to be opened.
	 * @param showLog Whether to print completion status to console. [OPTIONAL]
	 * @return List containing file lines as strings.
	 */
	private static List < String > fileLinesToStringList(String location) {
		return fileLinesToStringList(location, false);
	}
	/**
	 * Reads file at location, converts each line into a string and returns all the strings in an list.
	 * @param location The filename/location to be opened.
	 * @param showLog Whether to print completion status to console.[OPTIONAL]
	 * @return List containing file lines as strings.
	 */
	private static List < String > fileLinesToStringList(String location, boolean showLog) {
		List < String > stringList = new ArrayList < > ();
		try {
			// Open the specified file
			File originalFile = new File(location);
			// Declare scanner
			Scanner sc = new Scanner(originalFile);

			// Loop through every line
			while (sc.hasNextLine()) {
				// Add it to the list as a string
				stringList.add(sc.nextLine());
			}
			sc.close();

			if (showLog) {
				System.out.println("Read file: '" + location + "'!");
			}
		} catch (Exception e) {
			System.out.println("Error converting file contents to list: " + e);
		}

		// Return the converted string list
		return stringList;
	}

	/**
	 * Takes an list of strings and encrypts them before pasting them onto
	 * each line of a new file.
	 * @param outputLocation The filename/location to write to.
	 * @param stringList The list of strings to be encrypted.
	 */
	private static void writeFile(String outputLocation, List < String > stringList) {
		try {
			// Declare formatter
			Formatter f = new Formatter(outputLocation);

			// Loop through every item in the list
			for (int i = 0; i < stringList.size(); i++) {
				// Make sure it is a string
				String nextLine = stringList.get(i);

				// Apply it the the new file
				f.format("%s", nextLine + "\r\n");
			}
			f.close(); // Prevent leaks
			System.out.println("Generated file: '" + outputLocation + "'!");
		} catch (Exception e) {
			System.out.println("Error writing file: " + e);
		}
	}

	/**
	 * Used to get only float values in a string.
	 * Takes a string containing a number. Returns only the number or returns 0 if none is found.
	 * @param s The initial string.
	 * @return A float number(if one exists in the string).
	 */
	private static float extractFloat(String s) {
		// Use regex to remove everything except the float values
		String num = s.replaceAll("[^[0-9]*\\.?[0-9]*]", "");

		if (num.isEmpty()) {
			// return 0 if no digits found
			return 0.0f;
		} else {
			return Float.parseFloat(num);
		}
	}

	/**
	 * Takes a string and returns the integer within, returning -1 if no integer is found.
	 * @param str The string to check.
	 * @return Integer value from within the string or -1 if there is none.
	 */
	private static int parseIntFancy(String str) {
		String num = str.replaceAll("\\D", "");

		// return -1 if no digits found
		return num.isEmpty() ? -1 : Integer.parseInt(num);
	}

}

/* REFERENCES
1. https://www.geeksforgeeks.org/split-string-java-examples/
2. Sorting method from comment by Bohemian: https://stackoverflow.com/questions/13973503/sorting-strings-that-contains-number-in-java
3. Regex to get only floats, comment by Mustofa Rizwan: https://stackoverflow.com/questions/43084537/what-is-the-regex-for-decimal-numbers-in-java
4. Scanner errors, comment by Yogendra Singh: https://stackoverflow.com/questions/13042008/java-util-nosuchelementexception-scanner-reading-user-input

5. Getting filenames in a folder: https://stackoverflow.com/questions/5694385/getting-the-filenames-of-all-files-in-a-folder
6. https://stackoverflow.com/questions/30708036/delete-the-last-two-characters-of-the-string

7. https://codebeautify.org/javaviewer
*/