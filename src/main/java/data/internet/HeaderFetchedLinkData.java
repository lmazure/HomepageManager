package data.internet;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Header data (fetched from Internet) about a link
 *
 * @param url URL of the link
 * @param headers HTTT header, empty if the retrieval failed
 * @param previousRedirection link data of the previous redirection
 */
public record HeaderFetchedLinkData(String url,
                                    Optional<Map<String, List<String>>> headers,
                                    HeaderFetchedLinkData previousRedirection) {
    // EMPTY
}
