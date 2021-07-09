import java.util.Scanner;

public class UserInput {

	private String name;
	private String gender;
	private String state;
	private final Scanner scanner;
	private final String[] stateCodes = { "AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "IA", "ID",
			"IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ",
			"NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV",
			"WY" };

	public UserInput() {
		this.scanner = new Scanner(System.in);
	}

	public void askUserQuestions() {
		getNameFromUser();

		getGenderFromUser();

		getStateFromUser();
	}

	private void getNameFromUser() {
		String namePrompt = "Name of the person (or EXIT to quit): ";
		System.out.println(namePrompt);
		String name = scanner.nextLine();

		// Continuously loop until name is not blank
		while (name.isBlank()) {
			System.out.println(namePrompt);
			name = scanner.nextLine();
		}
		this.name = name.trim();

		// typing "exit" exits the program
		if (this.name.equalsIgnoreCase("exit")) {
			System.exit(0);
		}
	}

	private void getGenderFromUser() {
		String genderPrompt = "Gender M/F: ";
		System.out.println(genderPrompt);
		String gender = scanner.nextLine();
		while (gender.isBlank() || !isValidGender(gender)) {
			System.out.println("Invalid gender. Try again.\n" + genderPrompt);
			gender = scanner.nextLine();
		}
		this.gender = gender.trim();
	}

	private void getStateFromUser() {
		String statePrompt = "State of birth (two-letter state code): ";
		System.out.println(statePrompt);
		String state = scanner.nextLine();
		while (state.isBlank() || !isValidStateCode(state)) {
			System.out.println("Invalid state code. Try again.\n" + statePrompt);
			state = scanner.nextLine();
		}
		this.state = state.trim();
	}

	private boolean isValidGender(String gender) {
		return gender.equalsIgnoreCase("m") || gender.equalsIgnoreCase("f");
	}

	private boolean isValidStateCode(String stateCode) {
		for (String code : stateCodes) {
			if (code.equalsIgnoreCase(stateCode)) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public String getGender() {
		return gender;
	}

	public String getState() {
		return state;
	}

}
