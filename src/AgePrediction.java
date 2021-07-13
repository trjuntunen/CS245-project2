import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/*
 * Program that predicts the most likely age
 * given inputs Name, Gender, and state
 *
 * Author: Teddy Juntunen
 */
public class AgePrediction {

    private List<NameRecord> records;
    private final Configuration config;

    public AgePrediction(Path configFile) {
        config = new Configuration(configFile);

        // Instantiate different type of list for 'records' depending on configuration file
        if (config.isUsingLinkedList()) {
            records = new LinkedList<>();
        } else {
            records = new ArrayList<>();
        }
    }

    /**
     * Build the list of records and then start the AgePrediction loop to predict
     * ages based on name, gender and state.
     */
    public void start() {
        // Build the database of records from the data files
        buildListOfRecords();

        while (true) {
            // Ask user all of the required questions
            UserInput input = new UserInput();
            input.askUserQuestions();

            // Find the most likely person based on the user inputs
            List<NameRecord> results = findResults(input);

            printResults(results);
        }
    }

    /**
     * Read all data files within the directory in the given config file
     */
    private void buildListOfRecords() {
        // Walk through all directories and read all files within
        try (Stream<Path> paths = Files.walk(config.getDirectory())) {
            paths.filter(Files::isRegularFile).forEach(this::readDataFile);
        } catch (IOException e) {
            System.out.println("Error: Unable to access the data files.");
            System.exit(1);
        }
    }

    /**
     * Reads an individual data file and adds it to our master list of records
     */
    private void readDataFile(Path file) {
        if (file.toString().toLowerCase().endsWith(".txt")) {
            try {
                FileReader fr = new FileReader(file.toAbsolutePath().toString());
                BufferedReader br = new BufferedReader(fr);
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    // Each line: [name, gender, state, year, nameCount]
                    if (values.length == 5) {
                        String name = values[3];
                        String gender = values[1];
                        String state = values[0];
                        int year = Integer.parseInt(values[2]);
                        int nameCount = Integer.parseInt(values[4]);
                        // Create new record and add to list
                        NameRecord record = new NameRecord(name, gender, state, year, nameCount);
                        // Add at beginning for LinkedList and end for ArrayList
                        if (config.isUsingLinkedList()) {
                            records.add(0, record);
                        } else {
                            records.add(record);
                        }
                    }
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Finds the result of the most likely age given user inputs using
     * Linked Lists
     */
    private List<NameRecord> findResultsLinkedList(UserInput input) {
        List<NameRecord> results = new LinkedList<>();
        LinkedList<NameRecord> recordsMatchingInput = new LinkedList<>();
        int maxNameCount = 0;
        // Cast records to LL and get head Node
        Node<NameRecord> tempHead = ((LinkedList<NameRecord>) records).getHead();
        // Loop through all records to find records that match all user inputs
        for (int i = 0; i < records.size(); i++) {
            if (recordMatchesInput(tempHead.data, input)) {
                try {
                    recordsMatchingInput.add(0, tempHead.data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Check for name with highest count
                if (tempHead.data.getNameCount() > maxNameCount) {
                    maxNameCount = tempHead.data.getNameCount();
                }
            }
            tempHead = tempHead.next;
        }
        // Loop through records that match user input to see which ones match maxNameCount
        Node<NameRecord> head = recordsMatchingInput.getHead();
        for (int i = 0; i < recordsMatchingInput.size(); i++) {
            if (head.data.getNameCount() == maxNameCount) {
                results.add(head.data);
            }
            head = head.next;
        }
        return results;
    }

    /**
     * Finds the result of the most likely age given user inputs using
     * Arraylists
     */
    private List<NameRecord> findResultsArrayList(UserInput input) {
        List<NameRecord> results = new LinkedList<>();
        ArrayList<NameRecord> recordsMatchingInput = new ArrayList<>();
        int maxNameCount = 0;
        try {
            for (int i = 0; i < records.size(); i++) {
                NameRecord record = records.get(i);
                if (recordMatchesInput(record, input)) {
                    recordsMatchingInput.add(record);
                    // Check for name with highest count
                    if (record.getNameCount() > maxNameCount) {
                        maxNameCount = record.getNameCount();
                    }
                }
            }
            for (int i = 0; i < recordsMatchingInput.size(); i++) {
                NameRecord record = recordsMatchingInput.get(i);
                if (record.getNameCount() == maxNameCount) {
                    results.add(record);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<NameRecord> findResults(UserInput input) {
        return config.isUsingLinkedList() ? findResultsLinkedList(input) : findResultsArrayList(input);
    }

    private boolean recordMatchesInput(NameRecord record, UserInput input) {
        return record.getName().equalsIgnoreCase(input.getName())
                && record.getGender().equalsIgnoreCase(input.getGender())
                && record.getState().equalsIgnoreCase(input.getState());
    }

    /**
     * Depending on size of results, print the results or display "no match"
     * message.
     */
    public void printResults(List<NameRecord> results) {
        try {
            if (results.size() <= 0) {
                System.out.println("No person found matching that name");
            } else if (results.size() == 1) {
                System.out.println(results.get(0));
            } else {
                // find lowest age and highest age, and return a range between the two
                printAgeRange(results);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * If the results size is more than 1, print an age range
     */
    private void printAgeRange(List<NameRecord> records) throws Exception {
        if (records.size() <= 1) {
            throw new Exception("Age range must have more than 1 record.");
        }
        try {
            // Use the first record since they all have equal data members
            NameRecord firstRec = records.get(0);
            int max = firstRec.getAge();
            int min = firstRec.getAge();
            for (int i = 0; i < records.size(); i++) {
                NameRecord record = records.get(i);
                if (record.getAge() < min) {
                    min = record.getAge();
                }
                if (record.getAge() > max) {
                    max = record.getAge();
                }
            }
            System.out.println(firstRec.getName() + " born in " + firstRec.getState() + " is most likely around " + min
                    + " to " + max + " years old.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        /* Validate input arguments */
        if (args.length != 1) {
            System.out.println("Invalid input. Usage... [TODO enter usage] ");
            System.exit(1);
        }
        Path configFile = Path.of(args[0]);
        AgePrediction agePrediction = new AgePrediction(configFile);
        agePrediction.start();
    }

}
