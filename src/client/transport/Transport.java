package client.transport;

import exception.AppException;
import message.Message;
import settings.Settings;

import java.io.*;
import java.net.Socket;
import java.util.function.Function;

public class Transport {
    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Transport(Socket socket) {
        this.socket = socket;
    }

    public static <T> T withinConnection(Function<Transport, T> action) {
        try (var s = new Socket(Settings.SERVER_ADDRESS, Settings.SERVER_PORT)) {
            return action.apply(new Transport(s));
        } catch (IOException e) {
            throw new AppException(e.getMessage());
        }
    }

    public void send(Message message) {
        try {
            if (out == null) {
                out = new ObjectOutputStream(socket.getOutputStream());
            }
            out.writeObject(message);
        } catch (IOException e) {
            throw new AppException(e.getMessage());
        }
    }

    public Message receive() {
        try {
            if (in == null) {
                in = new ObjectInputStream(socket.getInputStream());
            }
            return (Message) in.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new AppException(e.getMessage());
        }
    }

    public OutputStream getOutputStream() {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
            throw new AppException(e.getMessage());
        }
    }
}
