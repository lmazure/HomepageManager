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
                                                                 "von", "von",
                                                                 "van", "van"
                                                                );

    /**
     * Extract an author name from a string
     *
     * @param str string
     * @return extracted author name
     * @throws ContentParserException failure to extract an author name from the string
     */
    public static AuthorData getAuthor(final String str) throws ContentParserException {
        final String s = str.replaceAll("\\(.*\\)", "");
        final String[] nameParts = HtmlHelper.cleanContent(s).split(" ");
        if (nameParts.length == 2) {
            return new AuthorData(Optional.empty(),
                                  Optional.of(uppercaseFirstCharacter(nameParts[0])),
                                  Optional.empty(),
                                  Optional.of(uppercaseFirstCharacter(nameParts[1])),
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
                                      Optional.of(uppercaseFirstCharacter(nameParts[0])),
                                      Optional.empty(),
                                      Optional.of(properCaseParticle(nameParts[1]) + " " + uppercaseFirstCharacter(nameParts[2])),
                                      Optional.empty(),
                                      Optional.empty());
            }
            return new AuthorData(Optional.empty(),
                                  Optional.of(uppercaseFirstCharacter(nameParts[0])),
                                  Optional.of(uppercaseFirstCharacter(nameParts[1])),
                                  Optional.of(uppercaseFirstCharacter(nameParts[2])),
                                  Optional.empty(),
                                  Optional.empty());
        }
        if ((nameParts.length == 4) && isParticle(nameParts[2])) {
            return new AuthorData(Optional.empty(),
                                  Optional.of(uppercaseFirstCharacter(nameParts[0])),
                                  Optional.of(uppercaseFirstCharacter(nameParts[1])),
                                  Optional.of(properCaseParticle(nameParts[2]) + " " + uppercaseFirstCharacter(nameParts[3])),
                                  Optional.empty(),
                                  Optional.empty());
        }

        throw new ContentParserException("Failed to parse author name (author name has " + nameParts.length + " parts)");
    }

    private static boolean isParticle(final String name) {
        return _particles.containsKey(name.toLowerCase());
    }

    private static String properCaseParticle(final String particle) {
        return _particles.get(particle.toLowerCase());
    }

    private static String uppercaseFirstCharacter(final String str) {
        final StringBuilder converted = new StringBuilder();
        for (final char ch : str.toCharArray()) {
            converted.append(converted.isEmpty() ? Character.toUpperCase(ch) : ch);
        }
        return converted.toString();
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
            authorList.add(LinkContentParserUtils.getAuthor(s));
        }
        return authorList;
    }
}
