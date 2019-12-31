package data.jsongenerator;

import java.util.Optional;

import utils.xmlparsing.DurationData;
import utils.xmlparsing.LinkData;

public class Link extends LinkData implements Comparable<Link> {
	
	private final String a_sortingKey;

	/**
	 * @param article
	 * @param title
	 * @param subtitle
	 * @param url
	 * @param status 
	 * @param protection 
	 * @param formats
	 * @param languages
	 * @param durationHour
	 * @param durationMinute
	 * @param durationSecond
	 */
	public Link(final Article article,
			    final String title,
			    final Optional<String> subtitle, // TODO should be String[]
				final String url,
				final Optional<String> status,
				final Optional<String> protection,
				final String[] formats,
				final String[] languages,
				final Optional<DurationData> duration) {
	    super(title, subtitle, url, status, protection, formats, languages, duration);
		a_sortingKey = normalizeName(url);
	}

	/**
	 * @return
	 */
	public String getSortingKey() {
		return a_sortingKey;
	}

	/**
	 * @param name name to be normalized
	 * @return normalized (i.e. usable for sorting) name
	 */
	static private String normalizeName(final String name) {
		final int i = name.indexOf(':');
		String str = name.substring(i+1);
		while ( str.codePointAt(0) == "/".codePointAt(0) ) {
			str = str.substring(1);
		}
		return str;
	}

	@Override
	public int compareTo(final Link o) {
		return getSortingKey().compareToIgnoreCase(o.getSortingKey());
	}
}
