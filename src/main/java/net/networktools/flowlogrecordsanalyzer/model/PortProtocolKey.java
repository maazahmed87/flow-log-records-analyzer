package net.networktools.flowlogrecordsanalyzer.model;

import java.util.Objects;

public class PortProtocolKey {
    private final int port;
    private final String protocol;

    public PortProtocolKey(int port, String protocol) {
        this.port = port;
        this.protocol = Objects.requireNonNull(protocol, "Protocol cannot be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PortProtocolKey that = (PortProtocolKey) o;

        if (port != that.port)
            return false;
        return protocol != null ? protocol.equals(that.protocol) : that.protocol == null;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(port, protocol);
    }

    public int getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    @Override
    public String toString() {
        return String.format("Port: %d, Protocol: %s", port, protocol);
    }
}
