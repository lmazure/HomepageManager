package utils.xmlparsing;

import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Optional;

public class ArticleData {

    private final Optional<TemporalAccessor> _date;
    private final List<AuthorData> _authors;
    private final List<LinkData> _links;

    public ArticleData(final Optional<TemporalAccessor> date,
                       final List<AuthorData> authors,
                       final List<LinkData> links) {
        _date = date;
        _authors = authors;
        _links = links;
    }

    public Optional<TemporalAccessor> getDate() {
        return _date;
    }

    public List<AuthorData> getAuthors() {
        return _authors;
    }

    public List<LinkData> getLinks() {
        return _links;
    }
}
