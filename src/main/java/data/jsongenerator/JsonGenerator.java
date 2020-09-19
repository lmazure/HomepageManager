package data.jsongenerator;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import utils.Logger;

public class JsonGenerator {

    private final Parser parser;
    private final Reporter reporter;

    final static private String s_linksDirectoryFileName = "links";
    final static private String s_tableDirectoryFileName = "content";
    final static private String s_personFileName = "persons.xml";
    final static private String s_shortArticleJsonFileName = "article.json";
    final static private String s_shortAuthorJsonFileName = "author.json";
    final static private String s_shortKeywordJsonFileName = "keyword.json";

    /**
     *
     */
    public JsonGenerator() {
        final ArticleFactory articleFactory = new ArticleFactory();
        final AuthorFactory authorFactory = new AuthorFactory();
        final LinkFactory linkFactory = new LinkFactory();
        final KeywordFactory keywordFactory = new KeywordFactory();
        parser = new Parser(articleFactory, linkFactory, authorFactory, keywordFactory);
        reporter = new Reporter(articleFactory, authorFactory, keywordFactory);
    }

    /**
     * @param args
     */
    public static void generate(final Path homepage,
                                final List<Path> files) {

        final JsonGenerator main = new JsonGenerator();

        final String homepagePath = homepage.toString();

        // parse the XML files
        for (final Path file: files) {
            main.scanFile(file.toFile());
        }
        main.scanPersonFile(new File(homepagePath + File.separator + s_linksDirectoryFileName + File.separator + s_personFileName));

        // generate content files
        main.generateReports(homepagePath);

        Logger.log(Logger.Level.INFO)
              .append("done! ")
              .submit();
    }

    /**
     * @param f
     */
    private void scanFile(final File f) {
        parser.parse(f);
    }

    private void scanPersonFile(final File f) {
        parser.parsePersonFile(f);
    }

    /**
     * @param rootDirectory
     */
    private void generateReports(final String homepagePath) {

        final String authorJsonFileName = s_tableDirectoryFileName + File.separator + s_shortAuthorJsonFileName;
        final String articleJsonFileName = s_tableDirectoryFileName + File.separator + s_shortArticleJsonFileName;
        final String keywordJsonFileName = s_tableDirectoryFileName + File.separator + s_shortKeywordJsonFileName;

        reporter.generateAuthorJson(new File(homepagePath), authorJsonFileName);
        reporter.generateArticleJson(new File(homepagePath), articleJsonFileName);
        reporter.generateKeywordJson(new File(homepagePath), keywordJsonFileName);
    }
}
