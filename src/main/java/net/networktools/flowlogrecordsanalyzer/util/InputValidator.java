package net.networktools.flowlogrecordsanalyzer.util;

import java.util.regex.Pattern;
import java.time.Instant;
import java.time.format.DateTimeParseException;

/**
 * Utility class for validating input data in flow log processing.
 * Currently implements core validations for protocol numbers, ports, and log entry structure.
 * 
 * Potential extensions based on requirements could include:
 * - IP address validation (IPv4/IPv6)
 * - Timestamp format validation
 * - Action field validation (ACCEPT/REJECT)
 * - Traffic volume validation ...
 */
public class InputValidator {
    // Maximum allowed port number
    private static final int MAX_PORT = 65535;

    // Maximum allowed protocol number
    private static final int MAX_PROTOCOL = 255;

    // Regex patterns for validation
    private static final Pattern IP_PATTERN = 
        Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    
    private static final Pattern ACTION_PATTERN = 
        Pattern.compile("^(ACCEPT|REJECT)$", Pattern.CASE_INSENSITIVE);

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
     * Validates an IP address string
     * @param ip IP address to validate
     * @throws IllegalArgumentException if IP address is invalid
     */
    public static void validateIpAddress(String ip) {
        if (ip == null || !IP_PATTERN.matcher(ip).matches()) {
            throw new IllegalArgumentException("Invalid IP address: " + ip);
        }
    }

    /**
     * Validates a timestamp string in Unix epoch format
     * @param timestamp Timestamp to validate
     * @throws IllegalArgumentException if timestamp is invalid
     */
    public static void validateTimestamp(String timestamp) {
        try {
            long epochTime = Long.parseLong(timestamp);
            Instant.ofEpochSecond(epochTime);
        } catch (NumberFormatException | DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid timestamp: " + timestamp);
        }
    }

    /**
     * Validates the action field (ACCEPT/REJECT)
     * @param action Action to validate
     * @throws IllegalArgumentException if action is invalid
     */
    public static void validateAction(String action) {
        if (action == null || !ACTION_PATTERN.matcher(action).matches()) {
            throw new IllegalArgumentException("Invalid action: " + action + ". Must be ACCEPT or REJECT");
        }
    }

    /**
     * Enhanced flow log entry validation with field-specific checks
     * @param logParts Parsed log entry parts
     * @throws IllegalArgumentException if any field is invalid
     */
    public static void validateLogEntry(String[] logParts) {
        // Basic structure validation
        if (logParts == null || logParts.length < 14) {
            throw new IllegalArgumentException("Malformed log entry");
        }
        try {
            // Validate specific fields
            validateTimestamp(logParts[0]);
            validateIpAddress(logParts[3]);  // source IP
            validateIpAddress(logParts[4]);  // dest IP
            validatePort(Integer.parseInt(logParts[5]));  // source port
            validatePort(Integer.parseInt(logParts[6]));  // dest port
            validateProtocolNumber(Integer.parseInt(logParts[7]));
            validateAction(logParts[12]);
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric value in log entry: " + e.getMessage());
        }
    }
}