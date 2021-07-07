import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.ArrayList;

/*
 * TODO:ch
 * implement own linkedlist
 * do we have to deal with improperly formatted data?
 * do we have to validate that input path is valid format
 * 
 * If theconfiguration file is not provided or is missingor
 *  if any ofthe key-value pairs is improperly specified, yourimplementation
 *   must exit, gracefully, with a messageindicating the reason for early termination.
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
			ArrayList<NameRecord> results = findResults(input);
			printResults(results);
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
						records.add(record);
					}
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private ArrayList<NameRecord> findResults(UserInput input) {
		ArrayList<NameRecord> results = new ArrayList<>();
		try {
			ArrayList<NameRecord> recordsMatchingInput = new ArrayList<>();
			int maxNameCount = 0;
			for (int i = 0; i < records.size(); i++) {
				NameRecord record = records.get(i);
				// Check if record matches all the user inputs
				if (record.getName().equalsIgnoreCase(input.getName())
						&& record.getGender().equalsIgnoreCase(input.getGender())
						&& record.getState().equalsIgnoreCase(input.getState())) {

					// Add to matching names list
					recordsMatchingInput.add(record);
					// Find the max name count out of all the matching records
					if (record.getNameCount() > maxNameCount) {
						maxNameCount = record.getNameCount();
					}
				}
			}
			// Find all records that match maxNameCount and add to results
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

	// this function will change to just create a range of ages
	public void printResults(ArrayList<NameRecord> results) {
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
	
	private void printAgeRange(ArrayList<NameRecord> records) {
		NameRecord firstRec = records.get(0);
		int max = firstRec.getAge();
		int min = firstRec.getAge();
		for(int i = 0; i < records.size(); i++) {
			NameRecord record = records.get(i);
			if(record.getAge() < min) {
				min = record.getAge();
			}
			if(record.getAge() > max) {
				max = record.getAge();
			}
		}
		System.out.println(firstRec.getName() + " born in " + firstRec.getState() + " is most likely around " + min + " to " + max + " years old.");
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
