package data;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import utils.ExitHelper;
import utils.FileHelper;
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
		return check(FileHelper.slurpFile(_file));
	}

	public List<LinkContentCheck> check(final String data) {
		
		final List<LinkContentCheck> checks = new ArrayList<LinkContentCheck>();
		
		if (_linkData.getDuration().isPresent()) {
			final LinkContentCheck check = checkDuration(data);
			if ( check != null) {
				checks.add(check);
			}
		}
		checks.add(new LinkContentCheck("check content of " + _url + " " + _linkData.getTitle() + " using " + _file));
		
		return checks;
	}
	
	public LinkContentCheck checkDuration(final String data) {
		
		return null;
	}
}
