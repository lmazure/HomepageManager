package data.jsongenerator2;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Laurent
 *
 */
public class JsonGenerator2 {

	private final ArticleFactory2 a_articleFactory;
	private final LinkFactory2 a_linkFactory;
	private final AuthorFactory2 a_authorFactory;
	private final Parser2 a_parser;
	private final Reporter2 a_reporter;

    final static private String s_linksDirectoryFileName = "links";
	final static private String s_tableDirectoryFileName = "content";
    final static private String s_personFileName = "persons.xml";
    final static private String s_shortArticleJsonFileName = "article2.json";
    final static private String s_shortAuthorJsonFileName = "author2.json";

	/**
	 * 
	 */
	public JsonGenerator2() {
		a_articleFactory = new ArticleFactory2();
		a_authorFactory = new AuthorFactory2();
		a_linkFactory = new LinkFactory2();
		a_parser = new Parser2(a_articleFactory, a_linkFactory, a_authorFactory);
		a_reporter = new Reporter2(a_articleFactory, a_authorFactory);
	}

	/**
	 * @param args
	 */
	public static void generate(final Path homepage,
	                            final List<Path> files) {
		
		final JsonGenerator2 main = new JsonGenerator2();

		final String homepagePath = homepage.toString();
		
		// parse the XML files
		for (final Path file: files) {
		    main.scanFile(file.toFile());
		}
		main.scanPersonFile(new File(homepagePath + File.separator + s_linksDirectoryFileName + File.separator + s_personFileName));

		// generate content files
		main.generateReports(homepagePath);

		System.out.println("done! ");
	}

	/**
	 * @param f
	 */
	private void scanFile(final File f) {
        a_parser.parse(f);
	}
	
	private void scanPersonFile(final File f) {
	    a_parser.parsePersonFile(f);
	}

	/**
	 * @param rootDirectory
	 */
	private void generateReports(final String homepagePath) {

        final String authorJsonFileName = s_tableDirectoryFileName + File.separator + s_shortAuthorJsonFileName;
        final String articleJsonFileName = s_tableDirectoryFileName + File.separator + s_shortArticleJsonFileName;

        a_reporter.generateAuthorJson(new File(homepagePath), authorJsonFileName);
        a_reporter.generateArticleJson(new File(homepagePath), articleJsonFileName);
	}
}
