package net.networktools.flowlogrecordsanalyzer.service;

import java.io.IOException;

/**
 * Service interface for analyzing flow log records.
 * Defines the core operations for processing network flow logs,
 * including loading protocol definitions, lookup tables, and
 * generating analysis output.
 */
public interface FlowAnalyzerService {
    /**
     * Loads protocol definitions from the configured protocols file.
     * @throws IOException if there is an error reading the protocols file
     */
    void loadProtocols() throws IOException;

    /**
     * Loads the lookup table data from the configured lookup file.
     * @throws IOException if there is an error reading the lookup file
     */
    void loadLookupTable() throws IOException;

    /**
     * Processes the flow log records from the configured input file.
     * Analyzes network traffic patterns and prepares analysis results.
     * @throws IOException if there is an error reading the flow logs
     */
    void processFlowLogs() throws IOException;

    /**
     * Writes the analysis results to the configured output files.
     * This includes tag counts and port-protocol mappings.
     * @throws IOException if there is an error writing to the output files
     */
    void writeOutput() throws IOException;
}
