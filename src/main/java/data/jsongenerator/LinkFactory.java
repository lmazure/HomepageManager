package data.jsongenerator;

import java.util.Arrays;
import java.util.HashSet;

import utils.xmlparsing.LinkData;

public class LinkFactory {

    private final HashSet<Link> _links;

    public LinkFactory() {
        _links = new HashSet<Link>();
    }

    public Link newLink(final Article article,
                        final LinkData linkData) {
        final Link link = new Link(article,
                                   linkData.getTitle(),
                                   linkData.getSubtitles(),
                                   linkData.getUrl(),
                                   linkData.getStatus(),
                                   linkData.getProtection(),
                                   linkData.getFormats(),
                                   linkData.getLanguages(),
                                   linkData.getDuration(),
                                   linkData.getPublicationDate());
        _links.add(link);
        return link;
    }

    /**
     * @return sorted list of links
     */
    public Link[] getLinks() {
        final Link a[] = _links.toArray(new Link[0]);
        Arrays.sort(a);
        return a;
    }
}
