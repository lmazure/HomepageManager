package data.internet;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import utils.FileSection;

/**
 * Information (saved on disk) about a link 
 */
public class SiteData {

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

    private final String _url;
    private final Status _status;
    private final Optional<Integer> _httpCode;
    private final Optional<Map<String, List<String>>> _headers;
    private final Optional<String> _error;
    private final Optional<FileSection> _dataFile;

    /**
     * @param url URL of the link
     * @param status status SUCCESS/FAILURE
     * @param httpCode HTTP code, empty if the retrieval failed
     * @param headers HTTT header, empty if the retrieval failed
     * @param dataFile file section containing the HTTP payload, empty if the retrieval failed
     * @param error error message describing why the information retrieval failed, empty if there is no error
     */
    public SiteData(final String url,
                    final Status status,
                    final Optional<Integer> httpCode,
                    final Optional<Map<String, List<String>>> headers,
                    final Optional<FileSection> dataFile,
                    final Optional<String> error) {
        _url = url;
        _status = status;
        _httpCode = httpCode;
        _headers = headers;
        _error = error;
        _dataFile = dataFile;
    }

    /**
     * @return URL of the link
     */
    public String getUrl() {
        return _url;
    }

    /**
     * @return status SUCCESS/FAILURE
     */
    public Status getStatus() {
        return _status;
    }

    /**
     * @return HTTP code, empty if the retrieval failed
     */
    public Optional<Integer> getHttpCode() {
        return _httpCode;
    }

    /**
     * @return HTTT header, empty if the retrieval failed
     */
    public Optional<Map<String, List<String>>> getHeaders() {
        return _headers;
    }

    /**
     * @return  error message describing why the information retrieval failed, empty if there is no error
     */
    public Optional<String> getError() {
        return _error;
    }

    /**
     * @return file section containing the HTTP payload, empty if the retrieval failed
     */
    public Optional<FileSection> getDataFileSection() {
        return _dataFile;
    }
}
