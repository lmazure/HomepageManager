package fr.mazure.homepagemanager.data.linkchecker;

import java.time.Duration;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkProtection;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

/**
 * Generate the XML describing articles
 */
public class XmlGenerator {

    private static final String s_helpMessage = "@0@, @1@,… @9@ are replaced by the name of the first, second,… tenth author of the article.\n" +
                                                "@C[foobar]@ is replaced by <CODEROUTINE>foobar</CODEROUTINE>.";
    /**
     * @param links links of the article
     * @param date creation date of the article
     * @param authors authors of the article
     * @param quality quality of the link (between -2 and 2)
     * @param comment comment of the article
     * @return XML describing the article
     */
    public static String generateXml(final List<ExtractedLinkData> links,
                                     final Optional<TemporalAccessor> date,
                                     final List<AuthorData> authors,
                                     final int quality,
                                     final String comment) {
        final StringBuilder builder = new StringBuilder();
        builder.append("<ARTICLE>");
        for (final ExtractedLinkData linkData: links) {
            builder.append("<X");
            if (linkData.status().isPresent()) {
                builder.append(generateStatus(linkData.status().get()));
            }
            if (linkData.protection().isPresent()) {
                builder.append(generateProtection(linkData.protection().get()));
            }
            if (quality != 0) {
                builder.append(" quality=\"" + quality + "\"");
            }
            builder.append(">")
                   .append("<T>")
                   .append(XmlHelper.transform(linkData.title()))
                   .append("</T>");
            for (final String subTitle: linkData.subtitles()) {
                builder.append("<ST>")
                       .append(XmlHelper.transform(subTitle))
                       .append("</ST>");
            }
            builder.append("<A>")
                   .append(XmlHelper.transform(linkData.url()))
                   .append("</A>");
            for (final Locale language: linkData.languages()) {
                builder.append(generateLanguage(language));
            }
            for (final LinkFormat format: linkData.formats()) {
                builder.append(generateFormat(format));
            }
            if (linkData.duration().isPresent()) {
                builder.append(generateDuration(linkData.duration().get()));
            }
            if (linkData.publicationDate().isPresent()) {
                builder.append(generateDate(linkData.publicationDate().get()));
            }
            builder.append("</X>");
        }
        for (final AuthorData authorData: authors) {
            builder.append(generateAuthor(authorData));
        }
        if (date.isPresent()) {
            builder.append(generateDate(date.get()));
        }
        builder.append("<COMMENT>");
        String str = XmlHelper.transform(comment);;
        for (int i = 0; i < authors.size(); i++) {
            str = str.replaceAll("@" + i + "@", generateAuthor(authors.get(i)));
        }
        str = str.replaceAll("@C\\[([^\\]]+)\\]@", "<CODEROUTINE>$1</CODEROUTINE>");
        builder.append(str)
               .append("</COMMENT>")
               .append("</ARTICLE>");
        return builder.toString();
    }

    /**
     * return the help message
     *
     * @return help message
     */
    public static String getHelpMessage() {
	    return s_helpMessage;
    }

    private static String generateAuthor(final AuthorData authorData) {
        final StringBuilder builder = new StringBuilder();
        builder.append("<AUTHOR>");
        if (authorData.getNamePrefix().isPresent()) {
            builder.append("<NAMEPREFIX>")
                   .append(XmlHelper.transform(authorData.getNamePrefix().get()))
                   .append("</NAMEPREFIX>");
        }
        if (authorData.getFirstName().isPresent()) {
            builder.append("<FIRSTNAME>")
                   .append(XmlHelper.transform(authorData.getFirstName().get()))
                   .append("</FIRSTNAME>");
        }
        if (authorData.getMiddleName().isPresent()) {
            builder.append("<MIDDLENAME>")
                   .append(XmlHelper.transform(authorData.getMiddleName().get()))
                   .append("</MIDDLENAME>");
        }
        if (authorData.getLastName().isPresent()) {
            builder.append("<LASTNAME>")
                   .append(XmlHelper.transform(authorData.getLastName().get()))
                   .append("</LASTNAME>");
        }
        if (authorData.getNameSuffix().isPresent()) {
            builder.append("<NAMESUFFIX>")
                   .append(XmlHelper.transform(authorData.getNameSuffix().get()))
                   .append("</NAMESUFFIX>");
        }
        if (authorData.getGivenName().isPresent()) {
            builder.append("<GIVENNAME>")
                   .append(XmlHelper.transform(authorData.getGivenName().get()))
                   .append("</GIVENNAME>");
        }
        builder.append("</AUTHOR>");
        return builder.toString();
    }

    /**
     * Generate the XML attribute for the status
     * @param status status
     * @return XML attribute as a text
     */
    public static String generateStatus(final LinkStatus status) {
        return switch (status) {
        case OK -> "";
        case REMOVED -> " status=\"removed\"";
        case DEAD -> " status=\"dead\"";
        case OBSOLETE -> " status=\"obsolete\"";
        case ZOMBIE -> " status=\"zombie\"";
        default -> throw new UnsupportedOperationException("Illegal status value (" + status + ")");
        };
    }

    private static String generateProtection(final LinkProtection protection) {
        return switch (protection) {
        case NO_REQUIRED_REGISTRATION -> "";
        case FREE_REGISTRATION -> " protection=\"free_registration\"";
        case PAYED_REGISTRATION -> " protection=\"payed_registration\"";
        default -> throw new UnsupportedOperationException("Illegal protection value (" + protection + ")");
        };
    }

    private static String generateLanguage(final Locale language) {
        final StringBuilder builder = new StringBuilder();
        builder.append("<L>");
        if (language == Locale.FRENCH) {
            builder.append("fr");
        } else if (language == Locale.ENGLISH) {
            builder.append("en");
        } else {
            throw new UnsupportedOperationException("Illegal language value (" + language + ")");
        }
        builder.append("</L>");
        return builder.toString();
    }

    private static String generateFormat(final LinkFormat format) {
        final StringBuilder builder = new StringBuilder();
        builder.append("<F>");
        switch (format) {
            case FLASH:
                builder.append("Flash");
                break;
            case FLASH_VIDEO:
                builder.append("Flash Video");
                break;
            case HTML:
                builder.append("HTML");
                break;
            case MP3:
                builder.append("MP3");
                break;
            case MP4:
                builder.append("MP4");
                break;
            case PDF:
                builder.append("PDF");
                break;
            case POSTSCRIPT:
                builder.append("PostScript");
                break;
            case POWERPOINT:
                builder.append("PowerPoint");
                break;
            case REALMEDIA:
                builder.append("RealMedia");
                break;
            case TXT:
                builder.append("txt");
                break;
            case WINDOWS_MEDIA_PLAYER:
                builder.append("Windows Media Player");
                break;
            case WORD:
                builder.append("Word");
                break;
            default:
                throw new UnsupportedOperationException("Illegal format value (" + format + ")");
        }
        builder.append("</F>");
        return builder.toString();
    }

    /**
     * Convert a date into an XML string
     * @param date date
     * @return XML String
     */
    public static String generateDate(final TemporalAccessor date) {
        final StringBuilder builder = new StringBuilder();
        builder.append("<DATE>")
               .append("<YEAR>")
               .append(date.get(ChronoField.YEAR))
               .append("</YEAR>");
        if (date.isSupported(ChronoField.MONTH_OF_YEAR)) {
            builder.append("<MONTH>")
                   .append(date.get(ChronoField.MONTH_OF_YEAR))
                   .append("</MONTH>");
            if (date.isSupported(ChronoField.DAY_OF_MONTH)) {
                builder.append("<DAY>")
                       .append(date.get(ChronoField.DAY_OF_MONTH))
                       .append("</DAY>");
            }
        }
        builder.append("</DATE>");
        return builder.toString();
    }

    /**
     * Convert a duration into an XML string
     * @param duration duration
     * @return XML String
     */
    public static String generateDuration(final Duration duration) {
        final StringBuilder builder = new StringBuilder();
        builder.append("<DURATION>");
        if ((duration.toHoursPart() > 0) || (duration.toMinutesPart()) > 0) {
            if (duration.toHoursPart() > 0) {
                builder.append("<HOUR>")
                       .append(duration.toHoursPart())
                       .append("</HOUR>");
            }
            builder.append("<MINUTE>")
                   .append(duration.toMinutesPart())
                   .append("</MINUTE>");
        }
        builder.append("<SECOND>")
               .append(duration.toSecondsPart())
               .append("</SECOND>")
               .append("</DURATION>");
        return builder.toString();
    }
}
