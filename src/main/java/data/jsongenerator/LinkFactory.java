package data.jsongenerator;

import java.util.Arrays;
import java.util.HashSet;

import utils.xmlparsing.LinkData;

public class LinkFactory {

	private final HashSet<Link> a_links;
	
	public LinkFactory() {
		a_links = new HashSet<Link>();
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
		a_links.add(link);
		return link;
	}
	
	/**
	 * @return sorted list of links
	 */
	public Link[] getLinks() {
		final Link a[] = a_links.toArray(new Link[0]);
		Arrays.sort(a);
		return a;
	}
}
