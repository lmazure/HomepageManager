package fr.mazure.homepagemanager.utils.xmlparsing;

import java.util.List;

/**
 * @param keyId ID of the keyword
 * @param keyText Text of the keyword
 * @param article List of articles containing the keyword
 * @param links List of links containing the keyword
 */
public record KeywordData(String keyId,
                          String keyText,
                          List<ArticleData> article,
                          List<LinkData> links) {
    // EMPTY
}
