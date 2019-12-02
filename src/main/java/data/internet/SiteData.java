package data.internet;

import java.io.File;
import java.util.List;
import java.util.Map;

public class SiteData {

    public enum Status {
        SUCCESS,
        FAILURE
    };
    
    final private Status _status;
    final private int _httpCode;
    final private Map<String, List<String>> _headers; 
    final private File _dataFile;
    final private File _errorFile;
    
    public SiteData(final Status status,
                    final int httpCode,
                    final Map<String, List<String>> headers,
                    final File dataFile,
                    final File errorFile) {
        _status = status;
        _httpCode = httpCode;
        _headers = headers;
        _dataFile = dataFile;
        _errorFile = errorFile;
    }

    public Status getStatus() {
        return _status;
    }

    public int getHttpCode() {
        return _httpCode;
    }

    public Map<String, List<String>> getHeaders() {
        return _headers;
    }
    
    public File getDataFile() {
        return _dataFile;
    }

    public File getErrorFile() {
        return _errorFile;
    }
}
