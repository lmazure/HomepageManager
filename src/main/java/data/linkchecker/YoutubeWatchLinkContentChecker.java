package data.linkchecker;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

public class YoutubeWatchLinkContentChecker extends LinkContentChecker {

	private YoutubeWatchLinkContentParser _parser;
	
	public YoutubeWatchLinkContentChecker(final LinkData linkData,
                                          final Optional<ArticleData> articleData,
			                              final File file) {
		super(linkData, articleData, file);
	}

	@Override
	protected LinkContentCheck checkGlobalData(String data) {
		_parser = new YoutubeWatchLinkContentParser(data);
		
		if (!_parser.isPlayable()) {
			return new LinkContentCheck("video is not playable");	    				
		}
		
		return null;
	}
	
	@Override
	public LinkContentCheck checkLinkTitle(final String data,
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
	public LinkContentCheck checkLinkDuration(final String data,
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
	protected LinkContentCheck checkArticleDate(final String data,
                                                final Optional<TemporalAccessor> publicationDate,
                                                final Optional<TemporalAccessor> creationDate)
	{

		if (creationDate.isEmpty()) {
			return new LinkContentCheck("YouTube link with no creation date");			
		}
		
	    final TemporalAccessor date = publicationDate.isPresent() ? publicationDate.get() : creationDate.get();
	
		if (!(date instanceof LocalDate)) {
			return new LinkContentCheck("Date without month or day");
       }

		final LocalDate expectedDate = (LocalDate)date;
		final LocalDate effectivePublishDate = _parser.getPublishDate();
		final LocalDate effectiveUploadDate = _parser.getUploadDate();

		if (!expectedDate.equals(effectivePublishDate)) {
			return new LinkContentCheck("expected date " +
				                        expectedDate +
                                        " is not equal to the effective publish date " +
                                        effectivePublishDate);
       }

	   if (!expectedDate.equals(effectiveUploadDate)) {
			return new LinkContentCheck("expected date " +
				                        expectedDate +
                                        " is not equal to the effective upload date " +
                                        effectivePublishDate);
       }

       return null;
	}
}
