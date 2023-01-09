package server.protocol;

import message.FileListRequest;
import message.FileListResponse;
import server.service.FileService;
import server.service.UserService;
import server.transport.Transport;

import java.util.List;

public class FileListHandler extends Handler {
    private final UserService userService;
    private final FileService fileService;

    public FileListHandler(Transport transport, UserService userService, FileService fileService) {
        super(transport);
        this.userService = userService;
        this.fileService = fileService;
    }

    public void handle(FileListRequest request) {
        var user = userService.getUserForToken(request.getToken());
        var files = fileService.getFilesForUser(user);
        transport.send(new FileListResponse(files));
    }
}
