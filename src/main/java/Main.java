import ui.FileTable;

import java.nio.file.Path;
import java.nio.file.Paths;

import utils.ExitHelper;

public class Main {

    private static final String enableInternetAccessOption = "-enableInternetAccess";
    private static final String disableInternetAccessOption = "-disableInternetAccess";
    
    public static void main(final String[] args) {
        
        if ((args.length < 2) || args.length > 3) {
            exitOnErrorSyntax();
        }
 
        boolean internetAccessiSEnabled = false;
        
        if (args.length == 3) {
            if (args[0].equals(enableInternetAccessOption)) {
                internetAccessiSEnabled = true;
            } else if (args[0].equals(disableInternetAccessOption)){
                internetAccessiSEnabled = false;
            } else {
                exitOnErrorSyntax();
            }
        }
        
        final Path homepagePath = Paths.get(args[args.length - 2]);
        final Path tmpPath = Paths.get(args[args.length - 1]);
        final FileTable table = new FileTable();
        table.display(homepagePath, tmpPath, internetAccessiSEnabled);
    }
    
    private static void exitOnErrorSyntax() {
        ExitHelper.exit("Syntax: HomepageManager [-" +
                enableInternetAccessOption +
                "|" +
                disableInternetAccessOption +
                "] <homepage directory> <tmp directory>");        
    }
}