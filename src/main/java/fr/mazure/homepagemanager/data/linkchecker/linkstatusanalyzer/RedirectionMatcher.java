package fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import fr.mazure.homepagemanager.data.internet.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.internet.HeaderFetchedLinkData;
import fr.mazure.homepagemanager.utils.internet.HttpHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;

/**
 * a redirection pattern
 */
public class RedirectionMatcher {

    /**
     * How many time can an element appear in the redirection chain
     */
    public enum Multiplicity {
        /**
         * one
         */
        ONE,
        /**
         * one or more
         */
        ONE_OR_MANY
    }

    private static final char sep1 = '£';
    private static final char sep2 = '€';
    /**
     * regexp matching any string
     */
    public static final String ANY_STRING = "([^/" + sep1 + sep2 + "]+)";

    private final String _name;
    private final Set<LinkStatus> _statuses;
    private final List<Element> _elements;
    private Pattern _pattern;

    /**
     * Create an empty matcher
     *
     * @param name Name of the matcher
     * @param statuses Possible statuses for this redirection chain
     */
    public RedirectionMatcher(final String name,
                              final Set<LinkStatus> statuses) {
        _name = name;
        _statuses = statuses;
        _elements = new ArrayList<>();
    }

    /**
     * @param regexp the regular expression on the URL
     * @param httpCodes the possible valued for the received HTTP code, null for no response
     * @param multiplicity the number of times this element may appear in the chain of redirections
     */
    public void add(final String regexp,
                    final Set<Integer> httpCodes,
                    final Multiplicity multiplicity) {
        if (_pattern != null) {
            throw new UnsupportedOperationException("Cannot add a pattern element on a compiled pattern");
        }

        _elements.add(new Element(regexp, httpCodes, multiplicity));
    }

    /**
     * Get the name of the matcher
     *
     * @return Name
     */
    public final String getName() {
        return _name;
    }

    /**
     * Get the statuses possible for a matching redirection chain
     *
     * @return Statuses
     */
    public final Set<LinkStatus> getStatuses() {
        return _statuses;
    }

    /**
     * Compile the pattern
     */
    public void compile() {
        if (_pattern != null) {
            throw new UnsupportedOperationException("Cannot compile a compiled pattern");
        }

        final StringBuilder builder =  new StringBuilder();
        builder.append("^");
        for (final Element elem: _elements) {
            builder.append(compileElement(elem));
        }
        builder.append("$");

        _pattern = Pattern.compile(builder.toString());
   }

    private static String compileElement(final Element element) {
        final StringBuilder builder =  new StringBuilder();
        builder.append("(");
        builder.append(element.regexp());
        builder.append(sep1);
        builder.append("(");
        builder.append(element.httpCodes()
                .stream()
                .map(c -> (c == null) ? ""
                        : c.toString())
                .collect(Collectors.joining( "|")));
        builder.append(")");
        builder.append(sep2);
        builder.append(")");
        switch (element.multiplicity()) {
            case ONE:
                // do nothing
                break;
            case ONE_OR_MANY:
                builder.append("+");
                break;
            default:
                // impossible
                break;
        }
        return builder.toString().replace("()", ""); // kludge: add nothing if the only code is null
    }

    /**
     * Test if a redirection chain matches the pattern
     *
     * @param effectiveData effective date retrieved from the link
     * @return true is the chain matches, false if npt
     */
    public boolean doesRedirectionMatch(final FullFetchedLinkData effectiveData) {
        if (_pattern == null) {
            throw new UnsupportedOperationException("Cannot apply a non-compiled pattern");
        }
        final String encoded = encode(effectiveData);
        return _pattern.matcher(encoded).matches();
    }

    private static String encode(final FullFetchedLinkData effectiveData) {
        final StringBuilder builder = new StringBuilder();
        builder.append(effectiveData.url());
        builder.append(sep1);
        if (effectiveData.headers().isPresent()) {
            builder.append(HttpHelper.getResponseCodeFromHeaders(effectiveData.headers().get()));
        }
        builder.append(sep2);
        HeaderFetchedLinkData d = effectiveData.previousRedirection();
        while (d != null) {
            builder.append(d.url());
            builder.append(sep1);
            if (d.headers().isPresent()) {
                builder.append(HttpHelper.getResponseCodeFromHeaders(d.headers().get()));
            }
            builder.append(sep2);
            d = d.previousRedirection();
        }
        return builder.toString();
    }

    private record Element(String regexp,
                           Set<Integer> httpCodes,
                           Multiplicity multiplicity) {
    }
}
