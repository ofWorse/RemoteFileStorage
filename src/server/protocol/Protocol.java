package server.protocol;

import exception.AppException;
import message.*;
import server.service.FileService;
import server.service.UserService;
import server.transport.Transport;

public class Protocol {
    private final Transport transport;
    private final UserService userService;
    private final FileService fileService;

    public Protocol(Transport transport, UserService userService, FileService fileService) {
        this.transport = transport;
        this.userService = userService;
        this.fileService = fileService;
    }

    public void handle() {
        try (var s = transport.getSocket()) {
            try {
                var message = transport.receive();
                System.out.println(message + " получено.");
                if (message instanceof LoginRequest request) {
                    new LoginHandler(transport, userService).handle(request);
                } else if (message instanceof FileListRequest request) {
                    new FileListHandler(transport, userService, fileService).handle(request);
                } else if (message instanceof FileUploadRequest request) {
                    new FileUploadHandler(transport, userService, fileService).handle(request);
                } else if (message instanceof FileRemoveRequest request) {
                    new FileRemoveHandler(transport, userService, fileService).handle(request);
                } else if(message instanceof FileDownloadRequest request) {
                    new FileDownloadHandler(transport, userService, fileService).handle(request);
                }
                else {
                    throw new AppException("неизвестное сообщение со стороны клиента.");
                }
            }
            catch (AppException e) {
                transport.send(new ErrorMessage(e.getMessage()));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
