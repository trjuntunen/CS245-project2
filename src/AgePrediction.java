import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/*
 * TO-DO:
 * Validate each user inputs
 * implement own linkedlist
 * add support for linkedlist
 * deal with only reading .txt files?
 * do we have to deal with improperly formatted data?
 * do we have to validate that input path is valid format
 * ask about surrounding things with try catch blocks
 * ask about skipping file unable to read with catch that has nothing in it
 */
public class AgePrediction {

    private ArrayList<Person> people;
    private final Printer printer;
    private final Configuration config;

    public AgePrediction(String configPath) {
        people = new ArrayList<>();
        config = new Configuration(configPath);
        buildListOfPeople(); // Build the entire list of people from the data in the data files
        this.printer = new Printer();
    }

    public void start() {
        while (true) {
            /* Ask user all of the questions */
            HashMap<String, String> userInput = printer.printMenuAndGetUserInput();
            /* Find the most likely person based on the data and inputs */
            ArrayList<Person> mostLikelyPeople = findMostLikelyPeople(userInput);
            printer.printResult(mostLikelyPeople);
        }
    }

    private void buildListOfPeople() {
        /* Find all files in directory and read them all */
        try (Stream<Path> paths = Files.walk(Paths.get("data"))) {
            paths.filter(Files::isRegularFile)
                    .forEach(this::readDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readDataFile(Path file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile().getAbsolutePath()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                createAndAddPerson(values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAndAddPerson(String[] line) throws Exception {
        if (line.length == 5) { // 5 values in each line in data file
            /* Create a new Person with the values in each line [name, gender, state, year, nameCount] */
            try {
                Person person = new Person(line[3], line[1], line[0], Integer.parseInt(line[2]), Integer.parseInt(line[4]));
                people.add(person);
            } catch(Exception e) {
                // skip file that was unable to read
            }

        }
    }

    private ArrayList<Person> findMostLikelyPeople(Map<String, String> userInput) {
        ArrayList<Person> mostLikelyPeople = new ArrayList<>();
        try {
            ArrayList<Person> filteredPeople = new ArrayList<>();
            int maxNameCount = 0;
            for (int i = 0; i < people.size(); i++) {
                Person person = people.get(i);
                if (person.getName().equalsIgnoreCase(userInput.get("name"))
                        && person.getGender().equalsIgnoreCase(userInput.get("gender"))
                        && person.getState().equalsIgnoreCase(userInput.get("state"))) {
                    filteredPeople.add(person);
                    if (person.getNameCount() > maxNameCount) {
                        maxNameCount = person.getNameCount();
                    }
                }
            }
            for (int i = 0; i < filteredPeople.size(); i++) {
                Person person = filteredPeople.get(i);
                if (person.getNameCount() == maxNameCount) {
                    mostLikelyPeople.add(person);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return mostLikelyPeople;
    }

    public static void main(String[] args) {
        /* Validate input args */
        if (args.length != 1) {
            System.out.println("Invalid input. Usage... [TODO enter usage] ");
            System.exit(1);
        }
        String configPath = args[0];
        AgePrediction agePrediction = new AgePrediction(configPath);
        agePrediction.start();
    }

}
