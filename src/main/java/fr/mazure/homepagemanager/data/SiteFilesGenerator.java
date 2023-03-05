package fr.mazure.homepagemanager.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;

import fr.mazure.homepagemanager.utils.ExitHelper;
import fr.mazure.homepagemanager.utils.Logger;

/**
 * Class generating the files global to the site
 */
public class SiteFilesGenerator {

    private static final String s_domain = "mazure.fr"; // TODO this should not be hardcoded
    private static final String s_robottxt = "robot.txt";
    private static final String s_sitemap_directory = "sitemap"; // TODO this directory name also appears in DataOrchestrator
    private static final String s_sitemap = "sitemap.xml";

    /**
     * Generate the files
     * @param homepage path of the homepage directory
     * @param files list of the paths of all files
     */
    public static void generate(final Path homepage,
                                final List<Path> files) {
        generateRobottxt(homepage, files);
        generateSitemap(homepage, files);
    }

    private static void generateRobottxt(final Path homepage,
                                         final List<Path> files) {

        final File robottxt = homepage.resolve(s_robottxt).toFile();

        try (final FileOutputStream os = new FileOutputStream(robottxt);
             final PrintWriter pw = new PrintWriter(os)) {
            pw.println("User-Agent: *");
            pw.println("Sitemap: https://" + s_domain + "/" + s_sitemap_directory + "/" + s_sitemap);
            for (final Path file: files) {
                pw.println("Disallow: " + getXmlUrlFromFile(homepage,file));
            }
            pw.println("Allow: /");
            pw.flush();
        } catch (final Exception e) {
                ExitHelper.exit(e);
        }

        Logger.log(Logger.Level.INFO)
              .append(robottxt)
              .append(" is generated")
              .submit();
    }

    private static void generateSitemap(final Path homepage,
                                        final List<Path> files) {

        final Path sitemapDir = homepage.resolve(s_sitemap_directory);
        sitemapDir.toFile().mkdir();

        final File sitemap = sitemapDir.resolve(s_sitemap).toFile();

        try (final FileOutputStream os = new FileOutputStream(sitemap);
             final PrintWriter pw = new PrintWriter(os)) {
            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            pw.println("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
            for (final Path file: files) {
                pw.println("  <url>");
                pw.println("    <loc>https://" + s_domain + getHtmlUrlFromFile(homepage,file) + "</loc>");
                pw.println("  </url>");
            }
            pw.println("</urlset>");
            pw.flush();
        } catch (final Exception e) {
                ExitHelper.exit(e);
        }

        Logger.log(Logger.Level.INFO)
              .append(sitemap)
              .append(" is generated")
              .submit();
    }

    private static String getXmlUrlFromFile(final Path homepage,
                                            final Path file) {
        return "/" + homepage.relativize(file).toString().replace(File.separatorChar, '/');
    }

    private static String getHtmlUrlFromFile(final Path homepage,
                                             final Path file) {
        return getXmlUrlFromFile(homepage,file).replaceAll(".xml$", ".html");
    }
}
