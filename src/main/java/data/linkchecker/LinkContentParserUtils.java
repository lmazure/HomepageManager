package data.linkchecker;

import java.util.Optional;

import utils.HtmlHelper;
import utils.xmlparsing.AuthorData;

public class LinkContentParserUtils {

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
        if (nameParts.length == 3) {
            return new AuthorData(Optional.empty(),
                                  Optional.of(nameParts[0]),
                                  Optional.of(nameParts[1]),
                                  Optional.of(nameParts[2]),
                                  Optional.empty(),
                                  Optional.empty());
        }
        throw new ContentParserException("Failed to parse author name (author name has " + nameParts.length + " parts)");
    }
}
