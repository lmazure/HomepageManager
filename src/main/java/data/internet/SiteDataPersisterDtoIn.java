package data.internet;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import data.internet.SiteData.Status;

public class SiteDataPersisterDtoIn {

    private final Status _status;
    private final Optional<Integer> _httpCode;
    private final Optional<Map<String, List<String>>> _headers;
    private final Optional<InputStream> _dataStream;
    private final Optional<String> _error;
    
    public SiteDataPersisterDtoIn(final Status status,
                                  final Optional<Integer> httpCode,
                                  final Optional<Map<String, List<String>>> headers,
                                  final Optional<InputStream> dataStream,
                                  final Optional<String> error) {
        _status = status;
        _httpCode = httpCode;
        _headers = headers;
        _dataStream = dataStream;
        _error = error;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return _status;
    }

    /**
     * @return the httpCode
     */
    public Optional<Integer> getHttpCode() {
        return _httpCode;
    }

    /**
     * @return the headers
     */
    public Optional<Map<String, List<String>>> getHeaders() {
        return _headers;
    }

    /**
     * @return the dataStream
     */
    public Optional<InputStream> getDataStream() {
        return _dataStream;
    }

    /**
     * @return the error
     */
    public Optional<String> getError() {
        return _error;
    }
}
