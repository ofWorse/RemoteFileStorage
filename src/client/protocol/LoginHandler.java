package client.protocol;

import client.transport.Transport;
import exception.AppException;
import message.ErrorMessage;
import message.LoginRequest;
import message.LoginResponse;

public class LoginHandler extends Handler {
    public LoginHandler(Transport transport) {
        super(transport);
    }

    public String login(String user, String password) {
        transport.send(new LoginRequest(user, password));
        var message = transport.receive();
        if (message instanceof LoginResponse response) {
            return response.getToken();
        } else if (message instanceof ErrorMessage error) {
            throw new AppException(error.getMessage());
        } else {
            throw new AppException("unexpected message: " + message);
        }
    }
}
