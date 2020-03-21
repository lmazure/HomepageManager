package data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;

import utils.ExitHelper;
import utils.Logger;

public class SiteFilesGenerator {
    
    final static private String DOMAIN = "mazure.fr"; // TODO this should not be hardcoded
    final static private String ROBOTTXT = "robot.txt";
    final static private String SITEMAPDIR = "sitemap"; // TODO this directory name also appears in DataOrchestrator
    final static private String SITEMAP = "sitemap.xml";

    static public void generate(final Path homepage,
                                final List<Path> files) {
        generateRobottxt(homepage, files);
        generateSitemap(homepage, files);
    }
    
    static private void generateRobottxt(final Path homepage,
                                         final List<Path> files) {
        
        final File robottxt = homepage.resolve(ROBOTTXT).toFile();
        
        try (final FileOutputStream os = new FileOutputStream(robottxt);
             final PrintWriter pw = new PrintWriter(os)) {
            pw.println("User-Agent: *");
            pw.println("Sitemap: https://" + DOMAIN + "/" + SITEMAPDIR + "/" + SITEMAP);
            for (final Path file: files) {
                pw.println("Disallow: " + getXmlUrlFromFile(homepage,file));
            }
            pw.println("Allow: /");
        } catch (final Exception e) {
                ExitHelper.exit(e);
        }
        
        Logger.log(Logger.Level.INFO).append(robottxt.toString() + " is generated").submit();
    }

    
    static private void generateSitemap(final Path homepage,
                                        final List<Path> files) {
        
        final Path sitemapDir = homepage.resolve(SITEMAPDIR);
        sitemapDir.toFile().mkdir();
        
        final File sitemap = sitemapDir.resolve(SITEMAP).toFile();
        
        try (final FileOutputStream os = new FileOutputStream(sitemap);
             final PrintWriter pw = new PrintWriter(os)) {
            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            pw.println("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
            for (final Path file: files) {
                pw.println("  <url>");
                pw.println("    <loc>https://" + DOMAIN + getHtmlUrlFromFile(homepage,file) + "</loc>");
                pw.println("  </url>");
            }
            pw.println("</urlset>");
        } catch (final Exception e) {
                ExitHelper.exit(e);
        }
        
        Logger.log(Logger.Level.INFO).append(sitemap.toString() + " is generated").submit();
    }
    
    static private String getXmlUrlFromFile(final Path homepage,
                                            final Path file) {
        return "/" + homepage.relativize(file).toString().replace(File.separatorChar, '/');
    }

    static private String getHtmlUrlFromFile(final Path homepage,
                                             final Path file) {
        return getXmlUrlFromFile(homepage,file).replaceAll(".xml$", ".html");
    }
}
