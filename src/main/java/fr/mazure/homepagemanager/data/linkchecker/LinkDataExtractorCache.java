package fr.mazure.homepagemanager.data.linkchecker;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LinkDataExtractor cache
 */
public class LinkDataExtractorCache {

    private static final int s_cache_size = 1000;
    private LinkedHashMap<String, LinkDataExtractor> _map;

    /**
     * Constructor
     */
    public LinkDataExtractorCache() {

        _map = new LinkedHashMap<>(s_cache_size, 0.75f, true) {

            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(Map.Entry<String, LinkDataExtractor> eldest) {
                return size() > s_cache_size;
            }
        };

    }

    /**
     * retrieve a LinkDataExtractor
     *
     * @param url URL
     * @return LinkDataExtractor
     */
    public LinkDataExtractor query(final String url) {
        return _map.get(url);
    }

    /**
     * store a LinkDataExtractor
     *
     * @param url URL
     * @param value LinkDataExtractor
     */
    public void store(final String url,
                      final LinkDataExtractor value) {
        _map.put(url, value);
    }
}