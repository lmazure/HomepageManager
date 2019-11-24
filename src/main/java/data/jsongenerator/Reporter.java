package data.jsongenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Laurent
 *
 */
public class Reporter {

	private final ArticleFactory a_articleFactory;
	private final AuthorFactory a_authorFactory;

	/**
	 * @param articleFactory
	 * @param linkFactory
	 * @param authorFactory
	 */
	public Reporter(
	    final ArticleFactory articleFactory,
	    final AuthorFactory authorFactory) {
	    
		a_articleFactory = articleFactory;
		a_authorFactory = authorFactory;
	}

	   /**
     * @param root
     * @param pageName
     */
    public void generateAuthorJson(final File root, final String pageName) {

        final String rootFileName = root.getAbsolutePath();
        final File f = new File(rootFileName + File.separator + pageName);
        f.delete();

        try {
            final Author authors[] = a_authorFactory.getAuthors();
            final OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(f),Charset.forName("UTF-8").newEncoder());
            out.write("{\n  \"authors\" : [");
            for (int i = 0; i < authors.length; i++) {
                final Author author = authors[i];
                boolean isAComponentWritten = false; 
                if (i != 0) {
                    out.write(",");                    
                }
                out.write("\n    {");
                if (author.getNamePrefix() != null) {
                    out.write("\n      \"namePrefix\" : \"" + jsonEscape(author.getNamePrefix()) +"\"");
                    isAComponentWritten = true;
                }
                if (author.getFirstName() != null) {
                    if (isAComponentWritten) {
                        out.write(",");
                    }
                    out.write("\n      \"firstName\" : \"" + jsonEscape(author.getFirstName()) +"\"");
                    isAComponentWritten = true;
                }
                if (author.getMiddleName() != null) {
                    if (isAComponentWritten) {
                        out.write(",");
                    }
                    out.write("\n      \"middleName\" : \"" + jsonEscape(author.getMiddleName()) +"\"");
                    isAComponentWritten = true;
                }
                if (author.getLastName() != null) {
                    if (isAComponentWritten) {
                        out.write(",");
                    }
                    out.write("\n      \"lastName\" : \"" + jsonEscape(author.getLastName()) +"\"");
                    isAComponentWritten = true;
                }
                if (author.getNameSuffix() != null) {
                    if (isAComponentWritten) {
                        out.write(",");
                    }
                    out.write("\n      \"nameSuffix\" : \"" + jsonEscape(author.getNameSuffix()) +"\"");
                    isAComponentWritten = true;
                }
                if (author.getGivenName() != null) {
                    if (isAComponentWritten) {
                        out.write(",");
                    }
                    out.write("\n      \"givenName\" : \"" + author.getGivenName() +"\"");
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
            out.close();
        } catch (final IOException e) {
            System.out.println("Failed to write file " + f.getAbsolutePath());
            e.printStackTrace();
        }

        System.out.println(f.getPath() + " is created");
    }

    
    /**
      * @param root
      * @param pageName
      */
     public void generateArticleJson(final File root, final String pageName) {
    
         final String rootFileName = root.getAbsolutePath();
         final File f = new File(rootFileName + File.separator + pageName);
         f.delete();

         final Author authors[] = a_authorFactory.getAuthors();
         final HashMap<Author, Integer> authorIndexes = new HashMap<>();
         for (int i = 0; i < authors.length; i++) {
             authorIndexes.put(authors[i], i);
         }
         
         try {
             final Article articles[] = a_articleFactory.getArticles();
  			 Arrays.sort(articles, new ArticleComparator());
             //final BufferedWriter out = new BufferedWriter(new FileWriter(f));
             final OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(f),Charset.forName("UTF-8").newEncoder());
             out.write("{\n  \"articles\" : [");
             for (int i = 0; i < articles.length; i++) {
                 final Article article = articles[i];
                 if (i != 0) {
                     out.write(",");                    
                 }
                 out.write("\n    {");
                 printLinks(out, article.getLinks());
                 out.write(",");
                 if (article.getDateYear() != null) {
                     out.write("\n      \"date\" : [" + article.getDateYear());
                     if (article.getDateMonth() != null) {
                         out.write(", " + article.getDateMonth());
                         if (article.getDateDay() != null) {
                             out.write(", " + article.getDateDay());
                         }
                     }
                     out.write("],");
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
             out.close();
         } catch (final IOException e) {
             System.out.println("Failed to write file " + f.getAbsolutePath());
             e.printStackTrace();
         }
    
         System.out.println(f.getPath() + " is created");
     }

    private void printLinks(final OutputStreamWriter out, final Link[] links) throws IOException {
        out.write("\n      \"links\" : [");
        for (int j = 0; j < links.length; j++) {
            final Link link = links[j];
            if (j != 0) {
                out.write(", ");
            }
            out.write("\n        {\n          \"url\" : \"" + jsonEscape(link.getURL()) + "\",\n");                         
            out.write("          \"title\" : \"" + jsonEscape(link.getTitle()) + "\",\n");
            if (link.getSubtitle() != null) {
                out.write("          \"subtitle\" : \"" + jsonEscape(link.getSubtitle()) + "\",\n");                         
            }
            if (link.getDurationSecond() != null) {
                out.write("          \"duration\" : [");
                if (link.getDurationMinute() != null) {
                    if (link.getDurationHour() != null) {
                        out.write(link.getDurationHour() + ", ");
                    }
                    out.write(link.getDurationMinute() + ", ");
                }
                out.write(link.getDurationSecond() + "],\n");
            }
            if (link.getStatus() != null) {
                out.write("          \"status\" : \"" + link.getStatus() + "\",\n");                         
            }
            if (link.getProtection() != null) {
                out.write("          \"protection\" : \"" + link.getProtection() + "\",\n");                         
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
