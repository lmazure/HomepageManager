package fr.mazure.homepagemanager.data.linkchecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper to extract a string from some data
 */
public class TextParser {

    private final Pattern _pattern;
    private final String _source;
    private final String _field;

    /**
     * @param prefix Regular expression of the text before the string to be extracted
     * @param postfix Regular expression of the text after the string to be extracted
     * @param source Textual description of the source of the data
     * @param field Textual description of the string to be extracted
     */
    public TextParser(final String prefix,
                      final String postfix,
                      final String source,
                      final String field) {
        this(prefix, ".+?", postfix, source, field);
    }

    /**
     * @param prefix Regular expression of the text before the string to be extracted
     * @param pattern Regular expression of the string to be extracted
     * @param postfix Regular expression of the text after the string to be extracted
     * @param source Textual description of the source of the data
     * @param field Textual description of the string to be extracted
     */
    public TextParser(final String prefix,
                      final String pattern,
                      final String postfix,
                      final String source,
                      final String field) {
        _pattern = Pattern.compile(prefix + "(" + pattern + ")" + postfix, Pattern.DOTALL);
        _source = source;
        _field = field;
    }

    /**
     * Extract a string from data
     *
     * @param data Data
     * @return Extracted string if found
     * @throws ContentParserException Exception if string not found
     */
    public String extract(final String data) throws ContentParserException {
        final Optional<String> str = extractOptional(data);
        if (str.isPresent()) {
            return str.get();
         }

        throw new ContentParserException("Failed to find " + _field + " in " + _source);
    }

    /**
     * Extract a string from data
     *
     * @param data Data
     * @return Extracted string if found, empty Optional if not found
     */
    public Optional<String> extractOptional(final String data) {
        final Matcher m = _pattern.matcher(data);
        if (m.find()) {
            return Optional.of(m.group(1));
         }

        return Optional.empty();
    }

    /**
     * Extract strings from data
     *
     * @param data Data
     * @return Extracted strings corresponding to all matches
     */
    public List<String> extractMulti(final String data) {
        final List<String> matches = new ArrayList<>();
        final Matcher m = _pattern.matcher(data);
        while (m.find()) {
            matches.add(m.group(1));
         }
        return matches;
    }}
