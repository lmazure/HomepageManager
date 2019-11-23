package data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;

import utils.ExitHelper;

public class RobottxtGenerator {
    
    static public void generate(final Path homepage,
                                final List<Path> files) {
        
        final File robottxt = homepage.resolve("robot.txt").toFile();
        
        try (final FileOutputStream os = new FileOutputStream(robottxt);
             final PrintWriter pw = new PrintWriter(os)) {
            pw.println("User-Agent: *");
            for (final Path file: files) {
                pw.println("Disallow: /" + homepage.relativize(file).toString().replace(File.separatorChar, '/'));
            }
            pw.println("Allow: /");
        } catch (final Exception e) {
                ExitHelper.exit(e);
        }
        
        System.out.println(robottxt.toString() + " is generated");
    }
}
