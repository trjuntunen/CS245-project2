public class Person {

    private final String name;
    private final String gender;
    private final String state;
    private final String year;
    private final String nameCount;

    public Person(String name, String gender, String state, String year, String nameCount) {
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

    public String getYear() {
        return year;
    }

    public String getNameCount() {
        return nameCount;
    }
}
