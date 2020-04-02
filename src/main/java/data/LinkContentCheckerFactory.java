package data;

import java.io.File;
import java.net.URL;

import utils.xmlparsing.LinkData;

public class LinkContentCheckerFactory {

	public static LinkContentChecker build(final URL url,
                                           final LinkData linkData,
                                           final File file) {
		
		if (url.toString().startsWith("https://www.youtube.com/watch?v=")) {
			return new YoutubeWatchLinkContentChecker(linkData, file);
		}
		
		if (url.toString().startsWith("https://medium.com/")) {
			return new MediumLinkContentChecker(linkData, file);
		}
		
		return new LinkContentChecker(linkData, file);
	}
}
