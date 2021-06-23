import java.util.HashMap;
import java.util.Scanner;

public class Printer {

    private final Scanner scanner;

    public Printer() {
        this.scanner = new Scanner(System.in);
    }

    public HashMap<String, String> printMenuAndGetUserInput() {
        System.out.println("Name of the person (or EXIT to quit): ");
        String name = scanner.nextLine();
        if (name.equalsIgnoreCase("exit")) {
            System.exit(0); // Typing exit will exit out of the program
        }
        System.out.println("Gender (M/F): ");
        String gender = scanner.nextLine();
        System.out.println("State of birth (two-letter state code): ");
        String state = scanner.nextLine();

        // Put all of the user inputs into a map to return
        HashMap<String, String> results = new HashMap<>();
        results.put("name", name);
        results.put("gender", gender);
        results.put("state", state);
        return results;
    }

    private void printResult(Person person) {
        System.out.println(person.getName() + " born in " + person.getState() + " is most likely around " + person.getAge() + " years" + " old.");
    }

    public void printResult(ArrayList<Person> people) {
        try {
            if(people.size() <= 0) {
                System.out.println("No person found with those inputs.");
            } else if(people.size() == 1) {
                printResult(people.get(0));
            } else {
                for (int i = 0; i < people.size(); i++) {
                    Person person = people.get(i);
                    printResult(person);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
