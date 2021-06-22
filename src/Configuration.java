import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Configuration {

    private String listType;
    private String directory;

    public Configuration(String configPath) {
        readConfig(configPath);
    }

    private void readConfig(String configPath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(configPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("=");
                if (values[0].equals("ListType")) {
                    listType = values[1];
                } else if (values[0].equals("Directory")) {
                    directory = values[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getListType() {
        return listType;
    }

    public String getDirectory() {
        return directory;
    }

}
