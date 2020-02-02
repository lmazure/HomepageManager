package data.internet.test;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({SynchronousSiteDataRetrieverTest.class,
                AsynchronousSiteDataRetrieverTest.class,
                CachedSiteDataRetrieverTest.class,
                SiteDataPersisterTest.class})
public class AllSiteRetrieverTests {
}
