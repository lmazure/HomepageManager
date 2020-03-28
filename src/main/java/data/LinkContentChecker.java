package data;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import utils.xmlparsing.LinkData;

public class LinkContentChecker {
	
	private final URL _url;
    private final LinkData _linkData;
    private final File _file;

	public LinkContentChecker(final URL url,
			                  final LinkData linkData,
			                  final File file) {
		_url = url;
		_linkData = linkData;
		_file = file;
	}

	public List<LinkContentCheck> check() {
		
		final List<LinkContentCheck> checks = new ArrayList<LinkContentCheck>();
		
		checks.add(new LinkContentCheck("check content of " + _url + " " + _linkData.getTitle() + " using " + _file));
		
		return checks;
	}
}
