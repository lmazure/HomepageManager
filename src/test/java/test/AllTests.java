package test;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages({"data.internet.test",
                 "data.jsongenerator.test",
                 "data.nodechecker.URLmanagement.test",
                 "data.test"})
public class AllTests {
}
