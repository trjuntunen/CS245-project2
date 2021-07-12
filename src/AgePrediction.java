import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/*
 * TODO:
 * implement own linkedlist
 * 
 * [Description]
 * 
 * Author: Teddy Juntunen
 */
public class AgePrediction {

	private List<NameRecord> records;
	private final Configuration config;

	public AgePrediction(Path configFile) {
		config = new Configuration(configFile);
		if(config.getListType().equalsIgnoreCase("linkedlist")) {
			records = new LinkedList<>();
			System.out.println("Using linked list...");
		} else {
			System.out.println("Using array list...");
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
						if(config.getListType().equalsIgnoreCase("linkedlist")) {
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
	 * Finds the results of the most likely name given the user input
	 */
	private List<NameRecord> findResults(UserInput input) {
		if(config.getListType().equalsIgnoreCase("linkedlist")) {
			return findResultsLinkedList(input);
		}
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
			// Find all matching records that match maxNameCount and add to results.
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

	private List<NameRecord> findResultsLinkedList(UserInput input) {
		LinkedList<NameRecord> results = new LinkedList<>();
		LinkedList<NameRecord> recordsMatchingInput = new LinkedList<>();
		LinkedList<NameRecord> temp = (LinkedList<NameRecord>) records;
		Node<NameRecord> tempHead = temp.getHead();
		int maxNameCount = 0;
		for (int i = 0; i < temp.size(); i++) {
			if (tempHead.data.getName().equalsIgnoreCase(input.getName())
					&& tempHead.data.getGender().equalsIgnoreCase(input.getGender())
					&& tempHead.data.getState().equalsIgnoreCase(input.getState())) {
				recordsMatchingInput.add(tempHead.data);

				if (tempHead.data.getNameCount() > maxNameCount) {
					maxNameCount = tempHead.data.getNameCount();
				}
			}
			tempHead = tempHead.next;
		}
		Node<NameRecord> matchHead = recordsMatchingInput.getHead();
		for(int i = 0; i < recordsMatchingInput.size(); i++) {
			if(matchHead.data.getNameCount() == maxNameCount) {
				results.add(matchHead.data);
			}
			matchHead = matchHead.next;
		}
		return results;
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
