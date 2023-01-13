package data.nodechecker.checker.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.xml.sax.SAXException;

import data.DataController;
import data.FileHandler.Status;
import data.nodechecker.checker.nodechecker.NodeCheckError;
import data.NodeValueChecker;
import utils.ExitHelper;

/**
 * base class for all the tests of NodeChecker subclasses
 */
public class NodeValueCheckerTestBase {

    protected static void test(final String content,
                               final String... details) throws SAXException {

        final String expected = Arrays.stream(details)
                                      .sorted()
                                      .collect(Collectors.joining("\n"));

        List<NodeCheckError> effective = null;
        final NodeValueChecker checker = new NodeValueChecker(Paths.get("home"), Paths.get("tmp"), new DummyDataController());
        try {
            final Path tempFile = File.createTempFile("NodeValueCheckerTest", ".xml").toPath();
            Files.writeString(tempFile, content);
            effective = checker.check(tempFile);
        } catch (final IOException e) {
            ExitHelper.exit(e);
        }

        Assertions.assertEquals(expected, normalize(effective));
    }

    private static String normalize(final List<NodeCheckError> errors) {
        return errors.stream()
                     .map(e -> e.detail())
                     .sorted()
                     .collect(Collectors.joining("\n"));
    }

    private static class DummyDataController implements DataController {

        @Override
        public void handleCreation(final Path file, final Status status, final Path outputFile, final Path reportFile) {
            // do nothing
        }

        @Override
        public void handleDeletion(final Path file, final Status status, final Path outputFile, final Path reportFile) {
            // do nothing
        }
    }

}
