package data.internet;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import utils.FileSection;

/**
 * Information about a link
 *
 * @param url URL of the link
 * @param httpCode HTTP code, empty if the retrieval failed
 * @param headers HTTT header, empty if the retrieval failed
 * @param dataFileSection file section containing the HTTP payload, empty if the retrieval failed
 * @param error error message describing why the information retrieval failed, empty if there is no error
 * @param previousRedirection link data of the previous redirection
 */
public record SiteData(String url,
                       Optional<Integer> httpCode,
                       Optional<Map<String, List<String>>> headers,
                       Optional<FileSection> dataFileSection,
                       Optional<String> error,
                       SiteData previousRedirection) {
    // EMPTY
}
