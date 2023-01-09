package message;

public class FileUploadRequest extends TokenizedMessage {
    private final String filename;
    private final long filesize;

    public FileUploadRequest(String token, String filename, long filesize) {
        super(token);
        this.filename = filename;
        this.filesize = filesize;
    }

    public String getFilename() {
        return filename;
    }

    public long getFilesize() {
        return filesize;
    }
}
