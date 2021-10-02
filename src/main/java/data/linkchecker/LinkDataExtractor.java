package data.linkchecker;

import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Optional;

import utils.xmlparsing.AuthorData;

public interface LinkDataExtractor {

    public Optional<TemporalAccessor> getDate();

    public List<AuthorData> getAuthors();

    public List<ExtractedLinkData> getLinks();
}
