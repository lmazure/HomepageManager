package fr.mazure.homepagemanager.data.violationcorrection;

import java.util.regex.Pattern;

import fr.mazure.homepagemanager.utils.xmlparsing.FeedFormat;

/**
 * Correct the format of a feed
 */
public class UpdateFeedFormatCorrection extends RegexpViolationCorrection {

    /**
     * Constructor
     * @param badFormat Incorrect format
     * @param correctFormat  Correct format
     * @param feedUrl URL of the feed
     */
    public UpdateFeedFormatCorrection(final FeedFormat badFormat,
                                      final FeedFormat correctFormat,
                                      final String feedUrl) {
        super("Update the feed format",
              "<FEED><A>" + Pattern.quote(feedUrl) + "</A><F>" + badFormat.toString() + "</F></FEED>",
              "<FEED><A>" + feedUrl + "</A><F>" + correctFormat.toString() + "</F></FEED>");
    }
}