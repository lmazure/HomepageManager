package data;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import utils.xmlparsing.ArticleData;
import utils.xmlparsing.DateData;
import utils.xmlparsing.LinkData;

public class YoutubeWatchLinkContentChecker extends LinkContentChecker {

	private YoutubeWatchLinkContentParser _parser;
	
	public YoutubeWatchLinkContentChecker(final LinkData linkData,
                                          final Optional<ArticleData> articleData,
			                              final File file) {
		super(linkData, articleData, file);
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
			                              final Duration expectedDuration) {

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
	
	@Override
	protected LinkContentCheck checkDate(final String data,
			                             final DateData duration) {
		
		final LocalDate expectedDate = LocalDate.of(duration.getYear(), duration.getMonth().get(), duration.getDay().get());
		final LocalDate effectiveDate = _parser.getPublishDate();

		if (!expectedDate.equals(effectiveDate)) {
			return new LinkContentCheck("expected date " +
				                        expectedDate +
                                        " is not equal to the effective publish date " +
                                        effectiveDate);
       }

       return null;
	}
}
