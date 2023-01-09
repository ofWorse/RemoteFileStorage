package server.service;

import exception.AppException;
import settings.Settings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public class FileService {
    public List<String> getFilesForUser(String user) {
        var dir = getUserDir(user);
        if (Files.notExists(dir)) {
            return List.of();
        }
        else {
            try (var stream = Files.list(dir)) {
                return stream.filter(Files::isRegularFile)
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .toList();
            }
            catch (IOException e) {
                throw new AppException(e.getMessage());
            }
        }
    }

    public Path getFilepath(String filename, String user) {
        return Path.of(getUserDir(user).toString(), filename);
    }


    private Path getUserDir(String user) {
        return Path.of(Settings.REMOTE_STORAGE_PATH, user);
    }

    public void withOutputStream(Path path, Consumer<OutputStream> action) {
        try (var stream = Files.newOutputStream(path)) {
            action.accept(stream);
        }
        catch (IOException e) {
            throw new AppException(e.getMessage());
        }
    }

    public void removeFile(String filename, String user) {
        var path = getFilepath(filename, user);
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new AppException(e.getMessage());
        }
    }

    public void download(String filename, String toDownloadPath, String user) {
        var path = getFilepath(filename, user);
        try {
            Files.copy(path, Path.of(toDownloadPath + "\\" + filename));
        } catch (IOException e) {
            throw new AppException(e.getMessage());
        }
    }

}
