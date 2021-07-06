import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

public class Configuration {
	
    private String listType;
    private Path directory;

    public Configuration(Path configFile) {
        setConfig(configFile);
    }

    private void setConfig(Path configFile) {
        try (InputStream input = new FileInputStream(configFile.toString())) {
            Properties properties = new Properties();
            properties.load(input);
            listType = properties.getProperty("ListType");
            directory = Path.of(properties.getProperty("Directory"));
        } catch (Exception e) {
        	System.out.println("Error: There is a problem with the config file given.");
            System.exit(1);
        }
    }

    public String getListType() {
        return listType;
    }

    public Path getDirectory() {
        return directory;
    }

}
