package data.linkchecker;

import java.io.File;
import java.util.Optional;

import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

public class MediumLinkContentChecker extends LinkContentChecker {

	private MediumLinkContentParser _parser;
	
	public MediumLinkContentChecker(final LinkData linkData,
                                    final Optional<ArticleData> articleData,
			                        final File file) {
		super(linkData, articleData, file);
	}

	@Override
	protected LinkContentCheck checkGlobalData(String data) {
		_parser = new MediumLinkContentParser(data);
		
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
}