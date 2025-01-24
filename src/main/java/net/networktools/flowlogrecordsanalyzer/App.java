package net.networktools.flowlogrecordsanalyzer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.networktools.flowlogrecordsanalyzer.config.FlowAnalyzerConfig;
import net.networktools.flowlogrecordsanalyzer.service.impl.FlowAnalyzerRecordsImpl;

public class App 
{
    // Logger for logging information and errors
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args )
    {
        try{
            // Ensure output directory exists
            Path outputDir = Paths.get("output");
            if (!Files.exists(outputDir)) {
                Files.createDirectory(outputDir);
                logger.info("Created output directory: {}", outputDir);
            }

            // Create a configuration object for the flow analyzer
            FlowAnalyzerConfig config = new FlowAnalyzerConfig.Builder()
                    .protocolsFile("input/protocols.csv") // Path to protocols file
                    .lookupFile("input/lookup.csv") // Path to lookup file
                    .flowLogFile("input/flowlogs.txt") // Path to flow logs file
                    .tagCountOutput("output/tagcount.csv") // Path to output tag count file
                    .portProtocolOutput("output/portprotocol.csv") // Path to output port protocol file
                    .build();

            // Initialize the flow analyzer with the configuration
            FlowAnalyzerRecordsImpl analyzer = new FlowAnalyzerRecordsImpl(config);
            analyzer.loadProtocols();
            analyzer.loadLookupTable();
            analyzer.processFlowLogs();
            analyzer.writeOutput();

            logger.info("Flow log records analyzer completed successfully");
        }catch(Exception e){
            
            logger.error("Flow log records analyzer failed", e);
            System.exit(1); // Exit with error code
        }
    }
}
