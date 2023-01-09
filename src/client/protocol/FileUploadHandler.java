package client.protocol;

import client.transport.Transport;
import message.FileUploadDone;
import message.FileUploadRequest;
import message.FileUploadResponse;

import java.io.InputStream;

import static util.Util.wrap;

public class FileUploadHandler extends Handler {
    public FileUploadHandler(Transport transport) {
        super(transport);
    }

    public boolean upload(String token, String filename, long filesize, InputStream input) {
        transport.send(new FileUploadRequest(token, filename, filesize));
        extractMessageOrFail(transport.receive(), FileUploadResponse.class);
        wrap(() -> input.transferTo(transport.getOutputStream()));
        extractMessageOrFail(transport.receive(), FileUploadDone.class);
        return true;
    }
}
