package data.internet;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Header data (fetched from Internet) about a link
 *
 * @param url URL of the link
 * @param headers HTTT header, empty if the retrieval failed
 * @param error error message describing why the information retrieval failed, empty if there is no error
 * @param previousRedirection link data of the previous redirection
 */
public record HeaderFetchedLinkData(String url,
                                    Optional<Map<String, List<String>>> headers,
                                    Optional<String> error,
                                    HeaderFetchedLinkData previousRedirection) {
    // EMPTY
}
