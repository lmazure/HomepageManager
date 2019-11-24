package data.jsongenerator;

import java.io.File;
import java.nio.file.Path;

/**
 * @author Laurent
 *
 */
public class JsonGenerator {

	private final ArticleFactory a_articleFactory;
	private final LinkFactory a_linkFactory;
	private final AuthorFactory a_authorFactory;
	private final Parser a_parser;
	private final Reporter a_reporter;

    final static private String s_linksDirectoryFileName = "links";
	final static private String s_tableDirectoryFileName = "content";
    final static private String s_personFileName = "persons.xml";
    final static private String s_shortArticleJsonFileName = "article.json";
    final static private String s_shortAuthorJsonFileName = "author.json";

	/**
	 * 
	 */
	public JsonGenerator() {
		a_articleFactory = new ArticleFactory();
		a_authorFactory = new AuthorFactory();
		a_linkFactory = new LinkFactory();
		a_parser = new Parser(a_articleFactory, a_linkFactory, a_authorFactory);
		a_reporter = new Reporter(a_articleFactory, a_authorFactory);
	}

	/**
	 * @param args
	 */
	public static void generate(final Path homepage) {
		
		final JsonGenerator main = new JsonGenerator();

		final String homepagePath = homepage.toString();
		
		// parse the XML files
		main.scanFiles(new File(homepagePath));
		main.scanPersonFile(new File(homepagePath + File.separator + s_linksDirectoryFileName + File.separator + s_personFileName));

		// generate content files
		main.generateReports(homepagePath);

		System.out.println("done! ");
	}

	/**
	 * @param f
	 */
	private void scanFiles(final File f) {
		if (f.isDirectory()) {
			final File[] list = f.listFiles();
			for (int i = 0; i < list.length; i++)
				scanFiles(list[i]);
		} else {
			if (f.getName().endsWith(".xml"))
				a_parser.parse(f);
		}
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
