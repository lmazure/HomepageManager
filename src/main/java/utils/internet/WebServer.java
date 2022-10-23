package utils.internet;

import java.net.InetSocketAddress;
import java.nio.file.Path;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;

import utils.Logger;

public class WebServer {

    public static void start(final Path homepagePath) {
        final HttpServer server = SimpleFileServer.createFileServer(new InetSocketAddress(80),
                                                                    homepagePath,
                                                                    SimpleFileServer.OutputLevel.VERBOSE);
        server.start();
        Logger.log(Logger.Level.INFO)
              .append("server is started")
              .submit();
    }
}
