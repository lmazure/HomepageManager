package data.jsongenerator;

import java.util.Arrays;
import java.util.HashMap;

public class KeywordFactory {

    private final HashMap<String,Keyword> _keywords;

    public KeywordFactory() {
        _keywords = new HashMap<String,Keyword>();
    }

    public Keyword newKeyword(final String keyId) {

        if (_keywords.containsKey(keyId)) {
            return _keywords.get(keyId);
        }

        final Keyword keyword = new Keyword(keyId);
        _keywords.put(keyId, keyword);
        return keyword;
    }

    /**
     * @return sorted list of keywords
     */
    public Keyword[] getKeywords() {
        final Keyword k[] = _keywords.values().toArray(new Keyword[0]);
        Arrays.sort(k, new KeywordComparator());
        return k;
    }
}
