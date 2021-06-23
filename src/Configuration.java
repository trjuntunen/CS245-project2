import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

    private String listType;
    private String directory;

    public Configuration(String configPath) {
        setConfig(configPath);
    }

    public void setConfig(String configPath) {
        try (InputStream input = new FileInputStream(configPath)) {
            Properties properties = new Properties();
            properties.load(input);
            listType = properties.getProperty("ListType");
            directory = properties.getProperty("Directory");
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
