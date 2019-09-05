import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    final static private String s_markerFile = "google1b78f05130a6dbb0.html";

    public static void main(final String[] args) {
        
        if (args.length != 1) {
            ExitHelper.of().message("Syntax: HomepageManager <homepage directory>").exit();
        }
 
        final Main main = new Main();
        main.start(args[0]);
    }

    private void start(final String homepagePath) {

        if (!(new File(homepagePath + File.separator + s_markerFile)).exists()) {
            ExitHelper.of().message(homepagePath + " does not contain the homepage").exit();
        }

        try {
            new WatchDir(Paths.get(homepagePath)).ignoreDirectory(".svn")
                                                 .ignoreDirectory(".git")
                                                 .ignoreDirectory(".vscode")
                                                 .ignoreDirectory("node_modules")
                                                 .addFileWatcher(FileSystems.getDefault().getPathMatcher("glob:**/*.xml"), (Path p, WatchDir.Event e) -> System.out.println(p + " " + e))
                                                 .processEvents();
        } catch (final IOException e) {
            ExitHelper.of().exception(e).exit();
        }

        System.out.println("Done!");
    }

}
