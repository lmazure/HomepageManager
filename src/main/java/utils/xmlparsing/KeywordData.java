package utils.xmlparsing;

import java.util.List;

/**
 * @param keyId 
 * @param keyText 
 * @param article 
 * @param links 
 *
 */
public record KeywordData(String keyId,
                          String keyText,
                          List<ArticleData> article,
                          List<LinkData> links) {
    // EMPTY
}
