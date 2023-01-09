package server.protocol;

import message.FileRemoveRequest;
import message.FileRemoveResponse;
import server.service.FileService;
import server.service.UserService;
import server.transport.Transport;

public class FileRemoveHandler extends Handler {

    private UserService userService;
    private FileService fileService;

    public FileRemoveHandler(Transport transport, UserService userService, FileService fileService) {
        super(transport);
        this.fileService = fileService;
        this.userService = userService;
    }


    public void handle(FileRemoveRequest message) {
        var user = userService.getUserForToken(message.getToken());
        fileService.removeFile(message.getFilename(), user);
        transport.send(new FileRemoveResponse());
    }
}
