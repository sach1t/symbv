package diffparser.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileManager {
    public String readFile(String filepath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filepath)));
    }

    public void writeFile(String filepath, String content) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(filepath);
        out.print(content);
    }
}
