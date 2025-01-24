# Flow Log Records Analyzer

## Table of Contents
- [Overview](#overview)
- [Key Features](#key-features)
- [Technical Architecture](#technical-architecture)
  - [Design Principles](#design-principles)
  - [Design Patterns](#design-patterns)
  - [Dependency Injection](#dependency-injection)
- [Technologies and Frameworks](#technologies-and-frameworks)
- [Use Cases](#use-cases)
- [Scalability Strategies](#scalability-strategies)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
  - [Input Files](#input-files)
  - [Output Files](#output-files)
- [Providing Input Files](#providing-input-files)
- [Testing](#testing)

## Overview

Network flow log analysis tool for processing AWS VPC flow logs, mapping network traffic to predefined tags based on destination port and protocol combinations.

## Key Features

- Parse network flow log files
- Map traffic to predefined tags
- Generate tag occurrence statistics
- Robust input validation
- Configurable processing pipeline
- Docker and Docker Compose support

## Technical Architecture

### Design Principles

- **Separation of Concerns**: Modular design with distinct layers
- **Immutability and Type Safety**: Robust key representations
- **Comprehensive Logging**: Detailed error tracking and processing insights

### Design Patterns

#### Builder Pattern for Configuration Management

The project uses the Builder pattern in `FlowAnalyzerConfig` to provide:

- **Flexible Configuration**: Create complex configuration objects step by step
- **Immutability**: Ensure configuration objects cannot be modified after creation
- **Robust Validation**: Validate configuration parameters before object instantiation

```java
FlowAnalyzerConfig config = new FlowAnalyzerConfig.Builder()
    .protocolsFile("protocols.csv")
    .lookupFile("lookup.csv")
    .flowLogFile("flowlogs.txt")
    .tagCountOutput("tagcount.csv")
    .portProtocolOutput("portprotocol.csv")
    .build();
```

#### Service Interface Pattern

The `FlowAnalyzerService` interface provides a flexible, modular approach to log processing:
- Defines standard methods for protocol loading, log analysis, and output generation
- Enables easy implementation of alternative processing strategies
- Supports future extensions and custom log analysis approaches

### Dependency Injection

Configuration is injected into `FlowAnalyzerRecordsImpl`, allowing:

- **Loose coupling between components**: Components are less dependent on each other, making the system more modular.
- **Easier testing and configuration management**: Simplifies the process of testing and managing configurations.
- **Flexibility in providing different configuration sources**: Allows for different configurations to be easily swapped in and out.

```java
FlowAnalyzerConfig config = new FlowAnalyzerConfig.Builder()
    .protocolsFile("protocols.csv")
    .lookupFile("lookup.csv")
    .flowLogFile("flowlogs.txt")
    .tagCountOutput("tagcount.csv")
    .portProtocolOutput("portprotocol.csv")
    .build();

FlowAnalyzerRecordsImpl analyzer = new FlowAnalyzerRecordsImpl(config);
```

## Technologies and Frameworks

- **Language**: Java 11
- **Build Tool**: Maven
- **Logging**: SLF4J
- **Containerization**: Docker
- **Testing**: JUnit Jupiter

## Use Cases

### Network Security Monitoring
- Categorize network traffic by service and risk profile
- Identify unusual port or protocol usage
- Zero Trust architecture validation
- Threat detection and anomaly identification

### Cloud Security and Compliance
- Monitor AWS VPC flow logs
- Validate firewall rule effectiveness
- Map communication patterns across network segments
- Track cloud security compliance

### Integration Capabilities
- Supplement intrusion detection systems
- Analyze inter-service communication
- Support network segmentation analysis

## Scalability Strategies

1. **Parallel Processing**
   - Implement parallel stream processing
   - Use `ForkJoinPool` for large log files

2. **Distributed Processing**
   - Integrate with Apache Spark
   - Implement message queue log ingestion

3. **Database Integration**
   - Replace in-memory maps with database storage
   - Use JPA for persistent mappings

## Getting Started

### Prerequisites
- Java 11+
- Maven
- Docker (optional)
- Input files in the `input` directory

### Installation

**Using Maven**:
```bash
# Ensure input files are in the input directory
# The output directory will be created automatically if it doesn't exist
mvn clean package
java -jar target/flow-log-records-analyzer-1.0-SNAPSHOT.jar
```

**Docker option**:
```bash
# Ensure input files are in the input directory
# The output directory will be created automatically in the container
docker-compose up --build
```

## Project Structure

```
.
|-- Dockerfile
|-- README.md
|-- input/               # Required directory for input files
|   |-- flowlogs.txt
|   |-- lookup.csv
|   `-- protocols.csv
|-- output/             # Auto-created if missing
|   |-- portprotocol.csv
|   `-- tagcount.csv
`-- src/
    # ...existing structure...
```

## Configuration

### Directory Structure
- The `input` directory must exist and contain the required input files
- The `output` directory will be automatically created if it doesn't exist
- Output files will be generated in the `output` directory

### Input Files
- `lookup.csv`: Maps ports and protocols to tags
- `protocols.csv`: Defines protocol number mappings
- `flowlogs.txt`: Network flow log entries

### Output Files
- `tagcount.csv`: Aggregated tag occurrence counts
- `portprotocol.csv`: Port and protocol combination statistics

## Providing Input Files

By default, the application expects input files to be placed in the `input` directory. This is something we can improve for scaling in the future. You can provide the necessary input files as follows:

1. **lookup.csv**: This file should map ports and protocols to tags. Place it in the `input` directory.
2. **protocols.csv**: This file should define protocol number mappings. Place it in the `input` directory.
3. **flowlogs.txt**: This file should contain network flow log entries. Place it in the `input` directory.

### Example Input Files

**lookup.csv**:
```csv
dstport,protocol,tag
80,TCP,HTTP
443,TCP,HTTPS
22,TCP,SSH
```

**protocols.csv**:
```csv
protocol_number,protocol_name
6,TCP
17,UDP
1,ICMP
```

**flowlogs.txt**:
```txt
2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 49153 6 25 20000 1620140761 1620140821 ACCEPT OK 
2 123456789012 eni-4d3c2b1a 192.168.1.100 203.0.113.101 23 49154 6 15 12000 1620140761 1620140821 REJECT OK 
2 123456789012 eni-5e6f7g8h 192.168.1.101 198.51.100.3 25 49155 6 10 8000 1620140761 1620140821 ACCEPT OK 
2 123456789012 eni-9h8g7f6e 172.16.0.100 203.0.113.102 110 49156 6 12 9000 1620140761 1620140821 ACCEPT OK 
2 123456789012 eni-7i8j9k0l 172.16.0.101 192.0.2.203 993 49157 6 8 5000 1620140761 1620140821 ACCEPT OK 
2 123456789012 eni-6m7n8o9p 10.0.2.200 198.51.100.4 143 49158 6 18 14000 1620140761 1620140821 ACCEPT OK 
2 123456789012 eni-1a2b3c4d 192.168.0.1 203.0.113.12 1024 80 6 10 5000 1620140661 1620140721 ACCEPT OK 
2 123456789012 eni-1a2b3c4d 203.0.113.12 192.168.0.1 80 1024 6 12 6000 1620140661 1620140721 ACCEPT OK 
2 123456789012 eni-1a2b3c4d 10.0.1.102 172.217.7.228 1030 443 6 8 4000 1620140661 1620140721 ACCEPT OK 
2 123456789012 eni-5f6g7h8i 10.0.2.103 52.26.198.183 56000 23 6 15 7500 1620140661 1620140721 REJECT OK 
2 123456789012 eni-9k10l11m 192.168.1.5 51.15.99.115 49321 25 6 20 10000 1620140661 1620140721 ACCEPT OK 
2 123456789012 eni-1a2b3c4d 192.168.1.6 87.250.250.242 49152 110 6 5 2500 1620140661 1620140721 ACCEPT OK 
2 123456789012 eni-2d2e2f3g 192.168.2.7 77.88.55.80 49153 993 6 7 3500 1620140661 1620140721 ACCEPT OK 
2 123456789012 eni-4h5i6j7k 172.16.0.2 192.0.2.146 49154 143 6 9 4500 1620140661 1620140721 ACCEPT OK 
```

## Testing

Due to time constraints during the initial development, comprehensive unit tests were not implemented. Future improvements should include:

- Unit tests for `InputValidator`
- Tests for `PortProtocolKey` model
- Comprehensive integration tests for `FlowAnalyzerRecordsImpl`
- Coverage for edge cases and error handling scenarios

