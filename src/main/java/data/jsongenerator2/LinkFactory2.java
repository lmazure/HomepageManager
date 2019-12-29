package data.jsongenerator2;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author Laurent
 *
 */
public class LinkFactory2 {

	private final HashSet<Link2> a_links;
	
	/**
	 * 
	 */
	public LinkFactory2() {
		a_links = new HashSet<Link2>();
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
	public Link2 newLink(final Article2 article,
			            final ParserLinkDto2 linkDto) {
		final Link2 link = new Link2(article,
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
	public Link2[] getLinks() {
		Link2 a[] = a_links.toArray(new Link2[0]);
		Arrays.sort(a);
		return a;
	}
}
