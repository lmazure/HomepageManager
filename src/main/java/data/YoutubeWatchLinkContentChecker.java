package data;

import java.io.File;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.xmlparsing.LinkData;

public class YoutubeWatchLinkContentChecker extends LinkContentChecker {

	public YoutubeWatchLinkContentChecker(final URL url,
			                              final LinkData linkData,
			                              final File file) {
		super(url, linkData, file);
	}

	@Override
	public LinkContentCheck checkDuration(final String data) {
		
		System.out.println("here -> ");
		return null;
	}
}
