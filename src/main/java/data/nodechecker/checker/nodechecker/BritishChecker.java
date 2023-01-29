package data.nodechecker.checker.nodechecker;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagselection.InclusionTagSelector;
import utils.XmlHelper;
import utils.xmlparsing.ElementType;

/**
*
*/
public class BritishChecker extends NodeChecker {

    private static final Set<Traduction> s_americanWords = new HashSet<>(Arrays.asList(
            new Traduction("analyze", "analyse"),
            new Traduction("anemia", "anaemia"),
            new Traduction("catalog[^u]", "catalogue"),
            new Traduction("center", "centre"),
            new Traduction("color", "colour"),
            new Traduction("defense", "defence"),
            new Traduction("fetus", "foetus"),
            new Traduction("\\W\\p{Ll}{2,}ize[sd]?\\W", "ise"),
            new Traduction("\\W\\p{Ll}{2,}ization", "isation"),
            new Traduction("\\W\\p{Ll}{2,}izing", "sing"),
            new Traduction("labor\\s", "labour"),
            new Traduction("license", "licence"),
            new Traduction("liters+\\s", "litre"),
            new Traduction("modeling", "modelling"),
            new Traduction("paralyze", "paralyse"),
            new Traduction("traveler", "traveller")));

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.COMMENT,
            ElementType.DESC
            });

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
                                           "\"");
                }
            }
        }

        return null;
    }

    private static class Traduction{
        private Pattern _americanRegexp;
        private String _british;
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