package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Scanner;

import utils.ExitHelper;
import utils.Logger;

public class MetricsExtractor {

    final static private String METRICS_DIRECTORY = "metrics";
    final static private String METRICS_FILE = "metrics.csv";
    final static private String SEPARATOR = ";";

    static public void generate(final Path homepage) {

    	final Path metricsDirectory = homepage.resolve(METRICS_DIRECTORY);
        final File metricsFile = metricsDirectory.resolve(METRICS_FILE).toFile();
    	
        final String metrics = extractMetrics(metricsDirectory);        
        
        try (final FileOutputStream os = new FileOutputStream(metricsFile);
             final PrintWriter pw = new PrintWriter(os)) {
            pw.print(metrics);
        } catch (final Exception e) {
                ExitHelper.exit(e);
        }
        
        Logger.log(Logger.Level.INFO)
              .append(metricsFile)
              .append(" is generated")
              .submit();
    }
    
    static private String extractMetrics(final Path metricsDirectory) {

    	final StringBuilder builder = new StringBuilder();
    	
    	builder.append("date" + SEPARATOR +
    			       "hits" + SEPARATOR +
    			       "files" + SEPARATOR +
    			       "pages" + SEPARATOR +
    			       "visits\n");

    	final File[] files = metricsDirectory.toFile().listFiles((dir, name) -> name.startsWith("usage_"));
    	for (File file: files) {
    		builder.append("01/");
 		    builder.append(file.getName().substring(10, 12));
    		builder.append("/");
    		builder.append(file.getName().substring(6, 10));
			try (final Scanner scanner = new Scanner(file)) {
				int lineNumber = 0;
	    		while (scanner.hasNextLine()) {
	     		   final String line = scanner.nextLine();
	     		   lineNumber++;
	     		   if (lineNumber == 49) { // hits
	     	          builder.append(SEPARATOR);
	     			  builder.append(extractNumberFromLine(line));
	     		   } else if (lineNumber == 51) { //files
		     	      builder.append(SEPARATOR);
	     			  builder.append(extractNumberFromLine(line));
	     		   } else if (lineNumber == 53) { // pages
		     	      builder.append(SEPARATOR);
	     			  builder.append(extractNumberFromLine(line));
	     		   } else if (lineNumber == 55) { // visits
		     	      builder.append(SEPARATOR);
	     			  builder.append(extractNumberFromLine(line));
		     	      builder.append("\n");
	     			  break;
	     		   } 
	     		}
			} catch (final FileNotFoundException e) {
				ExitHelper.exit(e);
			}
    	}
    	
		return builder.toString();
    }

    static private int extractNumberFromLine(final String line) {
    	final String numberAsString = line.replaceAll(".*<B>", "").replaceAll("</B>.*", "");
    	return Integer.parseInt(numberAsString);
    }
}
