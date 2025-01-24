package net.networktools.flowlogrecordsanalyzer.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.networktools.flowlogrecordsanalyzer.config.FlowAnalyzerConfig;
import net.networktools.flowlogrecordsanalyzer.model.PortProtocolKey;
import net.networktools.flowlogrecordsanalyzer.service.FlowAnalyzerService;
import net.networktools.flowlogrecordsanalyzer.util.InputValidator;

/**
 * Implementation of FlowAnalyzerService that processes network flow log records.
 * This class handles the analysis of network traffic patterns by:
 * - Loading protocol definitions and lookup tables
 * - Processing flow log entries
 * - Generating statistics for tags and port-protocol combinations
 * - Writing analysis results to output files
 */
public class FlowAnalyzerRecordsImpl implements FlowAnalyzerService {
    private static final Logger logger = LoggerFactory.getLogger(FlowAnalyzerRecordsImpl.class);

    /** Configuration containing file paths for input/output */
    private final FlowAnalyzerConfig config;
    /** Maps port-protocol combinations to their corresponding tags */
    private final Map<PortProtocolKey, String> lookupTable = new HashMap<>();
    /** Maps protocol numbers to protocol names */
    private final Map<Integer, String> protocolMap = new HashMap<>();
    /** Tracks the count of each tag occurrence */
    private final Map<String, Integer> tagCounts = new HashMap<>();
    /** Tracks the count of each port-protocol combination */
    private final Map<PortProtocolKey, Integer> portProtocolCounts = new HashMap<>();

    /**
     * Constructs a new FlowAnalyzerRecordsImpl with the given configuration
     * @param config Configuration containing necessary file paths
     */
    public FlowAnalyzerRecordsImpl(FlowAnalyzerConfig config) {
        this.config = config;
    }

    /**
     * {@inheritDoc}
     * Loads protocol definitions from CSV file, mapping protocol numbers to names.
     * Skips invalid entries and handles parsing errors gracefully.
     */
    @Override
    public void loadProtocols() throws IOException {
        logger.info("Loading protocols from file: {}", config.getProtocolsFilePath());
        // Load protocols from file with more robust parsing
        try (BufferedReader br = new BufferedReader(new FileReader(config.getProtocolsFilePath()))) {
            br.readLine(); // Skip header
            br.lines()
                    .filter(line -> !line.trim().isEmpty()) // Skip empty lines
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length >= 2) // Ensure at least two columns
                    .forEach(parts -> {
                        try {
                            int protocolNumber = Integer.parseInt(parts[0].trim());
                            String protocolName = parts[1].trim().toLowerCase();
                            protocolMap.put(protocolNumber, protocolName);
                        } catch (NumberFormatException e) {
                            logger.warn("Skipping invalid protocol entry: {}", String.join(",", parts));
                        }
                    });
        }
        logger.info("Loaded {} protocols", protocolMap.size());
    }

    /**
     * {@inheritDoc}
     * Loads the lookup table that maps port-protocol combinations to tags.
     * Expected CSV format: port,protocol,tag
     */
    @Override
    public void loadLookupTable() throws IOException {
        logger.info("Loading lookup table from file: {}", config.getLookupFilePath());
        
        try (BufferedReader br = new BufferedReader(new FileReader(config.getLookupFilePath()))) {
            br.readLine(); // Skip header
            br.lines()
                    .map(line -> line.split(","))
                    .forEach(parts -> {
                        int port = Integer.parseInt(parts[0]);
                        String protocol = parts[1].toLowerCase();
                        String tag = parts[2];
                        lookupTable.put(new PortProtocolKey(port, protocol), tag);
                    });
        }
    }

    /**
     * {@inheritDoc}
     * Processes each line of the flow logs file, analyzing traffic patterns
     * and accumulating statistics for both tags and port-protocol combinations.
     */
    @Override
    public void processFlowLogs() throws IOException {
        logger.info("Processing flow logs from file: {}", config.getFlowLogFilePath());
        // Process flow logs
        try (BufferedReader br = new BufferedReader(new FileReader(config.getFlowLogFilePath()))) {
            br.lines()
                    .filter(line -> line.split("\\s+").length >= 14)
                    .forEach(this::processLogEntry);
        }

    }

    /**
     * Processes a single log entry line, extracting port and protocol information
     * and updating the tag and port-protocol count maps.
     * @param logLine The log entry line to process
     */
    private void processLogEntry(String logLine) {
        try {
            String[] parts = logLine.split("\\s+");

            // Validate log entry structure 
            InputValidator.validateLogEntry(parts);

            int dstPort = Integer.parseInt(parts[6]);
            int protocolNum = Integer.parseInt(parts[7]);

            // Additional validations can be added in the future
            InputValidator.validatePort(dstPort);
            InputValidator.validateProtocolNumber(protocolNum);

            String protocol = protocolMap.getOrDefault(protocolNum, "unknown");
            PortProtocolKey key = new PortProtocolKey(dstPort, protocol);

            String tag = lookupTable.getOrDefault(key, "Untagged");
            tagCounts.merge(tag, 1, Integer::sum);
            portProtocolCounts.merge(key, 1, Integer::sum);

        } catch (NumberFormatException e) {
            logger.error("Invalid numeric data in log entry: {}", logLine, e);
        } catch (IllegalArgumentException e) {
            logger.warn("Skipping invalid log entry: {}. Reason: {}", logLine, e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * Writes analysis results to two output files:
     * 1. Tag counts sorted by tag name
     * 2. Port-protocol combination counts sorted by port and protocol
     */
    @Override
    public void writeOutput() throws IOException {
        logger.info("Writing tag count output to file: {}", config.getTagCountOutputPath());
        // Tag Count Output
        writeSortedCsv(
                config.getTagCountOutputPath(),
                "Tag,Count",
                tagCounts.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .map(e -> String.format("%s,%d", e.getKey(), e.getValue())));

        logger.info("Writing port protocol output to file: {}", config.getPortProtocolOutputPath());
        // Port/Protocol Combination Counts
        writeSortedCsv(
                config.getPortProtocolOutputPath(),
                "Port,Protocol,Count",
                portProtocolCounts.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey(
                                Comparator.comparingInt(PortProtocolKey::getPort)
                                        .thenComparing(k -> k.getProtocol())))
                        .map(e -> String.format("%d,%s,%d",
                                e.getKey().getPort(),
                                e.getKey().getProtocol(),
                                e.getValue())));
    }

    /**
     * Writes sorted data to a CSV file with the specified header
     * @param outputPath Path to the output file
     * @param header CSV header line
     * @param dataStream Stream of formatted data lines
     * @throws IOException if an I/O error occurs
     */
    private void writeSortedCsv(String outputPath, String header, Stream<String> dataStream) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {
            bw.write(header + "\n");
            dataStream.forEach(line -> {
                try {
                    bw.write(line + "\n");
                } catch (IOException e) {
                    logger.error("Error writing to file: {}", outputPath, e);
                }
            });
        }
    }
}
