package fr.mazure.homepagemanager.data.jsongenerator;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import fr.mazure.homepagemanager.utils.Logger;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlParsingException;

/**
 *
 */
public class JsonGenerator {

    private final Parser parser;
    private final JsonWriter reporter;

    private static final String s_linksDirectoryFileName = "links";
    private static final String s_tableDirectoryFileName = "content";
    private static final String s_personFileName = "persons.xml";
    private static final String s_shortArticleJsonFileName = "article.json";
    private static final String s_shortAuthorJsonFileName = "author.json";
    private static final String s_shortKeywordJsonFileName = "keyword.json";

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
     * @param homepage
     * @param files
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
     * @param file
     * @throws XmlParsingException
     */
    private void scanFile(final File file) throws XmlParsingException {
        parser.parse(file);
    }

    private void scanPersonFile(final File file) throws XmlParsingException {
        parser.parsePersonFile(file);
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