package net.networktools.flowlogrecordsanalyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.networktools.flowlogrecordsanalyzer.config.FlowAnalyzerConfig;
import net.networktools.flowlogrecordsanalyzer.service.impl.FlowAnalyzerRecordsImpl;

public class App 
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        try{
            FlowAnalyzerConfig config = new FlowAnalyzerConfig.Builder()
                    .protocolsFile("input/protocols.csv")
                    .lookupFile("input/lookup.csv")
                    .flowLogFile("input/flowlogs.txt")
                    .tagCountOutput("output/tagcount.csv")
                    .portProtocolOutput("output/portprotocol.csv")
                    .build();

            FlowAnalyzerRecordsImpl analyzer = new FlowAnalyzerRecordsImpl(config);
            analyzer.loadProtocols();
            analyzer.loadLookupTable();
            analyzer.processFlowLogs();
            analyzer.writeOutput();

            logger.info("Flow log records analyzer completed successfully");
        }catch(Exception e){
            logger.error("Flow log records analyzer failed", e);
            System.exit(1);
        }
    }
}
