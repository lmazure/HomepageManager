package fr.mazure.homepagemanager.data.dataretriever;

import fr.mazure.homepagemanager.utils.internet.HtmlHelper;

/**
 * Utility class to retrieve the content of a site
 */
public class SiteSlurper {

    private final CachedSiteDataRetriever _retriever;
    private final String _url;
    private String _content;

    /**
     * Constructor
     *
     * @param retriever the retriever to use
     * @param url the URL of the site to retrieve
     */
    public SiteSlurper(final CachedSiteDataRetriever retriever,
                       final String url) {
        _retriever = retriever;
        _url = url;
        _content = null;
    }

    /**
     * Get the content of the site
     *
     * @return the content of the site
     */
    public String getContent() {
        if (_content == null) {
            _retriever.retrieve(_url, this::consumeSiteData, false);
        }
        return _content;
    }

    private void consumeSiteData(final FullFetchedLinkData siteData) {
        _content = HtmlHelper.slurpFile(siteData.dataFileSection().get());
    }
}
