package net.networktools.flowlogrecordsanalyzer.model;

import java.util.Objects;

/**
 * Composite key class that combines a port number and protocol name.
 * Used as a key in hash-based collections to track port-protocol combinations
 * in network flow analysis. This class is immutable and implements proper
 * equals() and hashCode() methods for use in HashMaps.
 */
public class PortProtocolKey {
    /** The network port number */
    private final int port;
    /** The protocol name (e.g., "tcp", "udp") */
    private final String protocol;

    /**
     * Creates a new PortProtocolKey
     * @param port The network port number
     * @param protocol The protocol name (must not be null)
     * @throws NullPointerException if protocol is null
     */
    public PortProtocolKey(int port, String protocol) {
        this.port = port;
        this.protocol = Objects.requireNonNull(protocol, "Protocol cannot be null");
    }

    /**
     * Compares this key with another object for equality.
     * Two keys are equal if they have the same port and protocol.
     * @param o Object to compare with
     * @return true if the objects are equal, false otherwise
     */
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
    
    /**
     * Generates a hash code for this key based on both port and protocol.
     * @return hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(port, protocol);
    }

    /**
     * @return The port number
     */
    public int getPort() {
        return port;
    }

    /**
     * @return The protocol name
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Returns a string representation of this key in the format "Port: X, Protocol: Y"
     * @return formatted string representation
     */
    @Override
    public String toString() {
        return String.format("Port: %d, Protocol: %s", port, protocol);
    }
}
