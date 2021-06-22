import java.util.Calendar;

public class Person {

    private final String name;
    private final String gender;
    private final String state;
    private final int year;
    private final int nameCount;

    public Person(String name, String gender, String state, int year, int nameCount) {
        this.name = name;
        this.gender = gender;
        this.state = state;
        this.year = year;
        this.nameCount = nameCount;
    }

    public Person() {
        this.name = "";
        this.gender = "";
        this.state = "";
        this.year = -1;
        this.nameCount = -1;
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

    public int getAge() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return year - getYear();
    }

    public int getNameCount() {
        return nameCount;
    }

    public String toString() {
        return name + " " + gender + " " + state + " " + year + " " + nameCount;
    }
}
