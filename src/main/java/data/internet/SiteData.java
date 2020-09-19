package data.internet;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SiteData {

    public enum Status {
        SUCCESS,
        FAILURE
    }

    final private URL _url;
    final private Status _status;
    final private Optional<Integer> _httpCode;
    final private Optional<Map<String, List<String>>> _headers;
    final private Optional<File> _dataFile;
    final private Optional<String> _error;

    public SiteData(final URL url,
                    final Status status,
                    final Optional<Integer> httpCode,
                    final Optional<Map<String, List<String>>> headers,
                    final Optional<File> dataFile,
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

    public Optional<File> getDataFile() {
        return _dataFile;
    }

    public Optional<String> getError() {
        return _error;
    }
}
