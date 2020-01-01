package data.jsongenerator;

import java.util.Optional;

import utils.xmlparsing.DurationData;
import utils.xmlparsing.LinkData;

public class Link extends LinkData implements Comparable<Link> {
	
	private final String a_sortingKey;

	public Link(final Article article,
			    final String title,
			    final String[] subtitles,
				final String url,
				final Optional<String> status,
				final Optional<String> protection,
				final String[] formats,
				final String[] languages,
				final Optional<DurationData> duration) {
	    super(title, subtitles, url, status, protection, formats, languages, duration);
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
		String str = name.substring(i + 1);
		while (str.codePointAt(0) == "/".codePointAt(0)) {
			str = str.substring(1);
		}
		return str;
	}

	@Override
	public int compareTo(final Link o) {
		return getSortingKey().compareToIgnoreCase(o.getSortingKey());
	}
}
