package utils.xmlparsing;

import java.util.List;
import java.util.Optional;

public class KeywordData {

    private final String _keyId;
    private final String _keyText;
    private final Optional<ArticleData> _article;
    private final List<LinkData> _links;

    public KeywordData(final String keyId,
                       final String keyText,
                       final Optional<ArticleData> article,
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

    public Optional<ArticleData> getArticle() {
        return _article;
    }

    public List<LinkData> getLinks() {
        return _links;
    }
}
