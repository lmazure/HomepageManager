package data.internet;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import utils.FileSection;

/**
 *
 */
public class SiteData {

    /**
     *
     */
    public enum Status {
        SUCCESS,
        FAILURE
    }

    private final String _url;
    private final Status _status;
    private final Optional<Integer> _httpCode;
    private final Optional<Map<String, List<String>>> _headers;
    private final Optional<FileSection> _dataFile;
    private final Optional<String> _error;

    /**
     * @param url
     * @param status
     * @param httpCode
     * @param headers
     * @param dataFile
     * @param error
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
        _dataFile = dataFile;
        _error = error;
    }

    /**
     * @return
     */
    public String getUrl() {
        return _url;
    }

    /**
     * @return
     */
    public Status getStatus() {
        return _status;
    }

    /**
     * @return
     */
    public Optional<Integer> getHttpCode() {
        return _httpCode;
    }

    /**
     * @return
     */
    public Optional<Map<String, List<String>>> getHeaders() {
        return _headers;
    }

    public Optional<FileSection> getDataFile() {
        return _dataFile;
    }

    /**
     * @return
     */
    public Optional<String> getError() {
        return _error;
    }
}
