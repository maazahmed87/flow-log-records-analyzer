package net.networktools.flowlogrecordsanalyzer.util;

import java.util.regex.Pattern;

/**
 * Utility class for validating input data in flow log processing.
 * Provides static methods to validate protocol numbers, ports, and log entry
 * structures.
 */
public class InputValidator {
    // Maximum allowed port number
    private static final int MAX_PORT = 65535;

    // Maximum allowed protocol number
    private static final int MAX_PROTOCOL = 255;

    /**
     * Validates protocol number to ensure it's within the acceptable range.
     * 
     * @param protocolNum Protocol number to validate
     * @throws IllegalArgumentException if protocol number is invalid
     */
    public static void validateProtocolNumber(int protocolNum) {
        if (protocolNum < 0 || protocolNum > MAX_PROTOCOL) {
            throw new IllegalArgumentException(
                    String.format("Invalid protocol number: %d. Must be between 0-255", protocolNum));
        }
    }

    /**
     * Validates destination port number.
     * 
     * @param port Port number to validate
     * @throws IllegalArgumentException if port is invalid
     */
    public static void validatePort(int port) {
        if (port < 0 || port > MAX_PORT) {
            throw new IllegalArgumentException(
                    String.format("Invalid port number: %d. Must be between 0-65535", port));
        }
    }

    /**
     * Validates flow log entry structure.
     * 
     * @param logParts Parsed log entry parts
     * @throws IllegalArgumentException if log entry is malformed
     */
    public static void validateLogEntry(String[] logParts) {
        final int EXPECTED_PARTS = 14; // Matches your specific flow log format

        if (logParts == null || logParts.length < EXPECTED_PARTS) {
            throw new IllegalArgumentException(
                    String.format("Malformed log entry. Expected %d parts, found %d",
                            EXPECTED_PARTS, logParts != null ? logParts.length : 0));
        }
    }
}