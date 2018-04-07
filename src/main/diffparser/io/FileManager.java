package diffparser.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {
    private String basePath;

    public FileManager() {
        this.useCurrentDirectory();
    }

    private void useCurrentDirectory() {
        this.basePath = Paths.get(".").toAbsolutePath().normalize().toString();
    }

    public FileManager(String patchPath) {
        File patchFile = new File(patchPath);
        if (!patchFile.exists() || !patchFile.isFile()) {
            System.out.println("Bad patchPath provided");
            this.useCurrentDirectory();
            return;
        }

        // use parent directory as basePath
        this.basePath = patchFile.getAbsoluteFile().getParent();
    }

    private Path joinWithBase(String filepath) {
        return Paths.get(this.basePath, filepath);
    }

    public String readFile(String filepath) throws IOException {
        return new String(Files.readAllBytes(this.joinWithBase(filepath)));
    }

    public void writeFile(String filepath, String content) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(this.joinWithBase(filepath).toFile());
        out.print(content);
    }

    public void createDirectory(String dirpath) {
        File directory = this.joinWithBase(dirpath).toFile();
        if (!directory.exists()){
            directory.mkdir();
        }
    }
}
