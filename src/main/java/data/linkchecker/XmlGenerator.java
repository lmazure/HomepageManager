package data.linkchecker;

import java.time.Duration;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkFormat;

public class XmlGenerator {

    public static String generateXml(final List<ExtractedLinkData> links,
                                     final Optional<TemporalAccessor> date,
                                     final List<AuthorData> authors) {
        final StringBuilder builder = new StringBuilder();
        builder.append("<ARTICLE>");
        for (ExtractedLinkData linkData: links) {
            builder.append("<X");
            if (linkData.getStatus().isPresent()) {
                builder.append(" status=\"");
                switch (linkData.getStatus().get()) {
                    case DEAD:
                        builder.append("dead");
                        break;
                    case OBSOLETE:
                        builder.append("obsolete");
                        break;
                    case ZOMBIE:
                        builder.append("zombie");
                        break;
                    default:
                        throw new UnsupportedOperationException("Illegal status value (" + linkData.getStatus().get() + ")");
                }
                builder.append("\"");
            }
            if (linkData.getProtection().isPresent()) {
                builder.append(" status=\"");
                switch (linkData.getProtection().get()) {
                    case FREE_REGISTRATION:
                        builder.append("free_registration");
                        break;
                    case PAYED_REGISTRATION:
                        builder.append("payed_registration");
                        break;
                    default:
                        throw new UnsupportedOperationException("Illegal protection value (" + linkData.getProtection().get() + ")");
                }
                builder.append("\"");
            }
            builder.append(">");
            builder.append("<T>");
            builder.append(escapeXml(linkData.getTitle()));
            builder.append("</T>");
            for (String subTitle: linkData.getSubtitles()) {
                builder.append("<ST>");
                builder.append(escapeXml(subTitle));
                builder.append("</ST>");
            }
            builder.append("<A>");
            builder.append(linkData.getUrl());
            builder.append("</A>");
            for (Locale language: linkData.getLanguages()) {
                builder.append(generateLanguage(language));
            }
            for (LinkFormat format: linkData.getFormats()) {
                builder.append(generateFormat(format));
            }
            if (linkData.getDuration().isPresent()) {
                builder.append(generateDuration(linkData.getDuration().get()));
            }
            if (linkData.getPublicationDate().isPresent()) {
                builder.append(generateDate(linkData.getPublicationDate().get()));
            }
            builder.append("</X>");
        }
        for (AuthorData authorData: authors ) {
            builder.append(generateAuthor(authorData));
        }
        if (date.isPresent()) {
            builder.append(generateDate(date.get()));
        }
        builder.append("<COMMENT>XXXXX</COMMENT>");
        builder.append("</ARTICLE>");
        return builder.toString();
    }

    private static String generateAuthor(final AuthorData authorData) {
        final StringBuilder builder = new StringBuilder();
        builder.append("<AUTHOR>");
        if (authorData.getNamePrefix().isPresent()) {
            builder.append("<NAMEPREFIX>");
            builder.append(escapeXml(authorData.getNamePrefix().get()));
            builder.append("</NAMEPREFIX>");
        }
        if (authorData.getFirstName().isPresent()) {
            builder.append("<FIRSTNAME>");
            builder.append(escapeXml(authorData.getFirstName().get()));
            builder.append("</FIRSTNAME>");
        }
        if (authorData.getMiddleName().isPresent()) {
            builder.append("<MIDDLENAME>");
            builder.append(escapeXml(authorData.getMiddleName().get()));
            builder.append("</MIDDLENAME>");
        }
        if (authorData.getLastName().isPresent()) {
            builder.append("<LASTNAME>");
            builder.append(escapeXml(authorData.getLastName().get()));
            builder.append("</LASTNAME>");
        }
        if (authorData.getNameSuffix().isPresent()) {
            builder.append("<NAMESUFFIX>");
            builder.append(escapeXml(authorData.getNameSuffix().get()));
            builder.append("</NAMESUFFIX>");
        }
        if (authorData.getGivenName().isPresent()) {
            builder.append("<GIVENNAME>");
            builder.append(escapeXml(authorData.getGivenName().get()));
            builder.append("</GIVENNAME>");
        }
        builder.append("</AUTHOR>");
        return builder.toString();
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

    private static String generateDate(final TemporalAccessor publicationDate) {
        final StringBuilder builder = new StringBuilder();
        builder.append("<DATE>");
        builder.append("<YEAR>");
        builder.append(publicationDate.get(ChronoField.YEAR));
        builder.append("</YEAR>");
        if (publicationDate.isSupported(ChronoField.MONTH_OF_YEAR)) {
            builder.append("<MONTH>");
            builder.append(publicationDate.get(ChronoField.MONTH_OF_YEAR));
            builder.append("</MONTH>");
            if (publicationDate.isSupported(ChronoField.DAY_OF_MONTH)) {
                builder.append("<DAY>");
                builder.append(publicationDate.get(ChronoField.DAY_OF_MONTH));
                builder.append("</DAY>");
            }
        }
        builder.append("</DATE>");
        return builder.toString();
    }

    private static String generateDuration(final Duration duration) {
        final StringBuilder builder = new StringBuilder();
        builder.append("<DURATION>");
        if ((duration.toHoursPart() > 0) || (duration.toMinutesPart()) > 0) {
            if (duration.toHoursPart() > 0) {
                builder.append("<HOUR>");
                builder.append(duration.toHoursPart());
                builder.append("</HOUR>");
            }
            builder.append("<MINUTE>");
            builder.append(duration.toMinutesPart());
            builder.append("</MINUTE>");
        }
        builder.append("<SECOND>");
        builder.append(duration.toSecondsPart());
        builder.append("</SECOND>");
        builder.append("</DURATION>");
        return builder.toString();
    }

    private static String escapeXml(final String str) {
        return str.replace("&", "&amp;")
                  .replace("<","&lt;")
                  .replace(">","&gt;");
    }
}
