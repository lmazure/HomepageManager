package data.jsongenerator;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author Laurent
 *
 */
public class LinkFactory {

	private final HashSet<Link> a_links;
	
	/**
	 * 
	 */
	public LinkFactory() {
		a_links = new HashSet<Link>();
	}
	
	/**
	 * @param article
	 * @param title
	 * @param subtitle 
	 * @param url
	 * @param status 
	 * @param protection 
	 * @param formats
	 * @param languages
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public Link newLink(final Article article,
			            final ParserLinkDto linkDto) {
		final Link link = new Link(article,
		                           linkDto.getTitle(),
		                           linkDto.getSubtitle(),
		                           linkDto.getUrl(),
		                           linkDto.getStatus(),
		                           linkDto.getProtection(),
		                           linkDto.getFormats(),
		                           linkDto.getLanguages(),
		                           linkDto.getDurationHour(), 
		                           linkDto.getDurationMinute(),
		                           linkDto.getDurationSecond());
		a_links.add(link);
		return link;
	}
	
	/**
	 * @return sorted list of links
	 */
	public Link[] getLinks() {
		Link a[] = a_links.toArray(new Link[0]);
		Arrays.sort(a);
		return a;
	}
}
