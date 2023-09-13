package fr.mazure.homepagemanager.data.nodechecker.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.xml.sax.SAXException;

import fr.mazure.homepagemanager.data.DataController;
import fr.mazure.homepagemanager.data.FileHandler.Status;
import fr.mazure.homepagemanager.data.NodeValueChecker;
import fr.mazure.homepagemanager.data.Violation;
import fr.mazure.homepagemanager.data.ViolationDataController;
import fr.mazure.homepagemanager.data.nodechecker.NodeCheckError;
import fr.mazure.homepagemanager.utils.ExitHelper;

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
        final NodeValueChecker checker = new NodeValueChecker(Paths.get("home"), Paths.get("tmp"), new DummyDataController(), new DummyViolationController());
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
                     .map(e -> e.detail() + "<<" + e.checkName() + ">>")
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

    private static class DummyViolationController implements ViolationDataController {

        @Override
        public void add(final Violation violation) {
            // do nothing
        }

        @Override
        public void remove(final Predicate<Violation> violationFilter) {
            // do nothing
        }
    }
}
