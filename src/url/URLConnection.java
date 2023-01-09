package url;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class URLConnection {

    private String url;
    private String fileName;

    public URLConnection(String path, String url) {
        this.fileName = path;
        this.url = url;
    }

    public void downloadFile() {
        downloadFileFromURL(url , fileName);
    }

    public static boolean downloadFileFromURL(String url, String fileName) {
        try (var input = URI.create(url).toURL().openStream()) {
            Files.copy(input, Paths.get(fileName));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
