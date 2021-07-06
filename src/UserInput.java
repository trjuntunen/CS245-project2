import java.util.Scanner;

public class UserInput {
	
	private String name;
	private String gender;
	private String state;
	
	public void askUserQuestions() {
		Scanner scanner = new Scanner(System.in);

		String namePrompt = "Name of the person (or EXIT to quit): ";
		this.name = getUserInput(namePrompt, scanner);

		String genderPrompt =  "Gender M/F (or EXIT to quit): ";
		this.gender = getUserInput(genderPrompt, scanner);
		
		String statePrompt = "State of birth (two-letter state code) (or EXIT to quit): ";
		this.state = getUserInput(statePrompt, scanner);
	}
	
	private String getUserInput(String prompt, Scanner scanner) {
		System.out.println(prompt);
		String input = scanner.nextLine();
		while(input.isBlank()) {
			System.out.println(prompt);
			input = scanner.nextLine();
		}
		if (input.trim().equalsIgnoreCase("exit")) {
			System.exit(0); 
		}
		return input.trim();
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
