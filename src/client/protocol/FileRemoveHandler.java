package client.protocol;

import client.transport.Transport;
import message.FileRemoveRequest;
import message.FileRemoveResponse;

public class FileRemoveHandler extends Handler {

    public FileRemoveHandler(Transport transport) {
        super(transport);
    }

    public boolean removeFile(String filename, String token) {
        transport.send(new FileRemoveRequest(filename, token));
        extractMessageOrFail(transport.receive(), FileRemoveResponse.class);
        return true;
    }
}
