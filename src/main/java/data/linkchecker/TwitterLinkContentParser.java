package data.linkchecker;

import java.util.Locale;

public class TwitterLinkContentParser {

    private final String _data;

    public TwitterLinkContentParser(final String data) {
        _data = data;
    }
    
    public Locale getLanguage() {
        // https://gist.github.com/CripBoy/ad9148203af56cda212fd28d0dd95896
        // https://github.com/redouane59/twittered
        return Locale.ENGLISH;
    }

}
