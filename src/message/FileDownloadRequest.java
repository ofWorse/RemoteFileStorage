package message;

public class FileDownloadRequest extends TokenizedMessage {
    private String filename;
    private String toDownloadPath;
    public FileDownloadRequest(String filename, String toDownloadPath, String token) {
        super(token);
        this.filename = filename;
        this.toDownloadPath = toDownloadPath;
    }

    public String getFilename() {
        return this.filename;
    }

    public String getToDownloadPath() {
        return this.toDownloadPath;
    }
}
