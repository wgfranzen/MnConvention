# Download MnConvention 

[Download Zip](../MnConvention-2023.0.0-SNAPSHOT-windows.zip) to download a zip file.

[Download tar](../MnConvention-2023.0.0-SNAPSHOT-bin.tgz) to download a tar file.

[Download jar](../MnConvention-2023.0.0-SNAPSHOT.jar) to download the jar (no dependencies).

Note the Zip and tar files contain the documentation and dependencies.  

Download the appropriate file and extract accordingly.

## Checksums

* Zip MD5: 
* Zip SHA256: 
* tar MD5:
* tar SHA256: 
* jar MD5: 
* jar SHA256: 

## Quick Installation Verification 

### Help

At a command line (where the current directory contains the jar) issue: 
<code>java -jar MnConvention-2023.0.0-SNAPSHOT.jar -h</code>

See the response:

    Usage: MnConvention [-hV] -i=input CSV file -o=output XLS file
    MnConvention is a post-processor for reading the CSV generated from the
    BitForms containing registration information.  The registration information 
    needs to be parsed to translate escaped characters and specific unicode
    characters from the array of selected seminars.  Ultimately, this will produce
    an Excel XLS file that can be read as an Excel Spreadsheet.
    
    -h, --help      Show this help message and exit.
    -i, --input=input CSV file
    the input file downloaded from the web application
    -o, --output=output XLS file
    the standardized output file based on the input file
    -V, --version   Print version information and exit.
    Copyright (c) 2023 by WF0B, Bill Franzen.  All Rights Reserved.
    
    Licensed under GNU AFFERO GENERAL PUBLIC LICENSE

### Version 

At a command line (where the current directory contains the jar) issue:
<code>java -jar MnConvention-2023.0.0-SNAPSHOT.jar -V</code>

See the response:

    net.wf0b.code.MnConvention.MnConvention-2023.0.0-SNAPSHOT
    
### Use Test Data 

Change Directory to the inner directory (where you find run.bat or run.sh).  That directory contains the jar file and at
least one *.csv file.

Issue <code>run -i bitform_2test4d.csv -o bitform_2test4d.xlsx</code> or <code>java -jar MnConvention-2023.0.0-SNAPSHOT.jar -i bitform_2test4d.csv -o bitform_2test4d.xlsx</code>.

See the response:

    ERROR StatusLogger Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console...
    Registrations recorded: 8
    Seminar Preferences recorded: 33
    Meals counted: 3
    Seminars counted: 22

Note: The <code>ERROR StatusLogger Log4j2...</code> is expected.  The application supports Simple Logging, and will log errors to the console.  

Open <code>bitform_2test4d.xlsx</code> in Excel.

### Test Errors

#### Invalid Parameter 

The response will indicate what is unknown
(<code>Unknown options: '-x', '-y'</code>) or what is missing 
(<code>Missing required options: '--input=input CSV file', '--output=output XLS file'</code>), and then display the help 
text.  

    Usage: MnConvention [-hV] -i=input CSV file -o=output XLS file
    MnConvention is a post-processor for reading the CSV generated from the
    BitForms containing registration information.  The registration information
    needs to be parsed to translate escaped characters and specific unicode
    characters from the array of selected seminars.  Ultimately, this will produce
    an Excel XLS file that can be read as an Excel Spreadsheet.
    
    -h, --help      Show this help message and exit.
    -i, --input=input CSV file
    the input file downloaded from the web application
    -o, --output=output XLS file
    the standardized output file based on the input file
    -V, --version   Print version information and exit.
    Copyright (c) 2023 by WF0B, Bill Franzen.  All Rights Reserved.
    
    Licensed under GNU AFFERO GENERAL PUBLIC LICENSE

#### Input File is not found 

Look for <code>Caused by: java.nio.file.NoSuchFileException: .....csv </code>. 

    java.lang.RuntimeException: Error processing Input
    at net.wf0b.code.MnConvention.run(MnConvention.java:138)
    at picocli.CommandLine.executeUserObject(CommandLine.java:2026)
    at picocli.CommandLine.access$1500(CommandLine.java:148)
    at picocli.CommandLine$RunLast.executeUserObjectOfLastSubcommandWithSameParent(CommandLine.java:2461)
    at picocli.CommandLine$RunLast.handle(CommandLine.java:2453)
    at picocli.CommandLine$RunLast.handle(CommandLine.java:2415)
    at picocli.CommandLine$AbstractParseResultHandler.execute(CommandLine.java:2273)
    at picocli.CommandLine$RunLast.execute(CommandLine.java:2417)
    at picocli.CommandLine.execute(CommandLine.java:2170)
    at net.wf0b.code.MnConvention.main(MnConvention.java:474)
    Caused by: java.nio.file.NoSuchFileException: bitform_3test4d.csv
    at java.base/sun.nio.fs.WindowsException.translateToIOException(WindowsException.java:85)
    at java.base/sun.nio.fs.WindowsException.rethrowAsIOException(WindowsException.java:103)
    at java.base/sun.nio.fs.WindowsException.rethrowAsIOException(WindowsException.java:108)
    at java.base/sun.nio.fs.WindowsFileSystemProvider.newByteChannel(WindowsFileSystemProvider.java:236)
    at java.base/java.nio.file.Files.newByteChannel(Files.java:380)
    at java.base/java.nio.file.Files.newByteChannel(Files.java:432)
    at java.base/java.nio.file.spi.FileSystemProvider.newInputStream(FileSystemProvider.java:422)
    at java.base/java.nio.file.Files.newInputStream(Files.java:160)
    at java.base/java.nio.file.Files.newBufferedReader(Files.java:2922)
    at java.base/java.nio.file.Files.newBufferedReader(Files.java:2955)
    at net.wf0b.code.MnConvention.loadRegistrations(MnConvention.java:156)
    at net.wf0b.code.MnConvention.run(MnConvention.java:136)
    ... 9 more


#### Output File is in Use 

Look for <code>Caused by: java.io.FileNotFoundException: ....xlsx (The process cannot access the file because it is being used by another process)</code>

Open the resulting bitform_2test4d.xlsx in Excel.  Then at a command line (where the current directory contains the jar) issue:
<code>java -jar MnConvention-2023.0.0-SNAPSHOT.jar -i bitform_2test4d.csv -o bitform_2test4d.xlsx</code>

See the response:

    ERROR StatusLogger Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console...
    Registrations recorded: 8 
    Seminar Preferences recorded: 33
    Meals counted: 3
    Seminars counted: 22
    java.lang.RuntimeException: Error processing output
    at net.wf0b.code.MnConvention.run(MnConvention.java:145)
    at picocli.CommandLine.executeUserObject(CommandLine.java:2026)
    at picocli.CommandLine.access$1500(CommandLine.java:148)
    at picocli.CommandLine$RunLast.executeUserObjectOfLastSubcommandWithSameParent(CommandLine.java:2461)
    at picocli.CommandLine$RunLast.handle(CommandLine.java:2453)
    at picocli.CommandLine$RunLast.handle(CommandLine.java:2415)
    at picocli.CommandLine$AbstractParseResultHandler.execute(CommandLine.java:2273)
    at picocli.CommandLine$RunLast.execute(CommandLine.java:2417)
    at picocli.CommandLine.execute(CommandLine.java:2170)
    at net.wf0b.code.MnConvention.main(MnConvention.java:474)
    Caused by: java.io.FileNotFoundException: bitform_2test4d.xlsx (The process cannot access the file because it is being used by another process)
    at java.base/java.io.FileOutputStream.open0(Native Method)
    at java.base/java.io.FileOutputStream.open(FileOutputStream.java:293)
    at java.base/java.io.FileOutputStream.<init>(FileOutputStream.java:235)
    at java.base/java.io.FileOutputStream.<init>(FileOutputStream.java:184)
    at net.wf0b.code.MnConvention.writeWorkBook(MnConvention.java:250)
    at net.wf0b.code.MnConvention.run(MnConvention.java:143)
    ... 9 more




Change Directory to the inner directory (where you find run.bat or run.sh).  That directory contains the jar file and at
least one *.csv file.  

Issue <code>run -i bitform_2test4d.csv -o bitform_2test4d.xlsx</code> or <code>java -jar MnConvention-2023.0.0-SNAPSHOT.jar -i bitform_2test4d.csv -o bitform_2test4d.xlsx</code>.

Open <code>bitform_2test4d.xlsx</code> in Excel.