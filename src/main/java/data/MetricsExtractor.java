package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Scanner;

import utils.ExitHelper;
import utils.Logger;

/**
 *
 */
public class MetricsExtractor {

    private static final String s_metrics_directory = "metrics";
    private static final String s_metrics_file = "metrics.csv";
    private static final String s_separator = ";";

    /**
     * @param homepage
     */
    public static void generate(final Path homepage) {

        final Path metricsDirectory = homepage.resolve(s_metrics_directory);
        final File metricsFile = metricsDirectory.resolve(s_metrics_file).toFile();

        final String metrics = extractMetrics(metricsDirectory);

        try (final FileOutputStream os = new FileOutputStream(metricsFile);
             final PrintWriter pw = new PrintWriter(os)) {
            pw.print(metrics);
            pw.flush();
        } catch (final Exception e) {
                ExitHelper.exit(e);
        }

        Logger.log(Logger.Level.INFO)
              .append(metricsFile)
              .append(" is generated")
              .submit();
    }

    private static String extractMetrics(final Path metricsDirectory) {

        final StringBuilder builder = new StringBuilder();

        builder.append("Date" + s_separator +
                       "Hits" + s_separator +
                       "Files" + s_separator +
                       "Pages" + s_separator +
                       "Visits\n");

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
                       builder.append(s_separator);
                       builder.append(extractNumberFromLine(line));
                    } else if (lineNumber == 51) { //files
                       builder.append(s_separator);
                       builder.append(extractNumberFromLine(line));
                    } else if (lineNumber == 53) { // pages
                       builder.append(s_separator);
                       builder.append(extractNumberFromLine(line));
                    } else if (lineNumber == 55) { // visits
                       builder.append(s_separator);
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

    private static int extractNumberFromLine(final String line) {
        final String numberAsString = line.replaceAll(".*<B>", "").replaceAll("</B>.*", "");
        return Integer.parseInt(numberAsString);
    }
}
