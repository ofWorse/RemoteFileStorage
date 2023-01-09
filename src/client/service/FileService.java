package client.service;

import exception.AppException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import static util.Util.wrap;

public class FileService {
    public boolean exists(Path path) {
        return wrap(() -> Files.exists(path));
    }

    public String getFilename(Path path) {
        return path.getFileName().toString();
    }

    public long getFileSize(Path path) {
        return wrap(() -> Files.size(path));
    }

    public void withInputStream(Path path, Consumer<InputStream> action) {
        try (var stream = new BufferedInputStream(Files.newInputStream(path))) {
            action.accept(stream);
        }
        catch (IOException e) {
            throw new AppException(e.getMessage());
        }
    }
}
