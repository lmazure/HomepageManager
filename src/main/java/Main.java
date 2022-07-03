import ui.FileTable;

import java.nio.file.Path;
import java.nio.file.Paths;

import utils.ExitHelper;

public class Main {

    private static final String enableInternetAccessOption  = "-enableInternetAccess";
    private static final String disableInternetAccessOption = "-disableInternetAccess";

    public static void main(final String[] args) {

        if ((args.length < 2) || (args.length > 3)) {
            exitOnSyntaxError();
        }

        boolean internetAccessIsEnabled = false;

        if (args.length == 3) {
            if (args[0].equals(enableInternetAccessOption)) {
                internetAccessIsEnabled = true;
            } else if (args[0].equals(disableInternetAccessOption)) {
                internetAccessIsEnabled = false;
            } else {
                exitOnSyntaxError();
            }
        }

        final Path homepagePath = Paths.get(args[args.length - 2]);
        final Path tmpPath = Paths.get(args[args.length - 1]);
        FileTable.display(homepagePath, tmpPath, internetAccessIsEnabled);
    }

    private static void exitOnSyntaxError() {
        ExitHelper.exit("Syntax: HomepageManager [" +
                        enableInternetAccessOption +
                        "|" +
                        disableInternetAccessOption +
                        "] <homepage directory> <tmp directory>");
    }
}