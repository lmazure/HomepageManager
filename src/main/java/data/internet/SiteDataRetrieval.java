package data.internet;

import java.util.concurrent.Future;

public class SiteDataRetrieval {

    public enum Status {
        /**
         * the data is up-to-date
         * getCachedData() return nulls
         * getUpToDateData() return a Future which is already resolved
         */
        UP_TO_DATE,
        /**
         * the data is cache and out-of-date
         * getCachedData() return the out-of-date data
         * getUpToDateData() return a Future which will contain the up-to-date data
         */
        CACHED,
        /**
         * there is not data
         * getCachedData() return nulls
         * getUpToDateData() return a Future which will contain the up-to-date data
         */
        EMPTY
    }

    final private Status _status;
    final private Future<SiteData> _upToDateData;
    final private SiteData _cacheData;
    
    SiteDataRetrieval(final Status status,
                      final Future<SiteData> upToDateData,
                      final SiteData cacheData) {
        
        _status = status;
        _upToDateData = upToDateData;
        _cacheData = cacheData;
    }
    
    public Status getStatus() {
        return _status;
    }
    public Future<SiteData> getUpToDateData() {
        return _upToDateData;
    }
    
    public SiteData getCachedData()  {
        return _cacheData;
    }
}
