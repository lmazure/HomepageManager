package data.linkchecker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.ExitHelper;

public class MediumLinkContentParser {

	private final String _data;
	private String _title;

	public MediumLinkContentParser(final String data) {
		_data = data;
	}
	
	public String getTitle() {
		
		if (_title == null) {
			_title = extractTitle();
		}
		
		return _title;
	}

	private String extractTitle() {
		
		final Pattern p = Pattern.compile("<title.*>(.*)</title>");
		final Matcher m = p.matcher(_data);
		if (m.find()) {
			return m.group(1)
					.replaceFirst(" - (.*) - Medium", "")
					.replaceAll("&amp;","&");
		}

		ExitHelper.exit("Failed to find <title> in Medium page");
		
		// NOTREACHED
		return null;
	}
}
