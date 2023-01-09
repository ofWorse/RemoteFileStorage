package server.protocol;

import server.transport.Transport;

public abstract class Handler {
    protected final Transport transport;

    public Handler(Transport transport) {
        this.transport = transport;
    }
}
