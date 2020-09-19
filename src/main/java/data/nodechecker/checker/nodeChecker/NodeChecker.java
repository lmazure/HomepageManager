package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.TagSelector;


import org.w3c.dom.Element;

abstract public class NodeChecker {

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
