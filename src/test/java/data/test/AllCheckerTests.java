package data.test;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({FileCheckerTest.class,
                NodeValueCheckerTest.class})
public class AllCheckerTests {

}
