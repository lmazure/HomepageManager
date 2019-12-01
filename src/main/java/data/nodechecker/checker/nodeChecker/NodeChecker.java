package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.TagSelector;


import org.w3c.dom.Element;

abstract public class NodeChecker {
	
    final static String A = "A";
    final static String ARTICLE = "ARTICLE";
    final static String AUTHOR = "AUTHOR";
    final static String BLIST = "BLIST";
    final static String B = "B"; 
    final static String BR = "BR"; 
    final static String CELL = "CELL";
    final static String CLIST = "CLIST";
    final static String CODEFILE = "CODEFILE";
    final static String CODESAMPLE = "CODESAMPLE";
    final static String COMMENT = "COMMENT";
    final static String CONTENT = "CONTENT";
    final static String DATE = "DATE";
    final static String DEFINITION2TABLE = "DEFINITION2TABLE";
    final static String DEFINITIONTABLE = "DEFINITIONTABLE";
    final static String DESC = "DESC";
    final static String DURATION = "DURATION";
    final static String F = "F";
    final static String FEED = "FEED";
    final static String I = "I";
    final static String ITEM = "ITEM";
    final static String KEY = "KEY";
    final static String L = "L";
    final static String LINE = "LINE";
    final static String LLIST = "LLIST";
    final static String MODIFIERKEY = "MODIFIERKEY";
    final static String NLIST = "NLIST";
    final static String PAGE = "PAGE";
    final static String PROMPT = "PROMPT";
    final static String ROW = "ROW";
    final static String SCRIPT = "SCRIPT";
    final static String SLIST = "SLIST";
    final static String SMALL = "SMALL";
    final static String ST = "ST";
    final static String T = "T";
    final static String TAB = "TAB";
    final static String TABCHAR = "TABCHAR";
    final static String TABLE = "TABLE";
    final static String TERM = "TERM";
    final static String TERM1 = "TERM1";
    final static String TEXTBLOCK = "TEXTBLOCK";
    final static String TITLE = "TITLE";
    final static String X = "X";

	public interface NodeRule {
		/**
		 * @param e element to verify
		 * @return true is the rule is verified
		 * false if the rule is violated
		 */
		public CheckStatus checkElement(final Element e);
		
		/**
		 * @return description of the violation
		 */
		public String getDescription();
	}
	
	/**
	 * @return return the selector of the tag checked by the checker
	 */
	abstract public TagSelector getTagSelector();
	
	/**
	 * @return list of rules returned by the checker
	 */
	abstract public NodeRule[] getRules();
}
