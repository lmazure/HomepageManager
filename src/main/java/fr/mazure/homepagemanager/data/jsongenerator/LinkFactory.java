package fr.mazure.homepagemanager.data.jsongenerator;

import java.util.Arrays;
import java.util.HashSet;

import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;

/**
 *
 */
public class LinkFactory {

    private final HashSet<Link> _links;

    /**
     * Constructor
     */
    public LinkFactory() {
        _links = new HashSet<>();
    }

    /**
     * Create a new link
     *
     * @param linkData the link data
     * @return the link
     */
    public Link newLink(final LinkData linkData) {
        final Link link = new Link(linkData.getTitle(),
                                   linkData.getSubtitles(),
                                   linkData.getUrl(),
                                   linkData.getStatus(),
                                   linkData.getProtection(),
                                   linkData.getFormats(),
                                   linkData.getLanguages(),
                                   linkData.getQuality(),
                                   linkData.getDuration(),
                                   linkData.getPublicationDate(),
                                   linkData.getFeed());
        _links.add(link);
        return link;
    }

    /**
     * Get the list of links
     *
     * @return the list of links
     */
    public Link[] getLinks() {
        final Link a[] = _links.toArray(new Link[0]);
        Arrays.sort(a);
        return a;
    }
}
