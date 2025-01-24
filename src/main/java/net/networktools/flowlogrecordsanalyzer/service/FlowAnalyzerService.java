package net.networktools.flowlogrecordsanalyzer.service;

import java.io.IOException;

public interface FlowAnalyzerService {
    void loadProtocols() throws IOException;

    void loadLookupTable() throws IOException;

    void processFlowLogs() throws IOException;

    void writeOutput() throws IOException;
}
