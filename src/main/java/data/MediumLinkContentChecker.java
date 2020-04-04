package data;

import java.io.File;

import utils.xmlparsing.LinkData;

public class MediumLinkContentChecker extends LinkContentChecker {

	private MediumLinkContentParser _parser;
	
	public MediumLinkContentChecker(final LinkData linkData,
			                        final File file) {
		super(linkData, file);
	}

	@Override
	protected LinkContentCheck checkGlobal(String data) {
		_parser = new MediumLinkContentParser(data);
		
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
}