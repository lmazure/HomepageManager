import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;

import ui.FileTable;
import utils.ExitHelper;
import utils.Logger;

public class Main {

    private static final String enableInternetAccessOption  = "-enableInternetAccess";
    private static final String disableInternetAccessOption = "-disableInternetAccess";
    private static final String enableServer = "-serve";

    public static void main(final String[] args) {

        if ((args.length < 2) || (args.length > 4)) {
            exitOnSyntaxError(Optional.empty());
        }

        boolean internetAccessIsEnabled = false;
        boolean serverIsEnabled = false;

        for (int i = 0; i < args.length - 2; i++) {
            final String arg = args[i];
            if (arg.equals(enableInternetAccessOption)) {
                internetAccessIsEnabled = true;
            } else if (arg.equals(disableInternetAccessOption)) {
                internetAccessIsEnabled = false;
            } else if (arg.equals(enableServer)) {
                serverIsEnabled = true;
            } else {
                exitOnSyntaxError(Optional.of(arg));
            }
        }

        final Path homepagePath = Paths.get(args[args.length - 2]);
        final Path tmpPath = Paths.get(args[args.length - 1]);
        if (serverIsEnabled) {
            startServer(homepagePath);
        }
        FileTable.display(homepagePath, tmpPath, internetAccessIsEnabled);
    }

    private static void exitOnSyntaxError(final Optional<String> unknownParameter) {
        final String message1 = unknownParameter.isPresent() ? ("Unknown parameter: " + unknownParameter.get() + " -- ")
                                                             : "";
        final String message2 = "Syntax: HomepageManager [" +
                                enableInternetAccessOption +
                                "|" +
                                disableInternetAccessOption +
                                "] [" +
                                enableServer +
                                "] <homepage directory> <tmp directory>";
        ExitHelper.exit(message1 + message2);
    }
    
    private static void startServer(final Path homepagePath) {
        final HttpServer server = SimpleFileServer.createFileServer(new InetSocketAddress(80), homepagePath, SimpleFileServer.OutputLevel.VERBOSE);
        server.start();
        Logger.log(Logger.Level.INFO)
              .append("server is sarted")
              .submit();
    }
}