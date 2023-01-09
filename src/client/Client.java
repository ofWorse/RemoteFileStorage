package client;

import client.io.Console;
import client.protocol.*;
import client.service.FileService;
import client.transport.Transport;
import exception.AppException;
import settings.Settings;
import url.URLConnection;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;


public class Client {
    private final Console console = new Console();
    private final FileService fileService = new FileService();
    private String token;

    public static void main(String[] args) {
        new Client().start();
    }

    private void start() {
        String command = null;
        do {
            try {
                console.print(">>> ");
                command = console.read();
                switch (command) {
                    case "login" -> login();
                    case "token" -> token();
                    case "set" -> setFolder();
                    case "list" -> list();
                    case "upload" -> upload();
                    case "download" -> download();
                    case "remove" -> remove();
                    case "copy" -> copy();
                    case "help" -> help();
                    case "exit" -> console.println("пока!");
                    default -> console.println("неизвестная команда.");
                }
            } catch (AppException | IOException e) {
                console.println("ошибка: " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        while (!"exit".equals(command));
    }


    private void copy() {
        console.print("Вставьте ссылку: ");
        String url = console.read();
        if (url.equals("exit")) return;
        console.print("Введите путь к каталогу для копирования: ");
        String path = console.read();
        if (path.equals("exit")) return;
        console.print("Придумайте имя для файла с расширением: ");
        path += console.read();
        if (path.equals("exit")) return;

        new URLConnection(path, url).downloadFile();
        console.println("Файлы были скопированы в отдел: " + path);
    }

    private void setFolder() {
        console.print("Введите путь к каталогу: ");
        String path = console.read();
        if (path.equals("exit")) return;
        File dir = new File(path);
        while (!dir.isDirectory()) {
            console.println("Неверно введен путь к каталогу, или каталога не существует.");
            console.print("Попробуйте еще раз: ");
            path = console.read();
            if (path.equals("exit")) return;
            dir = new File(path);
        }
        Settings.LOCAL_STORAGE_PATH = String.valueOf(dir);

        console.println("Каталог клиента задан.");
    }

    private void login() {
        console.print("login: ");
        var user = console.read();
        console.print("password: ");
        var password = console.read();
        token = Transport.withinConnection(transport ->
                new LoginHandler(transport).login(user, password));
        console.println("регистрация прошла успешно!");
    }

    private void token() {
        console.println("token = " + token);
    }

    private void list() throws IOException {
        var files = Transport.withinConnection(transport ->
                new FileListHandler(transport).get(token));
        if (files.isEmpty()) {
            console.println("хранилище пустое");
        } else {
            console.println("файлы в хранилище:");
            files.forEach(console::println);
        }
    }

    private void upload() {
        console.print("Введите полный путь к файлу: ");
        var filepath = console.read();
        while (filepath.isEmpty()) {
            console.println("Неверно указан путь или файла не существует.");
            console.print("Попробуйте снова: ");
            filepath = console.read();
        }

        var path = Path.of(filepath);

        if (!fileService.exists(path))
            throw new AppException("Файл не найден.");


        fileService.withInputStream(path, input -> {
            var filename = fileService.getFilename(path);
            var filesize = fileService.getFileSize(path);
            Transport.withinConnection(transport ->
                    new FileUploadHandler(transport).upload(token, filename, filesize, input));
        });
    }

    private void download() throws IOException {
        list();
        console.print("Введите имя одного из файлов: ");
        String file = console.read();
        if(file.equals("exit")) return;

        if(Settings.LOCAL_STORAGE_PATH == null) {
            console.println("Ваш персональный путь к локальному каталогу пуст!");
            setFolder();
        }

        Transport.withinConnection(transport -> new FileDownloadHandler(transport).download(file, Settings.LOCAL_STORAGE_PATH, token));

        console.println("Вам был отправлен файл в каталог: " + Settings.LOCAL_STORAGE_PATH);
    }

    public void remove() throws IOException, RuntimeException {
        list();
        console.print("Введите имя одного из перечисленных файлов: ");
        String file = console.read();
        if (file.equals("exit")) return;

        Transport.withinConnection(transport -> new FileRemoveHandler(transport).removeFile(file, token));
        console.println("Файл " + file + " бы успешно удален.");
    }

    private void help() {
        console.println("СПИСОК КОМАНД, ДОСТУПНЫХ НА ЭТОМ СЕРВЕРЕ:");
        console.println("\n\tlogin - команда для регистрации пользователя. ");
        console.println("\n\ttoken - вывод персонального токена при учете,");
        console.println("\tесли вы были зарегистрированы.");
        console.println("\n\tset - установка каталога клиента. ");
        console.println("\n\tlist - вывод содержимого из указанного каталога.");
        console.println("\n\tupload - загрузка содержимого из локального каталога в каталог сервера. ");
        console.println("\n\tdownload - выгрузка определенного файла из удаленного хранилища. ");
        console.println("\t(содержит реализацию команды set).");
        console.println("\n\tremove - команда для удаления файла из удаленного хранилища. ");
        console.println("\t(содержит реализацию команды list).");
        console.println("\n\tcopy - копирует файлы из облачного хранилища. ");
        console.println("\n\texit - покинуть сервер (все учетные данные будут удалены).");
    }
}
