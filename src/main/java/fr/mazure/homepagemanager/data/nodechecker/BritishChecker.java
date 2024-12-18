package fr.mazure.homepagemanager.data.nodechecker;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;

import fr.mazure.homepagemanager.data.nodechecker.tagselection.InclusionTagSelector;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

/**
 *
 */
public class BritishChecker extends NodeChecker {

    private static final Predicate<String> s_whiteList;

    private static final List<Traduction> s_americanWords = Arrays.asList(
            new Traduction("analyze", "analyse"),
            new Traduction("anemia", "anaemia"),
            new Traduction("behavior", "behaviour"),
            new Traduction("catalog[^u]", "catalogue"),
            new Traduction("center", "centre"),
            new Traduction("color", "colour"),
            new Traduction("defense", "defence"),
            new Traduction("donut", "doughnut"),
            new Traduction("fetus", "foetus"),
            new Traduction("fulfill[^i]", "fulfil"),
            new Traduction("\\W\\p{Ll}{2,}ize[sd]?\\W", "ise"),
            new Traduction("\\W\\p{Ll}{2,}ization", "isation"),
            new Traduction("\\W\\p{Ll}{2,}izing", "sing"),
            new Traduction("labor\\s", "labour"),
            new Traduction("license[^d]", "licence"),
            new Traduction("liters+\\s", "litre"),
            new Traduction("modeling", "modelling"),
            new Traduction("paralyze", "paralyse"),
            new Traduction("traveler", "traveller"
            ));

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.COMMENT,
            ElementType.DESC
            });

    static {
        final List<Predicate<String>> list = Arrays.asList(
                Pattern.compile("criticize[sd]?").asPredicate()
                );

        s_whiteList = list.stream()
                          .reduce(Predicate::or)
                          .orElse(_ -> false);
    }

    /**
    * constructor
    */
    public BritishChecker() {
        super(s_selector,
                BritishChecker::commentUsesBritish,"a COMMENT must not contain US syntax");
    }

    private static CheckStatus commentUsesBritish(final Element e) {
        final List<String> list = XmlHelper.getFirstLevelTextContent(e);
        if (list.isEmpty()) {
            return null;
        }
        for (final String l: list ) {
            if (s_whiteList.test(l)) {
                continue;
            }
            for (final Traduction traduction: s_americanWords) {
                final String match = traduction.matchesAmerican(l);
                if (match != null) {
                    return new CheckStatus("AmericanSpelling",
                                           "COMMENT \"" +
                                           e.getTextContent() +
                                           "\" contains american word \"" +
                                           match +
                                           "\"  matching regexp \"" +
                                           traduction.getAmerican() +
                                           "\", it should be \"" +
                                           traduction.getBritish() +
                                           "\"",
                                           Optional.empty());
                }
            }
        }

        return null;
    }

    private static class Traduction{
        private final Pattern _americanRegexp;
        private final String _british;
        private Traduction(final String american,
                           final String british) {
            _americanRegexp = Pattern.compile(american);
            _british = british;
        }
        private String matchesAmerican(final String str) {
            final Matcher m = _americanRegexp.matcher(str);
            if (m.find()) {
                return m.group();
            }
            return null;
        }
        private String getAmerican() {
            return _americanRegexp.toString();
        }
        private String getBritish() {
            return _british;
        }
    }
}