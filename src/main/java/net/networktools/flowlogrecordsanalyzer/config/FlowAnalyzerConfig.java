package net.networktools.flowlogrecordsanalyzer.config;

import java.nio.file.*;

/**
 * Configuration class for the Flow Log Records Analyzer.
 * This class handles the configuration of file paths for input and output files
 * used in the flow log analysis process. It uses the Builder pattern for
 * constructing instances with validated file paths.
 */
public class FlowAnalyzerConfig {
    
    /** Path to the file containing protocol definitions */
    private final String protocolsFilePath;
    /** Path to the lookup reference file */
    private final String lookupFilePath;
    /** Path to the flow logs input file */
    private final String flowLogsFilePath;
    /** Output path for tag count results */
    private final String tagCountOutputPath;
    /** Output path for port-protocol mapping results */
    private final String portProtocolOutputPath;

    private FlowAnalyzerConfig(Builder builder) {
        this.protocolsFilePath = builder.protocolsFilePath;
        this.lookupFilePath = builder.lookupFilePath;
        this.flowLogsFilePath = builder.flowLogsFilePath;
        this.tagCountOutputPath = builder.tagCountOutputPath;
        this.portProtocolOutputPath = builder.portProtocolOutputPath;
    }

    /**
     * @return The path to the protocols definition file
     */
    public String getProtocolsFilePath() {
        return protocolsFilePath;
    }

    /**
     * @return The path to the lookup reference file
     */
    public String getLookupFilePath() {
        return lookupFilePath;
    }

    /**
     * @return The path to the flow logs input file
     */
    public String getFlowLogFilePath() {
        return flowLogsFilePath;
    }

    /**
     * @return The output path for tag count results
     */
    public String getTagCountOutputPath() {
        return tagCountOutputPath;
    }

    /**
     * @return The output path for port-protocol mapping results
     */
    public String getPortProtocolOutputPath() {
        return portProtocolOutputPath;
    }

    /**
     * Builder class for FlowAnalyzerConfig.
     * Provides a fluent interface for constructing FlowAnalyzerConfig instances
     * with path validation.
     */
    public static class Builder {
        private String protocolsFilePath;
        private String lookupFilePath;
        private String flowLogsFilePath;
        private String tagCountOutputPath;
        private String portProtocolOutputPath;

        /**
         * Sets the protocols file path
         * @param path Path to the protocols definition file
         * @return Builder instance for method chaining
         */
        public Builder protocolsFile(String path) {
            this.protocolsFilePath = path;
            return this;
        }

        /**
         * Sets the lookup file path
         * @param path Path to the lookup reference file
         * @return Builder instance for method chaining
         */
        public Builder lookupFile(String path) {
            this.lookupFilePath = path;
            return this;
        }

        /**
         * Sets the flow log file path
         * @param path Path to the flow logs input file
         * @return Builder instance for method chaining
         */
        public Builder flowLogFile(String path) {
            this.flowLogsFilePath = path;
            return this;
        }

        /**
         * Sets the tag count output path
         * @param path Output path for tag count results
         * @return Builder instance for method chaining
         */
        public Builder tagCountOutput(String path) {
            this.tagCountOutputPath = path;
            return this;
        }

        /**
         * Sets the port-protocol output path
         * @param path Output path for port-protocol mapping results
         * @return Builder instance for method chaining
         */
        public Builder portProtocolOutput(String path) {
            this.portProtocolOutputPath = path;
            return this;
        }

        /**
         * Builds and validates the FlowAnalyzerConfig instance
         * @return A new FlowAnalyzerConfig instance
         * @throws IllegalArgumentException if any required file paths are invalid
         */
        public FlowAnalyzerConfig build() {
            validatePaths();
            return new FlowAnalyzerConfig(this);
        }

        /**
         * Validates that all required input files exist
         * @throws IllegalArgumentException if any required file is missing
         */
        private void validatePaths() {
            validateFileExists(protocolsFilePath, "Protocols");
            validateFileExists(lookupFilePath, "Lookup");
            validateFileExists(flowLogsFilePath, "Flow Log");
        }

        /**
         * Validates that a specific file exists at the given path
         * @param path The file path to validate
         * @param fileType The type of file being validated (for error messages)
         * @throws IllegalArgumentException if the file does not exist
         */
        private void validateFileExists(String path, String fileType) {
            if (path == null || !Files.exists(Paths.get(path))) {
                throw new IllegalArgumentException(fileType + " file path is invalid: " + path);
            }
        }
    }

}