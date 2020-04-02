package data;

import java.io.File;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import utils.xmlparsing.DurationData;
import utils.xmlparsing.LinkData;

public class YoutubeWatchLinkContentChecker extends LinkContentChecker {

	private YoutubeWatchLinkContentParser _parser;
	
	public YoutubeWatchLinkContentChecker(final LinkData linkData,
			                              final File file) {
		super(linkData, file);
	}

	@Override
	protected LinkContentCheck checkGlobal(String data) {
		_parser = new YoutubeWatchLinkContentParser(data);
		
		if (!_parser.isPlayable()) {
			return new LinkContentCheck("video is not playable");	    				
		}
		
		return null;
	}
	
	@Override
	public LinkContentCheck checkTitle(final String data,
                                       final String title) {
		
	    final String effectiveTitle = _parser.getTitle();
	    
	    if (!title.equals(effectiveTitle)) {
			return new LinkContentCheck("title \"" +
                	                    title +
                	                    "\"  is not equal to the real title \"" +
                	                    effectiveTitle +
                  		                "\"");	    	
	    }
	    
		return null;
	}

	@Override
	public LinkContentCheck checkDuration(final String data,
			                              final DurationData duration) {

		Duration expectedDuration = Duration.ofSeconds(duration.getSeconds());
		if (duration.getMinutes().isPresent()) {
			expectedDuration = expectedDuration.plus(Duration.ofMinutes(duration.getMinutes().get()));
		}
		if (duration.getHours().isPresent()) {
			expectedDuration = expectedDuration.plus(Duration.ofHours(duration.getHours().get()));
		}

		final Duration effectiveMinDuration = _parser.getMinDuration().truncatedTo(ChronoUnit.SECONDS);
		final Duration effectiveMaxDuration = _parser.getMaxDuration().truncatedTo(ChronoUnit.SECONDS).plusSeconds(1);
		
		if ((expectedDuration.compareTo(effectiveMinDuration) < 0) ||
		    (expectedDuration.compareTo(effectiveMaxDuration) > 0)) {
			return new LinkContentCheck("expected duration " +
			                        	expectedDuration +
			                        	" is not in the real duration interval [" +
			                        	effectiveMinDuration +
			                        	"," +
			                        	effectiveMaxDuration +
			                        	"]");
		}
		
		return null;
	}
}
