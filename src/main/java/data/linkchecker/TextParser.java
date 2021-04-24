package data.linkchecker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TextParser {

    final Pattern _pattern;
    final String _source;
    final String _field;

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
        _pattern = Pattern.compile(prefix + "(" + pattern + ")" + postfix, Pattern.MULTILINE);
        _source = source;
        _field = field;
    }

    String extract(final String data) throws ContentParserException {
        final Matcher m = _pattern.matcher(data);
        if (m.find()) {
            return m.group(1);
         }

        throw new ContentParserException("Failed to find " + _field + " in " + _source);
    }
}
