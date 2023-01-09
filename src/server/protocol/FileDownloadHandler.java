package server.protocol;

import message.*;
import server.service.FileService;
import server.service.UserService;
import server.transport.Transport;

public class FileDownloadHandler extends Handler {
    private UserService userService;
    private FileService fileService;

    public FileDownloadHandler(Transport transport, UserService userService, FileService fileService) {
        super(transport);
        this.fileService = fileService;
        this.userService = userService;
    }


    public void handle(FileDownloadRequest request) {
        var user = userService.getUserForToken(request.getToken());
        fileService.download(request.getFilename(), request.getToDownloadPath(), user);
        transport.send(new FileDownloadResponse());
    }
}
