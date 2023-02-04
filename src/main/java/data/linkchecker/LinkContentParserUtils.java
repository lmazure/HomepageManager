package data.linkchecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import utils.internet.HtmlHelper;
import utils.xmlparsing.AuthorData;

/**
 * Helper class for the LinkDataExtractors
 *
 */
public class LinkContentParserUtils {

    /**
     * Extract an author names from a string
     * @param str string
     * @return extracted author name
     * @throws ContentParserException failure to extract an author name from the string
     */
    public static AuthorData getAuthor(final String str) throws ContentParserException {
        final String[] nameParts = HtmlHelper.cleanContent(str).split(" ");
        if (nameParts.length == 2) {
            return new AuthorData(Optional.empty(),
                                  Optional.of(nameParts[0]),
                                  Optional.empty(),
                                  Optional.of(nameParts[1]),
                                  Optional.empty(),
                                  Optional.empty());
        }
        if (nameParts.length == 1) {
            return new AuthorData(Optional.empty(),
                                  Optional.empty(),
                                  Optional.empty(),
                                  Optional.empty(),
                                  Optional.empty(),
                                  Optional.of(nameParts[0]));
        }
        if (nameParts.length == 3) {
            final String upperMiddleName = nameParts[1].toUpperCase();
            if (upperMiddleName.equals("DE") ||
                upperMiddleName.equals("VON") ||
                upperMiddleName.equals("VAN")) {
                return new AuthorData(Optional.empty(),
                                      Optional.of(nameParts[0]),
                                      Optional.empty(),
                                      Optional.of(nameParts[1] + " " + nameParts[2]),
                                      Optional.empty(),
                                      Optional.empty());
            }
            return new AuthorData(Optional.empty(),
                                  Optional.of(nameParts[0]),
                                  Optional.of(nameParts[1]),
                                  Optional.of(nameParts[2]),
                                  Optional.empty(),
                                  Optional.empty());
        }

        throw new ContentParserException("Failed to parse author name (author name has " + nameParts.length + " parts)");
    }

    /**
     * Extract author names from a string
     * @param str string
     * @return extracted author names
     * @throws ContentParserException failure to extract author names from the string
     */
    public static List<AuthorData> getAuthors(final String str) throws ContentParserException {
        final List<AuthorData> authorList = new ArrayList<>();
        final String[] splits = str.split(", and | and |, ");
        for (final String s: splits) {
            authorList.add(LinkContentParserUtils.getAuthor(s));
        }
        return authorList;
    }
}
