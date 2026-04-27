package fr.mazure.homepagemanager.data.dataretriever;

import fr.mazure.homepagemanager.utils.internet.HtmlHelper;

/**
 * Utility class to simply get the redirection chain and to retrieve the content of a link
 */
public class SiteSlurper {

    private final CachedSiteDataRetriever _retriever;
    private final String _url;
    private FullFetchedLinkData _linkData;
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
        _linkData = null;
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


    /**
     * Get the redirection chain of the site
     *
     * @return the redirection chain of the site
     */
    public FullFetchedLinkData getLinkData() {
        if (_content == null) {
            _retriever.retrieve(_url, this::consumeSiteData, false);
        }
        return _linkData;
    }

    private void consumeSiteData(final FullFetchedLinkData linkData) {
        _linkData = linkData;
        _content = HtmlHelper.slurpFile(linkData.dataFileSection().get());
    }
}
