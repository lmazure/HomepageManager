package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.TagSelector;


import org.w3c.dom.Element;

abstract public class NodeChecker {
	
    public final static String A = "A";  // TODO mode this list in another class
    public final static String ARTICLE = "ARTICLE";
    public final static String AUTHOR = "AUTHOR";
    public final static String BLIST = "BLIST";
    public final static String B = "B"; 
    public final static String BR = "BR"; 
    public final static String CELL = "CELL";
    public final static String CLIST = "CLIST";
    public final static String CODEFILE = "CODEFILE";
    public final static String CODESAMPLE = "CODESAMPLE";
    public final static String COMMENT = "COMMENT";
    public final static String CONTENT = "CONTENT";
    public final static String DATE = "DATE";
    public final static String DEFINITION2TABLE = "DEFINITION2TABLE";
    public final static String DEFINITIONTABLE = "DEFINITIONTABLE";
    public final static String DESC = "DESC";
    public final static String DURATION = "DURATION";
    public final static String F = "F";
    public final static String FEED = "FEED";
    public final static String I = "I";
    public final static String ITEM = "ITEM";
    public final static String KEY = "KEY";
    public final static String L = "L";
    public final static String LINE = "LINE";
    public final static String LLIST = "LLIST";
    public final static String MODIFIERKEY = "MODIFIERKEY";
    public final static String NLIST = "NLIST";
    public final static String PAGE = "PAGE";
    public final static String PROMPT = "PROMPT";
    public final static String ROW = "ROW";
    public final static String SCRIPT = "SCRIPT";
    public final static String SLIST = "SLIST";
    public final static String SMALL = "SMALL";
    public final static String ST = "ST";
    public final static String T = "T";
    public final static String TAB = "TAB";
    public final static String TABCHAR = "TABCHAR";
    public final static String TABLE = "TABLE";
    public final static String TERM = "TERM";
    public final static String TERM1 = "TERM1";
    public final static String TEXTBLOCK = "TEXTBLOCK";
    public final static String TITLE = "TITLE";
    public final static String X = "X";

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
