package data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import utils.FileHelper;
import utils.xmlparsing.DurationData;
import utils.xmlparsing.LinkData;

public class LinkContentChecker {
	
    private final LinkData _linkData;
    private final File _file;

	public LinkContentChecker(final LinkData linkData,
			                  final File file) {
		_linkData = linkData;
		_file = file;
	}

	public final List<LinkContentCheck> check() {		
		return check(FileHelper.slurpFile(_file));
	}

	public final List<LinkContentCheck> check(final String data) {
		
				final List<LinkContentCheck> checks = new ArrayList<LinkContentCheck>();

		
		{
			final LinkContentCheck check = checkGlobal(data);
			if ( check != null) {
				checks.add(check);
				return checks;
			}
		}
		
		{
			final LinkContentCheck check = checkTitle(data, _linkData.getTitle());
			if ( check != null) {
				checks.add(check);
			}
		}
		
		if (_linkData.getDuration().isPresent()) {
			final LinkContentCheck check = checkDuration(data, _linkData.getDuration().get());
			if ( check != null) {
				checks.add(check);
			}
		}
		
		return checks;
	}
	
	public LinkContentCheck checkTitle(final String data,
                                       final String title) {
	    return null;
	}

	public LinkContentCheck checkDuration(final String data,
			                              final DurationData duration) {
		return null;
	}
	
	protected LinkContentCheck checkGlobal(final String data) {
		return null;
	}
}
