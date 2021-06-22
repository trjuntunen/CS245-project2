import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

public class AgePrediction {

    private final ArrayList<Person> people;
    private final Configuration config;

    public AgePrediction(String configPath) {
        people = new ArrayList<>();
        this.config = new Configuration(configPath);
    }

    // this would be private right?
    private void fillListWithData() {
        // loop through each file in given directory in config file
        try (Stream<Path> paths = Files.walk(Paths.get(config.getDirectory()))) {
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
                if (values.length == 5) {
//                    for(String v:values) {
//                        System.out.print(v + " ");
//                    }
//                    System.out.println();
                    Person person = new Person(values[3], values[1], values[0], Integer.parseInt(values[2]),  Integer.parseInt(values[4]));
                    people.add(person);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Person get(int index) {
        return people.get(index);
    }

    public void start() {
        fillListWithData();
        while(true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Name of the person (or EXIT to quit): ");
            String name = scanner.nextLine();
            System.out.println("Gender (M/F): ");
            String gender = scanner.nextLine();
            System.out.println("State of birth (two-letter state code): ");
            String state = scanner.nextLine();

            ArrayList<Person> queriedPeople = new ArrayList<>();
            for(Person p: people) {
                if(p.getState().equals(state) && p.getName().equals(name) && p.getGender().equals(gender)) {
                    queriedPeople.add(p);
                }
            }
            Person maxPerson = new Person();
//            for(Person p:queriedPeople) {
//                System.out.println(p);
//            }
            for(Person p: queriedPeople) {
                if(p.getNameCount() > maxPerson.getNameCount()) {
                    maxPerson = p;
                }
            }
            System.out.println(maxPerson.getName() + " born in " + maxPerson.getState() + " is most likely around " + maxPerson.getAge() + " years old.");
        }
    }

    public static void main(String[] args) {
        /* Validate input args */
        if (args.length != 1) {
            System.out.println("Invalid input. Usage... [TODO enter usage] ");
            System.exit(1);
        }
        String configPath = args[0];
        AgePrediction agePredict = new AgePrediction(configPath);
        agePredict.start();
    }

}
