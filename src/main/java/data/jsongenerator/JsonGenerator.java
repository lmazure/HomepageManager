package data.jsongenerator;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import utils.Logger;
import utils.XmlParsingException;

public class JsonGenerator {

    private final Parser parser;
    private final JsonWriter reporter;

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
        reporter = new JsonWriter(articleFactory, authorFactory, keywordFactory);
    }

    /**
     * @param args
     * @throws XmlParsingException 
     */
    public static void generate(final Path homepage,
                                final List<Path> files) {

        final JsonGenerator main = new JsonGenerator();

        final String homepagePath = homepage.toString();

        // parse the XML files
        try {
            for (final Path file: files) {
                main.scanFile(file.toFile());
            }
            main.scanPersonFile(new File(homepagePath + File.separator + s_linksDirectoryFileName + File.separator + s_personFileName));
        } catch (final XmlParsingException e) {
            Logger.log(Logger.Level.ERROR)
            .append("Failed to parse the XML files")
            .append(e)
            .submit();
        }

        // generate content files
        main.generateReports(homepagePath);

        Logger.log(Logger.Level.INFO)
              .append("done! ")
              .submit();
    }

    /**
     * @param f
     * @throws XmlParsingException 
     */
    private void scanFile(final File f) throws XmlParsingException {
        parser.parse(f);
    }

    private void scanPersonFile(final File f) throws XmlParsingException {
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
