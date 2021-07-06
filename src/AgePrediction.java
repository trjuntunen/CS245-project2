import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/*
 * TODO:
 * implement own linkedlist
 * do we have to deal with improperly formatted data?
 * do we have to validate that input path is valid format
 */
public class AgePrediction {

	private ArrayList<NameRecord> records;
	private Configuration config;

	public AgePrediction(Path configFile) {
		records = new ArrayList<>();
		config = new Configuration(configFile);
	}

	public void start() {
		// Build the database of records from the data files
		buildListOfRecords();
		
		while (true) {
			// Ask user all of the required questions
			UserInput input = new UserInput();
			input.askUserQuestions();
			
			// Find the most likely person based on the user inputs
			ArrayList<NameRecord> mostLikelyPeople = findMostLikelyPeople(input);
			printResults(mostLikelyPeople);
		}
	}

	private void buildListOfRecords() {
		// Walk through all directories and read all files within
		try (Stream<Path> paths = Files.walk(config.getDirectory())) {
			paths.filter(Files::isRegularFile).forEach(this::readDataFile);
		} catch (IOException e) {
			System.out.println("Error: Unable to access the data files.");
			System.exit(1);
		}
	}

	private void readDataFile(Path file) {
		if (file.toString().toLowerCase().endsWith(".txt")) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file.toAbsolutePath().toString()))) {
				String line;
				while ((line = reader.readLine()) != null) {
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
						records.add(record);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private ArrayList<NameRecord> findMostLikelyPeople(UserInput input) {
		ArrayList<NameRecord> mostLikelyPeople = new ArrayList<>();
		try {
			ArrayList<NameRecord> filteredPeople = new ArrayList<>();
			int maxNameCount = 0;
			for (int i = 0; i < records.size(); i++) {
				NameRecord record = records.get(i);
				if (record.getName().equalsIgnoreCase(input.getName()) && record.getGender().equalsIgnoreCase(input.getGender())
						&& record.getState().equalsIgnoreCase(input.getState())) {
					filteredPeople.add(record);
					if (record.getNameCount() > maxNameCount) {
						maxNameCount = record.getNameCount();
					}
				}
			}
			for (int i = 0; i < filteredPeople.size(); i++) {
				NameRecord record = filteredPeople.get(i);
				if (record.getNameCount() == maxNameCount) {
					mostLikelyPeople.add(record);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mostLikelyPeople;
	}

	// this function will change to just create a range of ages
	public void printResults(ArrayList<NameRecord> records) {
		try {
			if (records.size() <= 0) {
				System.out.println("No person found with those inputs.");
			} else if (records.size() == 1) {
				System.out.println(records.get(0));
			} else {
				// find lowest num, and return a range
				for (int i = 0; i < records.size(); i++) {
					System.out.println(records.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		/* Validate input args */
		if (args.length != 1) {
			System.out.println("Invalid input. Usage... [TODO enter usage] ");
			System.exit(1);
		}
		Path dataPath = Path.of(args[0]);
		AgePrediction agePrediction = new AgePrediction(dataPath);
		agePrediction.start();
	}

}
