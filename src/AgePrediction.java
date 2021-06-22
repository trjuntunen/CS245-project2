import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class AgePrediction {

    private final ArrayList<Person> people;
    private final Configuration config;

    public AgePrediction(String configPath) {
        people = new ArrayList<>();
        this.config = new Configuration(configPath);
    }

    // this would be private right?
    void fillListWithData() {
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
                    Person person = new Person(values[3], values[1], values[0], values[2], values[4]);
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

    public static void main(String[] args) {
        /* Validate input args */
        if (args.length != 1) {
            System.out.println("Invalid input. Usage... [TODO enter usage] ");
            System.exit(1);
        }
        String configPath = args[0];
        AgePrediction agePredict = new AgePrediction(configPath);
        agePredict.fillListWithData();
    }

}
