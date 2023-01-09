package client.protocol;

import client.transport.Transport;
import exception.AppException;
import message.ErrorMessage;
import message.FileListRequest;
import message.FileListResponse;

import java.util.List;

public class FileListHandler extends Handler {
    public FileListHandler(Transport transport) {
        super(transport);
    }

    public List<String> get(String token) {
        transport.send(new FileListRequest(token));
        var message = extractMessageOrFail(transport.receive(), FileListResponse.class);
        return message.getFiles();
    }
}
