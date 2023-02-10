package fr.mazure.homepagemanager.data.jsongenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import fr.mazure.homepagemanager.utils.Logger;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkProtection;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;

/**
 *
 */
public class JsonWriter {

    private final ArticleFactory _articleFactory;
    private final AuthorFactory _authorFactory;
    private final KeywordFactory _keywordFactory;

    /**
     * @param articleFactory
     * @param authorFactory
     * @param keywordFactory
     */
    public JsonWriter(final ArticleFactory articleFactory,
                      final AuthorFactory authorFactory,
                      final KeywordFactory keywordFactory) {

        _articleFactory = articleFactory;
        _authorFactory = authorFactory;
        _keywordFactory = keywordFactory;
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

        try (final OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(f), Charset.forName("UTF-8").newEncoder())) {
            final Author authors[] = _authorFactory.getAuthors();
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
            out.flush();
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
      * @param fileName
      */
     public void generateArticleJson(final File root,
                                     final String fileName) {

         final String rootFileName = root.getAbsolutePath();
         final File f = new File(rootFileName + File.separator + fileName);
         f.delete();

         final Author authors[] = _authorFactory.getAuthors();
         final HashMap<Author, Integer> authorIndexes = new HashMap<>();
         for (int i = 0; i < authors.length; i++) {
             authorIndexes.put(authors[i], Integer.valueOf(i));
         }

         try (final OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(f), Charset.forName("UTF-8").newEncoder())) {
             final Article articles[] = _articleFactory.getArticles();
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
                         out.write(String.format("%02d", Integer.valueOf(date.get(ChronoField.MONTH_OF_YEAR))));
                         if (date.isSupported(ChronoField.DAY_OF_MONTH)) {
                             out.write(String.format("%02d", Integer.valueOf(date.get(ChronoField.DAY_OF_MONTH))));
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
             out.flush();
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
     * @param fileName
     */
    public void generateKeywordJson(final File root,
                                     final String fileName) {

        final String rootFileName = root.getAbsolutePath();
        final File f = new File(rootFileName + File.separator + fileName);
        f.delete();

        final Article[] articles = _articleFactory.getArticles();
        final HashMap<Article, Integer> articleIndexes = new HashMap<>();
        Arrays.sort(articles, new ArticleComparator());
        for (int i = 0; i < articles.length; i++) {
            articleIndexes.put(articles[i], Integer.valueOf(i));
        }

        final Author authors[] = _authorFactory.getAuthors();
        final HashMap<Author, Integer> authorIndexes = new HashMap<>();
        for (int i = 0; i < authors.length; i++) {
            authorIndexes.put(authors[i], Integer.valueOf(i));
        }

        try (final OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(f), Charset.forName("UTF-8").newEncoder())) {
            final Keyword keywords[] = _keywordFactory.getKeywords();
            Arrays.sort(keywords, new KeywordComparator());
            out.write("{\n  \"keywords\" : [");
            for (int i = 0; i < keywords.length; i++) {
                final Keyword keyword = keywords[i];
                if (i != 0) {
                    out.write(",");
                }
                out.write("\n    {");
                 out.write("\n      \"id\" : \"" + jsonEscape(keyword.getId()) + "\"");
                if (keyword.getLinks().length > 0) {
                    out.write(",");
                    printLinks(out, keyword.getLinks());
                }
                if (keyword.getArticles().isEmpty()) {
                    Logger.log(Logger.Level.WARN).append("No article for keyword \"" + keyword.getId() + "\"");
                }
                out.write(",\n      \"articleIndexes\" : [");
                boolean first = true;
                for (final Article article: keyword.getArticles()) {
                    if (!first) {
                        out.write(", ");
                    }
                    out.write(articleIndexes.get(article).toString());
                    first = false;
                }
                out.write("]\n    }");
            }
            out.write("\n  ]\n}");
            out.flush();
        } catch (final IOException e) {
            Logger.log(Logger.Level.ERROR).append("Failed to write file ")
                                          .append(f)
                                          .append(e)
                                          .submit();
        }

        Logger.log(Logger.Level.INFO).append(f)
                                     .append(" is created")
                                     .submit();
    }

    private static void printLinks(final OutputStreamWriter out,
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
                out.write("          \"status\" : \"" + formatStatus(link.getStatus().get()) + "\",\n");
            }
            if (link.getProtection().isPresent()) {
                out.write("          \"protection\" : \"" + formatProtection(link.getProtection().get()) + "\",\n");
            }
            out.write("          \"formats\" : [");
            for (int k = 0; k < link.getFormats().length; k++) {
                if (k != 0) {
                    out.write(", ");
                }
                out.write("\"" + formatFormat(link.getFormats()[k]) + "\"");
            }
            out.write("],\n");
            out.write("          \"languages\" : [");
            for (int k = 0; k < link.getLanguages().length; k++) {
                if (k != 0) {
                    out.write(", ");
                }
                out.write("\"" + formatLanguage(link.getLanguages()[k]) + "\"");
            }
            out.write("]\n        }");
        }
        out.write("\n      ]");
    }

    private static String jsonEscape(final String str) {
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"");
    }

    private static String formatStatus(final LinkStatus status) {
        switch (status) {
            case DEAD:
                return "dead";
            case OBSOLETE:
                return "obsolete";
            case ZOMBIE:
                return "zombie";
            default:
                throw new UnsupportedOperationException("Illegal status value (" + status + ")");
        }
    }

    private static String formatProtection(final LinkProtection protection) {
        switch (protection) {
            case FREE_REGISTRATION:
                return "free_registration";
            case PAYED_REGISTRATION:
                return "payed_registration";
            default:
                throw new UnsupportedOperationException("Illegal protection value (" + protection + ")");
        }
    }

    private static String formatFormat(final LinkFormat format) {
        switch (format) {
        case FLASH:
            return "Flash";
        case FLASH_VIDEO:
            return "Flash Video";
        case HTML:
            return "HTML";
        case MP3:
            return "MP3";
        case MP4:
            return "MP4";
        case PDF:
            return "PDF";
        case POSTSCRIPT:
            return "PostScript";
        case POWERPOINT:
            return "PowerPoint";
        case REALMEDIA:
            return "RealMedia";
        case TXT:
            return "txt";
        case WINDOWS_MEDIA_PLAYER:
            return "Windows Media Player";
        case WORD:
            return "Word";
        default:
            throw new UnsupportedOperationException("Illegal format value (" + format + ")");
        }
    }

    private static String formatLanguage(final Locale language) {
        if (language == Locale.FRENCH) {
            return "fr";
        }
        if (language == Locale.ENGLISH) {
            return "en";
        }
        throw new UnsupportedOperationException("Illegal language value (" + language + ")");
    }
}
