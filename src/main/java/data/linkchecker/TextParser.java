package data.linkchecker;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextParser {

    private final Pattern _pattern;
    private final String _source;
    private final String _field;

    public TextParser(final String prefix,
                      final String postfix,
                      final String source,
                      final String field) {
        this(prefix, ".+?", postfix, source, field);
    }

    public TextParser(final String prefix,
                      final String pattern,
                      final String postfix,
                      final String source,
                      final String field) {
        _pattern = Pattern.compile(prefix + "(" + pattern + ")" + postfix, Pattern.DOTALL);
        _source = source;
        _field = field;
    }

    public String extract(final String data) throws ContentParserException {
        final Optional<String> str = extractOptional(data);
        if (str.isPresent()) {
            return str.get();
         }

        throw new ContentParserException("Failed to find " + _field + " in " + _source);
    }

    public Optional<String> extractOptional(final String data) {
        final Matcher m = _pattern.matcher(data);
        if (m.find()) {
            return Optional.of(m.group(1));
         }

        return Optional.empty();
    }}
