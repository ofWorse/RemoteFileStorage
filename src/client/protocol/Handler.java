package client.protocol;

import client.transport.Transport;
import exception.AppException;
import message.ErrorMessage;
import message.Message;

public abstract class Handler {
    protected final Transport transport;

    public Handler(Transport transport) {
        this.transport = transport;
    }

    protected <T extends Message> T extractMessageOrFail(Message message, Class<T> expectedType) {
        if (message.getClass() == expectedType) {
            return expectedType.cast(message);
        } else if (message instanceof ErrorMessage error) {
            throw new AppException(error.getMessage());
        } else {
            throw new AppException("неизвестное сообщение: " + message);
        }
    }
}
