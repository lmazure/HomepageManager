package data.jsongenerator;

/**
 * @author Laurent
 *
 */
public class Link implements Comparable<Link> {
	
	private final Article a_article;
	private final String a_title;
	private final String a_subtitle;
	private final String a_URL;
	private final String a_status;
	private final String a_protection;
	private final String a_formats[];
	private final String a_languages[];
	private final Integer a_durationHour; 
	private final Integer a_durationMinute; 
	private final Integer a_durationSecond;
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
	 * @param hour
	 * @param minute
	 * @param second
	 */
	public Link(final Article article,
			    final String title,
			    final String subtitle,
				final String url,
				final String status,
				final String protection,
				final String[] formats,
				final String[] languages,
				final Integer hour,
				final Integer minute,
				final Integer second) {
		a_article = article;
		a_title = title;
		a_subtitle = subtitle;
		a_URL = url;
		a_status = status;
		a_protection = protection;
		a_formats = formats;
		a_languages = languages;
		a_durationHour = hour;
		a_durationMinute = minute;
		a_durationSecond = second;
		a_sortingKey = normalizeName(url);
	}

	/**
	 * @return the article
	 */
	public Article getArticle() {
		return a_article;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return a_title;
	}

	/**
	 * @return the subtitle
	 */
	public String getSubtitle() {
		return a_subtitle;
	}

	/**
	 * @return the URL
	 */
	public String getURL() {
		return a_URL;
	}

	/**
	 * @return the status (dead, alive...)
	 */
	public String getStatus() {
		return a_status;
	}

	/**
	 * @return the protection (firewall, free_registration...)
	 */
	public String getProtection() {
		return a_protection;
	}

	/**
	 * @return the formats
	 */
	public String[] getFormats() {
		return a_formats;
	}

	/**
	 * @return the Languages
	 */
	public String[] getLanguages() {
		return a_languages;
	}


	/**
	 * @return the duration hour
	 */
	public Integer getDurationHour() {
		return a_durationHour;
	}

	/**
	 * @return the duration minute
	 */
	public Integer getDurationMinute() {
		return a_durationMinute;
	}
	
	/**
	 * @return the duration second
	 */
	public Integer getDurationSecond() {
		return a_durationSecond;
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
