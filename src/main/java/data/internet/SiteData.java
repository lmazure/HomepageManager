package data.internet;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import utils.FileSection;

/**
 * Information about a link 
 * @param url URL of the link
 *
 * @param status status SUCCESS/FAILURE
 * @param httpCode HTTP code, empty if the retrieval failed
 * @param headers HTTT header, empty if the retrieval failed
 * @param dataFileSection file section containing the HTTP payload, empty if the retrieval failed
 * @param error error message describing why the information retrieval failed, empty if there is no error
 */
public record SiteData (String url,
                        Status status,
                        Optional<Integer> httpCode,
                        Optional<Map<String, List<String>>> headers,
                        Optional<FileSection> dataFileSection,
                        Optional<String> error) {
    /**
     * status of the information retrieval
     */
    public enum Status {
        /**
         * the information was successfully retrieved
         */
        SUCCESS,
        /**
         * could not retrieve the information
         */
        FAILURE
    }

}
