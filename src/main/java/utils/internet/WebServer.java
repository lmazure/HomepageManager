package utils.internet;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpHandler;
import java.nio.file.Path;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;

import utils.ExitHelper;
import utils.Logger;

/**
 * Management of the local Web Server
 *
 */
public class WebServer {

    public static void start(final Path homepagePath) {
        final InetSocketAddress address = new InetSocketAddress(80);
        final HttpHandler handler = SimpleFileServer.createFileHandler(homepagePath);
        final Filter filter = SimpleFileServer.createOutputFilter(System.out, SimpleFileServer.OutputLevel.VERBOSE);
        try {
            final HttpServer server = HttpServer.create(address,
                                                        10,
                                                        "/",
                                                        handler,
                                                        filter);
            server.start();
        } catch (final IOException e) {
            ExitHelper.exit("Failed to create Web Server", e);
        }
        Logger.log(Logger.Level.INFO)
              .append("server is started")
              .submit();
    }
}
