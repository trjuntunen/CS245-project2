import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Stream;


/*
 * TO-DO:
 * Show appropriate messages for when no one shows up with that name.
 * Validate each user inputs
 * implement own arraylist
 * implement own linkedlist
 * add support for linkedlist
 * deal with only reading .txt files?
 * do we have to deal with improperly formatted data?
 * do we have to validate that input path is valid format
 */
public class AgePrediction {

    private final ArrayList<Person> people;
    private String listType;
    private String directory;

    public AgePrediction(String configPath) {
        people = new ArrayList<>();
        setConfig(configPath);
    }

    public void start() {
        fillListWithData();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Name of the person (or EXIT to quit): ");
            String name = scanner.nextLine();
            System.out.println("Gender (M/F): ");
            String gender = scanner.nextLine();
            System.out.println("State of birth (two-letter state code): ");
            String state = scanner.nextLine();
            ArrayList<Person> filteredPeople = filterPeople(name, gender, state);
            Person matchingPerson = findMostPopularYearForName(filteredPeople);
            printResult(matchingPerson);
        }
    }

    private void fillListWithData() {
        // loop through each file in given directory in config file
        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
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
                if (values.length == 5) { // 5 values in each line in data file
                    String name = values[3];
                    String gender = values[1];
                    String state = values[0];
                    int year = Integer.parseInt(values[2]);
                    int nameCount = Integer.parseInt(values[4]);
                    Person person = new Person(name, gender, state, year, nameCount);
                    people.add(person);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Person> filterPeople(String name, String gender, String state) {
        ArrayList<Person> filteredPeople = new ArrayList<>();
        for (Person person : people) {
            if (person.getName().equalsIgnoreCase(name) && person.getGender().equalsIgnoreCase(gender) && person.getState().equalsIgnoreCase(state)) {
                filteredPeople.add(person);
            }
        }
        return filteredPeople;
    }

    private Person findMostPopularYearForName(ArrayList<Person> filteredPeople) {
        Person personWithHighestCount = null;
        for (Person person : filteredPeople) {
            if (personWithHighestCount == null) {
                personWithHighestCount = person;
            } else {
                if (person.getNameCount() > personWithHighestCount.getNameCount()) {
                    personWithHighestCount = person;
                }
            }
        }
        return personWithHighestCount;
    }

    private void printResult(Person person) {
        if (person != null) {
            String yearsPluralOrSingular = (person.getAge() == 1) ? "year" : "years";
            System.out.println(person.getName() + " born in " + person.getState() + " is most likely around " + person.getAge() + " " + yearsPluralOrSingular + " old.");
        } else {
            System.out.println("No person found with those inputs.");
        }
    }

    private void setConfig(String configPath) {
        try (InputStream input = new FileInputStream(configPath)) {
            Properties properties = new Properties();
            properties.load(input);
            listType = properties.getProperty("ListType");
            directory = properties.getProperty("Directory");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
