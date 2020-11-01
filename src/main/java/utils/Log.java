package utils;

import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Log {

    private static int THREAD_NAME_MAX_LENGTH = 27;
    private Logger.Level _level;
    private StringBuilder _stringBuilder;

    Log(final Logger.Level level,
        final Thread thread,
        final Instant instant) {

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss.SSS")
                                                             .withZone(ZoneId.systemDefault()); // TODO voir comment le créer une seule fois de façon thread safe


        _level = level;
        _stringBuilder = new StringBuilder();
        _stringBuilder.append(padString(thread.getName(), THREAD_NAME_MAX_LENGTH));
        _stringBuilder.append(" | ");
        _stringBuilder.append(formatter.format(instant));
        _stringBuilder.append(" | ");
        _stringBuilder.append(level.toString());
        _stringBuilder.append(" | ");
    }

    public Log append(final String string) {
        _stringBuilder.append(string.replaceAll("\0", "[0]").replaceAll("\r", "[returnline]").replaceAll("\n", "[newline]"));
        return this;
    }

    public Log append(final boolean b) {
        return append(Boolean.toString(b));
    }

    public Log append(final int i) {
        return append(Integer.toString(i));
    }

    public Log append(final File file) {
        return append(file.getAbsolutePath());
    }

    public Log append(final Path path) {
        return append(path.toString());
    }

    public Log append(final URL url) {
        return append(url.toString());
    }

    public Log append(final StackTraceElement[] stack) {
        for (final StackTraceElement elem: stack) {
            _stringBuilder.append("\n" + elem.toString());
        }
        return this;
    }

    public Log append(final Exception exception) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        _stringBuilder.append(sw.toString());
        return this;
    }

    public void submit() {
        @SuppressWarnings("resource")
        final PrintStream stream = (_level.ordinal() <= Logger.Level.WARN.ordinal()) ? System.err : System.out;
        stream.println(_stringBuilder.toString());
    }

    private static String padString(final String string,
                                    final int length) {

        if (string.length() >= length) {
            return string;
        }

        return (" ".repeat(length - string.length())) + string;
    }
}
