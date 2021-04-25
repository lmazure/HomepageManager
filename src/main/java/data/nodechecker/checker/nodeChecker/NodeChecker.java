package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.TagSelector;
import utils.XmlHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.w3c.dom.Element;

public class NodeChecker {

    private final TagSelector _tagSelector;
    private final Function<Element, CheckStatus>[] _rules;
    private final String[] _descriptions;

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

    @SuppressWarnings("unchecked")
    protected NodeChecker(final TagSelector tagSelector,
                          final Function<Element, CheckStatus> rule1,
                          final String description1) {
        _tagSelector = tagSelector;
        _rules = new Function[1];
        _rules[0] = rule1;
        _descriptions = new String[1];
        _descriptions[0] = description1;
    }

    @SuppressWarnings("unchecked")
    protected NodeChecker(final TagSelector tagSelector,
                          final Function<Element, CheckStatus> rule1,
                          final String description1,
                          final Function<Element, CheckStatus> rule2,
                          final String description2) {
        _tagSelector = tagSelector;
        _rules = new Function[2];
        _rules[0] = rule1;
        _rules[1] = rule2;
        _descriptions = new String[2];
        _descriptions[0] = description1;
        _descriptions[1] = description2;
    }

    public boolean isElementCheckable(final Element element) {
        return _tagSelector.isTagCheckable(XmlHelper.getElementType(element));
    }

    public List<NodeCheckError> check(final Element element) {
        final List<NodeCheckError> errors = new ArrayList<>();
        for (int i = 0; i < _rules.length; i++) {
            final CheckStatus status = _rules[i].apply(element);
            if (status != null) {
                errors.add(new NodeCheckError(element.getTagName(), element.getTextContent(), _descriptions[i], status.getDetail()));
            }
        }
        return errors;
    }
}
