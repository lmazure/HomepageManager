package fr.mazure.homepagemanager.utils.internet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpHandler;
import java.nio.file.Path;
import java.time.Instant;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;

import fr.mazure.homepagemanager.utils.ExitHelper;
import fr.mazure.homepagemanager.utils.FileHelper;
import fr.mazure.homepagemanager.utils.Logger;

/**
 * Management of the local Web Server
 *
 */
public class WebServer {

    private static final String s_log_directory_name = "webserver_logs";

    /**
     * Start the Web Server
     *
     * @param homepagePath path to the directory containing the pages
     * @param tmpPath path to the directory containing the temporary files and log files
     *
     */
    public static void start(final Path homepagePath,
                             final Path tmpPath) {
        final InetSocketAddress address = new InetSocketAddress(80);
        final HttpHandler handler = SimpleFileServer.createFileHandler(homepagePath);
        try {
            final File logFile = getLogFile(tmpPath);
            FileHelper.createParentDirectory(logFile.toPath());
            @SuppressWarnings("resource")
            final OutputStream out = new FileOutputStream(logFile);
            final Filter filter = SimpleFileServer.createOutputFilter(out, SimpleFileServer.OutputLevel.VERBOSE);
            try {
                final HttpServer server = HttpServer.create(address,
                                                            10,
                                                            "/",
                                                            handler,
                                                            filter);
                server.start();
            } catch (final IOException e1) {
                ExitHelper.exit("Failed to create Web Server", e1);
            }
        } catch (final FileNotFoundException e1) {
            ExitHelper.exit("Failed to create Web Server log file", e1);
        }
        Logger.log(Logger.Level.INFO)
              .append("server is started")
              .submit();
    }

    private static File getLogFile(final Path tmpPath) {
        final Instant timestamp = Instant.now();
        final String logFilename = timestamp.toString().replaceAll(":", ";") + ".log";
        return tmpPath.resolve(s_log_directory_name)
                      .resolve(logFilename)
                      .toFile();
    }
}
