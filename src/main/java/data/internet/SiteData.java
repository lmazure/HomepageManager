package data.internet;

import java.io.File;

public class SiteData {

    public enum Status {
        SUCCESS,
        FAILURE
    };
    
    final private Status _status;
    final private File _dataFile;
    final private File _errorFile;
    
    public SiteData(final Status status,
                    final File dataFile,
                    final File errorFile) {
        _status = status;
        _dataFile = dataFile;
        _errorFile = errorFile;
    }

    public Status getStatus() {
        return _status;
    }

    public File getDataFile() {
        return _dataFile;
    }

    public File getErrorFile() {
        return _errorFile;
    }
    
    
}
