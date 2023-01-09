package client.protocol;

import client.transport.Transport;
import message.FileDownloadDone;
import message.FileDownloadRequest;
import message.FileDownloadResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static util.Util.wrap;

public class FileDownloadHandler extends Handler {
    public FileDownloadHandler(Transport transport) {
        super(transport);
    }

    public boolean download(String filename, String toDownloadPath, String token) {
        transport.send(new FileDownloadRequest(filename, toDownloadPath, token));
        extractMessageOrFail(transport.receive(), FileDownloadResponse.class);
        return true;
    }
}
