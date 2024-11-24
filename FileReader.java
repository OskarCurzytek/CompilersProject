import java.nio.file.*;
import java.io.IOException;

public class FileReader {
    public static String readFile(String fileName) {
        try {
            return Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
    }
}
