package lmzr.homepagechecker;

import java.io.File;

/**
 * @author Laurent
 *
 */
public interface Logger {

	/**
	 * @param file
	 * @param tag
	 * @param value
	 * @param violation
	 * @param detail
	 */
	void record(
			File file,
			String tag,
			String value,
			String violation,
			String detail);
}
