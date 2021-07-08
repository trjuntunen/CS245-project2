import java.util.Calendar;

public class NameRecord {

	private final String name;
	private final String gender;
	private final String state;
	private final int year;
	private final int nameCount;

	public NameRecord(String name, String gender, String state, int year, int nameCount) {
		this.name = name;
		this.gender = gender;
		this.state = state;
		this.year = year;
		this.nameCount = nameCount;
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

	public int getYear() {
		return year;
	}

	public int getNameCount() {
		return nameCount;
	}

	/**
	 * Return the difference in the current year
	 * the record's year to calculate age.
	 */
	public int getAge() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		return year - getYear();
	}

	public String toString() {
		return name + " born in " + state + " is most likely around " + getAge() + " years" + " old.";
	}
}
