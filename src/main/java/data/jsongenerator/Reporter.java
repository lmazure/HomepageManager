package data.jsongenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.HashMap;

import utils.Logger;

public class Reporter {

	private final ArticleFactory a_articleFactory;
	private final AuthorFactory a_authorFactory;

	public Reporter(final ArticleFactory articleFactory,
	                final AuthorFactory authorFactory) {
	    
		a_articleFactory = articleFactory;
		a_authorFactory = authorFactory;
	}

	   /**
     * @param root
     * @param pageName
     */
    public void generateAuthorJson(final File root,
                                   final String pageName) {

        final String rootFileName = root.getAbsolutePath();
        final File f = new File(rootFileName + File.separator + pageName);
        f.delete();

        try (final OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(f),Charset.forName("UTF-8").newEncoder())) {
            final Author authors[] = a_authorFactory.getAuthors();
            out.write("{\n  \"authors\" : [");
            for (int i = 0; i < authors.length; i++) {
                final Author author = authors[i];
                boolean isAComponentWritten = false; 
                if (i != 0) {
                    out.write(",");                    
                }
                out.write("\n    {");
                if (author.getNamePrefix().isPresent()) {
                    out.write("\n      \"namePrefix\" : \"" + jsonEscape(author.getNamePrefix().get()) + "\"");
                    isAComponentWritten = true;
                }
                if (author.getFirstName().isPresent()) {
                    if (isAComponentWritten) {
                        out.write(",");
                    }
                    out.write("\n      \"firstName\" : \"" + jsonEscape(author.getFirstName().get()) + "\"");
                    isAComponentWritten = true;
                }
                if (author.getMiddleName().isPresent()) {
                    if (isAComponentWritten) {
                        out.write(",");
                    }
                    out.write("\n      \"middleName\" : \"" + jsonEscape(author.getMiddleName().get()) + "\"");
                    isAComponentWritten = true;
                }
                if (author.getLastName().isPresent()) {
                    if (isAComponentWritten) {
                        out.write(",");
                    }
                    out.write("\n      \"lastName\" : \"" + jsonEscape(author.getLastName().get()) + "\"");
                    isAComponentWritten = true;
                }
                if (author.getNameSuffix().isPresent()) {
                    if (isAComponentWritten) {
                        out.write(",");
                    }
                    out.write("\n      \"nameSuffix\" : \"" + jsonEscape(author.getNameSuffix().get()) + "\"");
                    isAComponentWritten = true;
                }
                if (author.getGivenName().isPresent()) {
                    if (isAComponentWritten) {
                        out.write(",");
                    }
                    out.write("\n      \"givenName\" : \"" + jsonEscape(author.getGivenName().get()) + "\"");
                    isAComponentWritten = true;
                }
                if (author.getLinks().length > 0) {
                    if (isAComponentWritten) {
                        out.write(",");
                    }
                    printLinks(out, author.getLinks());
                    isAComponentWritten = true;
                }
                out.write("\n    }");
            }
            out.write("\n  ]\n}");
        } catch (final IOException e) {
        	Logger.log(Logger.Level.ERROR)
        	      .append("Failed to write file ")
        	      .append(f)
			      .append(e)
        	      .submit();
        }

        Logger.log(Logger.Level.INFO)
              .append(f)
              .append(" is created")
              .submit();
    }

    
    /**
      * @param root
      * @param pageName
      */
     public void generateArticleJson(final File root,
    		                         final String pageName) {
    
         final String rootFileName = root.getAbsolutePath();
         final File f = new File(rootFileName + File.separator + pageName);
         f.delete();

         final Author authors[] = a_authorFactory.getAuthors();
         final HashMap<Author, Integer> authorIndexes = new HashMap<>();
         for (int i = 0; i < authors.length; i++) {
             authorIndexes.put(authors[i], i);
         }
         
         try (final OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(f), Charset.forName("UTF-8").newEncoder())) {
             final Article articles[] = a_articleFactory.getArticles();
  			 Arrays.sort(articles, new ArticleComparator());
             out.write("{\n  \"articles\" : [");
             for (int i = 0; i < articles.length; i++) {
                 final Article article = articles[i];
                 if (i != 0) {
                     out.write(",");                    
                 }
                 out.write("\n    {");
                 printLinks(out, article.getLinks());
                 out.write(",");
                 if (article.getDateData().isPresent()) {
                	 final TemporalAccessor date = article.getDateData().get();
                     out.write("\n      \"date\" : " + date.get(ChronoField.YEAR));
                     if (date.isSupported(ChronoField.MONTH_OF_YEAR)) {
                         out.write(String.format("%02d",date.get(ChronoField.MONTH_OF_YEAR)));
                         if (date.isSupported(ChronoField.DAY_OF_MONTH)) {
                             out.write(String.format("%02d",date.get(ChronoField.DAY_OF_MONTH)));
                         }
                     }
                     out.write(",");
                 }
                 if (article.getAuthors().length != 0) {
                     out.write("\n      \"authorIndexes\" : [");
                     for (int j = 0; j < article.getAuthors().length; j++) {
                         if (j != 0) {
                             out.write(", ");
                         }
                         out.write(authorIndexes.get(article.getAuthors()[j]).toString());                         
                     }
                     out.write("],");
                 }
                 final String page = article.getReferringPage()
                                            .getAbsolutePath()
                                            .substring(rootFileName.length() + 1)
                                            .replace(File.separatorChar, '/')
                                            .replaceFirst("\\.xml$", ".html");
                 out.write("\n      \"page\" : \"" + page +"\"\n    }");
             }
             out.write("\n  ]\n}");
         } catch (final IOException e) {
        	 Logger.log(Logger.Level.ERROR)
        	       .append("Failed to write file ")
                   .append(f)
 			       .append(e)
                   .submit();
         }
    
         Logger.log(Logger.Level.INFO)
               .append(f)
               .append(" is created")
               .submit();
     }

    private void printLinks(final OutputStreamWriter out,
    		                final Link[] links) throws IOException {
        out.write("\n      \"links\" : [");
        for (int i = 0; i < links.length; i++) {
            final Link link = links[i];
            if (i != 0) {
                out.write(", ");
            }
            out.write("\n        {\n          \"url\" : \"" + jsonEscape(link.getUrl()) + "\",\n");                         
            out.write("          \"title\" : \"" + jsonEscape(link.getTitle()) + "\",\n");
            if (link.getSubtitles().length > 0) {
                out.write("          \"subtitle\" : [");                         
                for (int k = 0; k < link.getSubtitles().length; k++) {
                    if (k != 0) {
                        out.write(", ");
                    }
                    out.write("\"" + jsonEscape(link.getSubtitles()[k]) + "\"");                                                  
                }
                out.write("],\n");                         
            }
            if (link.getDuration().isPresent()) {
                out.write("          \"duration\" : " + link.getDuration().get().getSeconds() + ",\n");
            }
            if (link.getStatus().isPresent()) {
                out.write("          \"status\" : \"" + link.getStatus().get() + "\",\n");                         
            }
            if (link.getProtection().isPresent()) {
                out.write("          \"protection\" : \"" + link.getProtection().get() + "\",\n");                         
            }
            out.write("          \"formats\" : [");                         
            for (int k = 0; k < link.getFormats().length; k++) {
                if (k != 0) {
                    out.write(", ");
                }
                out.write("\"" + link.getFormats()[k] + "\"");                                                  
            }
            out.write("],\n");                         
            out.write("          \"languages\" : [");                         
            for (int k = 0; k < link.getLanguages().length; k++) {
                if (k != 0) {
                    out.write(", ");
                }
                out.write("\"" + link.getLanguages()[k] + "\"");                                                  
            }
            out.write("]\n        }");                         
        }
        out.write("\n      ]");
    }
     
    static private String jsonEscape(final String str) {
        return str.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
