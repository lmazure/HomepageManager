package fr.mazure.homepagemanager.data.linkchecker;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.mazure.homepagemanager.data.violationcorrection.UpdateFeedFormatCorrection;
import fr.mazure.homepagemanager.utils.FileHelper;
import fr.mazure.homepagemanager.utils.FileSection;
import fr.mazure.homepagemanager.utils.xmlparsing.FeedData;
import fr.mazure.homepagemanager.utils.xmlparsing.FeedFormat;

/**
 * Class verifying the data aout a feed
 */
public class FeedContentChecker implements Checker {
    private final FeedData _feedData;
    private final FileSection _file;

    /**
     * @param feedData expected link data
     * @param file effective retrieved feed data
     */
    public FeedContentChecker(final FeedData feedData,
                              final FileSection file) {
        _feedData = feedData;
        _file = file;
    }

    /**
     * Perform the check
     *
     * @return List of violations
     * @throws ContentParserException Failure to extract the information
     */
    @Override
    public final List<LinkContentCheck> check() throws ContentParserException {
        final List<LinkContentCheck> checks = new ArrayList<>();
        final String content = FileHelper.slurpFileSection(_file, StandardCharsets.UTF_8);
        final FeedFormat format = getFormat(content);
        if (format != _feedData.getFormat()) {
            checks.add(new LinkContentCheck("WrongFeedFormat",
                                            "The expected feed format is " + _feedData.getFormat() + ", but the effective feed format is " + format,
                                            Optional.of(new UpdateFeedFormatCorrection(_feedData.getFormat(), format, _feedData.getUrl()))));

        }
        return checks;
    }
    
    private static FeedFormat getFormat(final String data) {
        if (data.contains("http://www.w3.org/2005/Atom")) {
            return FeedFormat.Atom;
        }
        return FeedFormat.RSS;
    }
}
