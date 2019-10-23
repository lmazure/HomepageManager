import ui.FileTable;

import java.nio.file.Path;
import java.nio.file.Paths;

import utils.ExitHelper;

public class Main {

    public static void main(final String[] args) {
        
        if (args.length != 2) {
            ExitHelper.exit("Syntax: HomepageManager <homepage directory> <tmp directory>");
        }
 
        final Path homepagePath = Paths.get(args[0]);
        final Path tmpPath = Paths.get(args[1]);
        final FileTable table = new FileTable();
        table.display(homepagePath, tmpPath);
    }
}