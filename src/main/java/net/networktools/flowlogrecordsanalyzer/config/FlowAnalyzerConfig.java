package net.networktools.flowlogrecordsanalyzer.config;

import java.nio.file.*;

public class FlowAnalyzerConfig {
    

    private final String protocolsFilePath;
    private final String lookupFilePath;
    private final String flowLogsFilePath;
    private final String tagCountOutputPath;
    private final String portProtocolOutputPath;

    private FlowAnalyzerConfig(Builder builder) {
        this.protocolsFilePath = builder.protocolsFilePath;
        this.lookupFilePath = builder.lookupFilePath;
        this.flowLogsFilePath = builder.flowLogsFilePath;
        this.tagCountOutputPath = builder.tagCountOutputPath;
        this.portProtocolOutputPath = builder.portProtocolOutputPath;
    }

    public String getProtocolsFilePath() {
        return protocolsFilePath;
    }

    public String getLookupFilePath() {
        return lookupFilePath;
    }

    public String getFlowLogFilePath() {
        return flowLogsFilePath;
    }

    public String getTagCountOutputPath() {
        return tagCountOutputPath;
    }

    public String getPortProtocolOutputPath() {
        return portProtocolOutputPath;
    }

    public static class Builder {
        private String protocolsFilePath;
        private String lookupFilePath;
        private String flowLogsFilePath;
        private String tagCountOutputPath;
        private String portProtocolOutputPath;

        public Builder protocolsFile(String path) {
            this.protocolsFilePath = path;
            return this;
        }

        public Builder lookupFile(String path) {
            this.lookupFilePath = path;
            return this;
        }

        public Builder flowLogFile(String path) {
            this.flowLogsFilePath = path;
            return this;
        }

        public Builder tagCountOutput(String path) {
            this.tagCountOutputPath = path;
            return this;
        }

        public Builder portProtocolOutput(String path) {
            this.portProtocolOutputPath = path;
            return this;
        }

        public FlowAnalyzerConfig build() {
            validatePaths();
            return new FlowAnalyzerConfig(this);
        }

        private void validatePaths(){
            validateFileExists(protocolsFilePath, "Protocols");
            validateFileExists(lookupFilePath, "Lookup");
            validateFileExists(flowLogsFilePath, "Flow Log");
        }

        private void validateFileExists(String path, String fileType) {
            if (path == null || !Files.exists(Paths.get(path))) {
                throw new IllegalArgumentException(fileType + " file path is invalid: " + path);
            }
        }
    }

}