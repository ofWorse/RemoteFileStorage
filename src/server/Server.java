package server;

import server.protocol.Protocol;
import server.service.FileService;
import server.service.UserService;
import server.transport.Transport;
import settings.Settings;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final ExecutorService pool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        var userService = new UserService();
        var fileService = new FileService();
        try (var serverSocket = new ServerSocket(Settings.SERVER_PORT)) {
            while (true) {
                var socket = serverSocket.accept();
                System.out.println(socket + " присоединился.");
                pool.submit(() -> new Protocol(new Transport(socket), userService, fileService).handle());
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        finally {
            pool.shutdown();
        }
    }
}
