package message;

public class FileRemoveRequest extends TokenizedMessage {
    private String filename;
    public FileRemoveRequest(String filename, String token) {
        super(token);
        this.filename = filename;
    }

    public String getFilename() {
        return this.filename;
    }

}
