package utils.xmlparsing;

import java.util.List;

public class KeywordData {

    private final String _keyId;
    private final String _keyText;
    private final List<ArticleData> _article;
    private final List<LinkData> _links;

    public KeywordData(final String keyId,
                       final String keyText,
                       final List<ArticleData> article,
                       final List<LinkData> links) {
        _keyId = keyId;
        _keyText = keyText;
        _article = article;
        _links = links;
    }

    public String getKeyId() {
        return _keyId;
    }

    public String getKeyText() {
        return _keyText;
    }

    public List<ArticleData> getArticle() {
        return _article;
    }

    public List<LinkData> getLinks() {
        return _links;
    }
}
