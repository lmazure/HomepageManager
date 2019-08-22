import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    final static private String s_markerFile = "google1b78f05130a6dbb0.html";

    public static void main(final String[] args) {
        
        if (args.length != 1) {
            System.err.println("Syntax: HomepageManager <homepage directory>");
            System.exit(1);             
        }
 
        final Main main = new Main();
        main.start(args[0]);
    }

    private void start(final String homepagePath) {

        if (!(new File(homepagePath + File.separator + s_markerFile)).exists()) {
            System.err.println(homepagePath + " does not contain the homepage");
            System.exit(1);             
        }

        try {
            new WatchDir(Paths.get(homepagePath)).ignoreDirectory(".svn")
                                                 .ignoreDirectory(".git")
                                                 .ignoreDirectory(".vscode")
                                                 .ignoreDirectory("node_modules")
                                                 .processEvents();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Done!");
    }

}
