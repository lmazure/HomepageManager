package utils.xmlparsing;

import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Optional;

/**
 * @param date 
 * @param authors 
 * @param links 
 *
 */
public record ArticleData(Optional<TemporalAccessor> date,
                          List<AuthorData> authors,
                          List<LinkData> links) {
    // EMPTY
}
