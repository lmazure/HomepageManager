package data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.ExitHelper;

public class YoutubeWatchLinkContentParser {

	private final String _data;
	private String _title;
	private String _description;
	private LocalDate _uploadDate;
	private LocalDate _publishDate;
	private Duration _minDuration;
	private Duration _maxDuration;
	
	public YoutubeWatchLinkContentParser(final String data) {
		_data = data;
	}
	
	public String getTitle() {
		
		if (_title == null) {
			_title = extractText("title");
		}
		
		return _title;
	}


	public String getDescription() {
		
		if (_description == null) {
			_description = extractText("shortDescription");
		}
		
		return _description;
	}

	public LocalDate getUploadDate() {
		
		if (_uploadDate == null) {
			_uploadDate = extractDate("uploadDate");
		}
		
		return _uploadDate;
	}

	public LocalDate getPublishDate() {
		
		if (_publishDate == null) {
			_publishDate = extractDate("publishDate");
		}
		
		return _publishDate;
	}


	public Duration getMinDuration() {
		
		if (_minDuration == null) {
			extractDuration();
		}
		
		return _minDuration;
	}
	
	public Duration getMaxDuration() {
		
		if (_maxDuration == null) {
			extractDuration();
		}
		
		return _maxDuration;
	}
	
	private String extractText(final String str) {
		
		String text = null;
		
		final Pattern p = Pattern.compile("\\\\\"" + str + "\\\\\":\\\\\"(.+?)(?<!\\\\\\\\)\\\\\"");
		final Matcher m = p.matcher(_data);
		while (m.find()) {
			final String t = m.group(1);
			if (text == null) {
				text = t;
			} else {
				if (!text.equals(t)) {
					ExitHelper.exit("Found different " + str + " texts in YouTube page");						
				}
			}
		}
		
		if (text == null) {
			ExitHelper.exit("Failed to extract  " + str + " text from YouTube watch page");			
		}
		
		assert(text != null);
		text = text.replaceAll("\\\\\\\\n", "\n")
				   .replaceAll("\\\\/","/")
       		       .replaceAll("\\\\\\\\\\\\\"","\"");
		
		return text;
	}

	private LocalDate extractDate(final String str) {		
		return LocalDate.parse(extractText(str));
	}
	
	private void extractDuration() {
		
		int minDuration = Integer.MAX_VALUE;
		int maxDuration = Integer.MIN_VALUE;
		
		final Pattern p = Pattern.compile("\\\\\"approxDurationMs\\\\\":\\\\\"(\\d+)\\\\\"");
		final Matcher m = p.matcher(_data);
		while (m.find()) {
			final int duration = Integer.parseInt(m.group(1));
			if (duration < minDuration) {
				minDuration = duration;
			}
			if (duration > maxDuration) {
				maxDuration = duration;
			}
		}

		if (minDuration == Integer.MAX_VALUE) {
			ExitHelper.exit("Failed to extract durations from YouTube watch page");
		}
		
		_minDuration = Duration.ofMillis(minDuration);
		_maxDuration = Duration.ofMillis(maxDuration);
	}
}
