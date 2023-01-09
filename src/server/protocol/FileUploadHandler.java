package server.protocol;

import message.*;
import server.service.FileService;
import server.service.UserService;
import server.transport.Transport;
import util.Util;

public class FileUploadHandler extends Handler {
    private final UserService userService;
    private final FileService fileService;

    public FileUploadHandler(Transport transport, UserService userService, FileService fileService) {
        super(transport);
        this.userService = userService;
        this.fileService = fileService;
    }

    public void handle(FileUploadRequest request) {
        var user = userService.getUserForToken(request.getToken());
        transport.send(new FileUploadResponse());
        var filepath = fileService.getFilepath(request.getFilename(), user);
        fileService.withOutputStream(filepath, output -> {
            var input = transport.getInputStream();
            Util.wrap(() -> {
                for (int i = 0; i < request.getFilesize(); i++) {
                    output.write((byte) (input.read()));
                }
                return true;
            });
        });
        transport.send(new FileUploadDone());
    }
}
