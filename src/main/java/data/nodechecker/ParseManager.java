package data.nodechecker;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import data.nodechecker.checker.nodeChecker.DateChecker;
import data.nodechecker.checker.nodeChecker.DoubleSpaceChecker;
import data.nodechecker.checker.nodeChecker.DurationChecker;
import data.nodechecker.checker.nodeChecker.DurationPresenceChecker;
import data.nodechecker.checker.nodeChecker.EllipsisChecker;
import data.nodechecker.checker.nodeChecker.ExtremitySpaceChecker;
import data.nodechecker.checker.nodeChecker.FormatChecker;
import data.nodechecker.checker.nodeChecker.FormatFromURLChecker;
import data.nodechecker.checker.nodeChecker.KeyChecker;
import data.nodechecker.checker.nodeChecker.LanguageChecker;
import data.nodechecker.checker.nodeChecker.MiddleNewlineChecker;
import data.nodechecker.checker.nodeChecker.MissingSpaceChecker;
import data.nodechecker.checker.nodeChecker.ModifierKeyChecker;
import data.nodechecker.checker.nodeChecker.MultipleAppearancesOfURLChecker;
import data.nodechecker.checker.nodeChecker.NodeChecker;
import data.nodechecker.checker.nodeChecker.NonEmptyChecker;
import data.nodechecker.checker.nodeChecker.NonNormalizedAuthorChecker;
import data.nodechecker.checker.nodeChecker.NonNormalizedURLChecker;
import data.nodechecker.checker.nodeChecker.ProtectionFromURLChecker;
import data.nodechecker.checker.nodeChecker.TableSortChecker;
import data.nodechecker.checker.nodeChecker.TitleFormatChecker;
import data.nodechecker.checker.nodeChecker.URLProtocolChecker;
import data.nodechecker.checker.nodeChecker.XMLSchemaValidationChecker;


/**
 * @author Laurent
 *
 */
public class ParseManager {

	private final Set<Logger> a_loggers;
	
	/**
	 * @param loggers
	 */
	public ParseManager(final Set<Logger> loggers) {
		a_loggers = loggers;
	}
	/**
	 * @param file
	 */
	public void parse(final File file) {
		
		final Set<NodeChecker> nodeCheckers = new HashSet<NodeChecker>(); 
		nodeCheckers.add(new ExtremitySpaceChecker());
		nodeCheckers.add(new MiddleNewlineChecker());
		nodeCheckers.add(new EllipsisChecker());
		nodeCheckers.add(new DoubleSpaceChecker());
        nodeCheckers.add(new MissingSpaceChecker());
		nodeCheckers.add(new TitleFormatChecker());
		nodeCheckers.add(new NonEmptyChecker());
		nodeCheckers.add(new FormatChecker());
		nodeCheckers.add(new LanguageChecker());
		nodeCheckers.add(new FormatFromURLChecker());
		nodeCheckers.add(new NonNormalizedURLChecker());
		nodeCheckers.add(new NonNormalizedAuthorChecker());
		nodeCheckers.add(new TableSortChecker());
		nodeCheckers.add(new DurationPresenceChecker());
		nodeCheckers.add(new URLProtocolChecker());
		nodeCheckers.add(new DateChecker());
		nodeCheckers.add(new ModifierKeyChecker());
		nodeCheckers.add(new KeyChecker());
		nodeCheckers.add(new DurationChecker());
		nodeCheckers.add(new ProtectionFromURLChecker());
		nodeCheckers.add(new XMLSchemaValidationChecker(file));
		if ( file.getParentFile().getName().equals("links") ) {
			nodeCheckers.add(new MultipleAppearancesOfURLChecker(file));
		}

		final ParseWorker worker = new ParseWorker(file, nodeCheckers, a_loggers);
		worker.run();
	}
}
