package server.protocol;

import message.LoginRequest;
import message.LoginResponse;
import server.service.UserService;
import server.transport.Transport;

public class LoginHandler extends Handler {
    private final UserService userService;

    public LoginHandler(Transport transport, UserService userService) {
        super(transport);
        this.userService = userService;
    }

    public void handle(LoginRequest request) {
        userService.checkUserCredentials(request.getUser(), request.getPassword());
        var token = userService.getTokenForUser(request.getUser());
        transport.send(new LoginResponse(token));
    }
}
