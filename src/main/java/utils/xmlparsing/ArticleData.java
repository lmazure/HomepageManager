package utils.xmlparsing;

import java.util.List;
import java.util.Optional;

public class ArticleData {

    private final Optional<DateData> _dateData;
    private final List<AuthorData> _authors;
    private final List<LinkData> _links;

    public ArticleData(final Optional<DateData> dateData,
                       final List<AuthorData> authors,
                       final List<LinkData> links) {
        _dateData = dateData;
        _authors = authors;
        _links = links;
    }

    public Optional<DateData> getDate() {
        return _dateData;
    }

    public List<AuthorData> getAuthors() {
        return _authors;
    }

    public List<LinkData> getLinks() {
        return _links;
    }

}
