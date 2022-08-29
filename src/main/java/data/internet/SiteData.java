package data.internet;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import utils.FileSection;

public class SiteData {

    public enum Status {
        SUCCESS,
        FAILURE
    }

    private final URL _url;
    private final Status _status;
    private final Optional<Integer> _httpCode;
    private final Optional<Map<String, List<String>>> _headers;
    private final Optional<FileSection> _dataFile;
    private final Optional<String> _error;

    public SiteData(final URL url,
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

    public URL getUrl() {
        return _url;
    }

    public Status getStatus() {
        return _status;
    }

    public Optional<Integer> getHttpCode() {
        return _httpCode;
    }

    public Optional<Map<String, List<String>>> getHeaders() {
        return _headers;
    }

    public Optional<FileSection> getDataFile() {
        return _dataFile;
    }

    public Optional<String> getError() {
        return _error;
    }
}
