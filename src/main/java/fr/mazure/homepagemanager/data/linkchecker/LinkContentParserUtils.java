package fr.mazure.homepagemanager.data.linkchecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * Helper class for the LinkDataExtractors
 *
 */
public class LinkContentParserUtils {

    private static final Map<String, String> _particles = Map.of("de", "De",
                                                                 "del", "Del",
                                                                 "von", "von",
                                                                 "van", "van"
                                                                );

    private static final Map<String, String> _doubleParticles = Map.of("van den", "van den"
                                                                      );

    /**
     * Extract an author name from a string
     *
     * @param str string
     * @return extracted author name
     * @throws ContentParserException failure to extract an author name from the string
     */
    public static AuthorData parseAuthorName(final String str) throws ContentParserException {
        Optional<String> nameSuffix = Optional.empty();
        String s2;
        if (str.endsWith(", PhD")) {
            nameSuffix = Optional.of("PhD");
            s2 = str.substring(0, str.length() - 5);
        } else {
            s2 = str;
        }
        final String s = s2.replaceAll("^(.+)\\(.*\\)", "$1"); // clumsy regexp to remove bracket except for "(λx.x)eranga"
        final String[] nameParts = HtmlHelper.cleanContent(s).split("( |\u00A0)");
        if (nameParts.length == 2) {
            return new AuthorData(Optional.empty(),
                                  Optional.of(formatName(nameParts[0])),
                                  Optional.empty(),
                                  Optional.of(formatName(nameParts[1])),
                                  Optional.empty(),
                                  Optional.empty());
        }
        if (nameParts.length == 1) {
            return new AuthorData(Optional.empty(),
                                  Optional.empty(),
                                  Optional.empty(),
                                  Optional.empty(),
                                  Optional.empty(),
                                  Optional.of(nameParts[0]));
        }
        if (nameParts.length == 3) {
            if (isParticle(nameParts[1])) {
                return new AuthorData(Optional.empty(),
                                      Optional.of(formatName(nameParts[0])),
                                      Optional.empty(),
                                      Optional.of(properCaseParticle(nameParts[1]) + " " + formatName(nameParts[2])),
                                      Optional.empty(),
                                      Optional.empty());
            }
            return new AuthorData(Optional.empty(),
                                  Optional.of(formatName(nameParts[0])),
                                  Optional.of(formatName(nameParts[1])),
                                  Optional.of(formatName(nameParts[2])),
                                  nameSuffix,
                                  Optional.empty());
        }
        if ((nameParts.length == 4)) {
            if (isParticle(nameParts[2])) {
                return new AuthorData(Optional.empty(),
                                      Optional.of(formatName(nameParts[0])),
                                      Optional.of(formatName(nameParts[1])),
                                      Optional.of(properCaseParticle(nameParts[2]) + " " + formatName(nameParts[3])),
                                      Optional.empty(),
                                      Optional.empty());
            }
            if (isDoubleParticle(nameParts[1], nameParts[2])) {
                return new AuthorData(Optional.empty(),
                                      Optional.of(formatName(nameParts[0])),
                                      Optional.empty(),
                                      Optional.of(nameParts[1].toLowerCase() + " " + nameParts[2].toLowerCase() + " " + formatName(nameParts[3])),
                                      Optional.empty(),
                                      Optional.empty());
            }
            if ((nameParts[2].startsWith("\"") || nameParts[2].startsWith("“")) &&
                (nameParts[2].endsWith("\"") || nameParts[2].endsWith("”"))) {
                return new AuthorData(Optional.empty(),
                                      Optional.of(formatName(nameParts[0])),
                                      Optional.of(formatName(nameParts[1])),
                                      Optional.of(formatName(nameParts[3])),
                                      Optional.empty(),
                                      Optional.of(formatName(nameParts[2].substring(1, nameParts[2].length() - 1))));
            }
            return new AuthorData(Optional.empty(),
                                  Optional.of(formatName(nameParts[0])),
                                  Optional.of(formatName(nameParts[1])),
                                  Optional.of(formatName(nameParts[2]) + " " + formatName(nameParts[3])),
                                  Optional.empty(),
                                  Optional.empty());
        }
        throw new ContentParserException("Failed to parse author name (author name has " + nameParts.length + " parts)");
    }

    private static boolean isParticle(final String str) {
        return _particles.containsKey(str.toLowerCase());
    }

    private static boolean isDoubleParticle(final String str1,
                                            final String str2) {
        return _doubleParticles.containsKey(str1.toLowerCase() + " " + str2.toLowerCase());
    }

    private static String properCaseParticle(final String particle) {
        return _particles.get(particle.toLowerCase());
    }

    /**
     * Properly format the name:
     * - uppercase the first character
     * - replace ' with ’
     *
     * @param str string to properly format
     * @return properly formatted string
     */
    private static String formatName(final String str) {
        final StringBuilder converted = new StringBuilder();
        for (final char ch : str.toCharArray()) {
            converted.append(converted.isEmpty() ? Character.toUpperCase(ch) : ch);
        }
        return converted.toString().replace("'", "’");
    }

    /**
     * Extract author names from a string
     *
     * @param str string
     * @return extracted author names
     * @throws ContentParserException failure to extract author names from the string
     */
    public static List<AuthorData> getAuthors(final String str) throws ContentParserException {
        final List<AuthorData> authorList = new ArrayList<>();
        final String[] splits = str.split(", and | and |, ");
        for (final String s: splits) {
            authorList.add(LinkContentParserUtils.parseAuthorName(s));
        }
        return authorList;
    }
}
