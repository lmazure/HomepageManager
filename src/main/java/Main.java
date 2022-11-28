import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import ui.FileTable;
import utils.ExitHelper;
import utils.internet.WebServer;

/**
 * Top level class
 *
 */
public class Main {

    private static final String enableInternetAccessOption  = "-enableInternetAccess";
    private static final String disableInternetAccessOption = "-disableInternetAccess";
    private static final String enableServer = "-serve";

    /**
     * Entry point of the program
     *
     * @param args command line parameters
     */
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
            WebServer.start(homepagePath, tmpPath);
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
}