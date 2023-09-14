package fr.mazure.homepagemanager.utils.xmlparsing;

import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Optional;

/**
 * Article data
 * @param date Date of the article
 * @param authors Authors of the article
 * @param links Links toward instances of the article
 *
 */
public record ArticleData(Optional<TemporalAccessor> date,
                          List<AuthorData> authors,
                          List<LinkData> links) {
    // EMPTY
}
