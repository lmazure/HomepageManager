package utils;

import java.io.PrintStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Log {

	
	private Logger.Level _level;
	private Thread _thread;
	private Instant _instant;
	private StringBuilder _stringBuilder;
	
	Log(final Logger.Level level,
	    final Thread thread,
	    final Instant instant) {
		_level = level;
		_thread = thread;
		_instant = instant;
		_stringBuilder = new StringBuilder();
	}
	
	public Log append(final String string) {
		_stringBuilder.append(string);
		return this;
	}
	
	public Log append(final boolean b) {
		return append(Boolean.toString(b));
	}
	
	public Log append(final int i) {
		return append(Integer.toString(i));
	}
	
	public void submit() {

		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss.SSS")
                                                             .withZone(ZoneId.systemDefault() );

		@SuppressWarnings("resource")
		final PrintStream stream = (_level.ordinal() <= Logger.Level.WARN.ordinal()) ? System.err : System.out;
		
		final StringBuilder builder = new StringBuilder();
		builder.append(_thread.getId());
		builder.append(" ");
		builder.append(_thread.getName());
		builder.append(" | ");
		builder.append(formatter.format(_instant));
		builder.append(" | ");
		builder.append(_stringBuilder);
		stream.println(builder.toString());
	}
}
