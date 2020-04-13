package data.linkchecker;

import java.io.File;
import java.time.Duration;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import utils.FileHelper;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

public class LinkContentChecker {
	
    private final LinkData _linkData;
    private final Optional<ArticleData> _articleData;
    private final File _file;

	public LinkContentChecker(final LinkData linkData,
                              final Optional<ArticleData> articleData,
			                  final File file) {
		_linkData = linkData;
		_articleData = articleData;
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

		if (_articleData.isPresent() && _articleData.get().getDate().isPresent()) {
			final LinkContentCheck check = checkDate(data, _articleData.get().getDate().get());
			if ( check != null) {
				checks.add(check);
			}
		}

		return checks;
	}
	
	protected LinkContentCheck checkTitle(final String data,
                                                final String title)
	{
		return null;
	}

	protected LinkContentCheck checkDuration(final String data,
			                                       final Duration duration)
	{
		return null;
	}
      
	protected LinkContentCheck checkDate(final String data,
			                                   final TemporalAccessor duration)
	{
		return null;
	}

	protected LinkContentCheck checkGlobal(final String data)
	{
		return null;
	}
}
