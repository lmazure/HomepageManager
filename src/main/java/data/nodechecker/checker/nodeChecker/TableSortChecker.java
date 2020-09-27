package data.nodechecker.checker.nodeChecker;

import java.text.Normalizer;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.XMLHelper;
import utils.xmlparsing.ElementType;


public class TableSortChecker extends NodeChecker {

    final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.DEFINITION2TABLE,
            ElementType.DEFINITIONTABLE
            });

    @Override
    public TagSelector getTagSelector() {
        return s_selector;
    }

    @Override
    public NodeRule[] getRules() {
        final NodeRule a[]= new NodeRule[1];
        a[0] = new NodeRule() { @Override
                               public CheckStatus checkElement(final Element e) { return checkTableSorting(e);}
                               @Override
                                  public String getDescription() { return "incorrect table sorting"; } };
        return a;
    }

    private CheckStatus checkTableSorting(final Element e) {

        String lastTerm = "";
        String lastNormalizedTerm = "";
        int numberOfTerms = 0;
        int numberOfUnsortedTerms = 0;
        String summary = "";

        final NodeList children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            final Node child = children.item(i);
            if ((child.getNodeType() == Node.ELEMENT_NODE) &&
                (XMLHelper.isOfType(child, ElementType.ROW))) {
                final NodeList childrenOfChild = child.getChildNodes();
                for (int j = 0; j < childrenOfChild.getLength(); j++) {
                    final Node childOfChild = childrenOfChild.item(j);
                    if ((childOfChild.getNodeType() == Node.ELEMENT_NODE) &&
                        (XMLHelper.isOfType(childOfChild, ElementType.TERM))) {
                        numberOfTerms++;
                        final Element elementOfChild = (Element)childOfChild;
                        final String textContent = getTextOfElementWithoutChildren(elementOfChild);
                        if (lastTerm.equals("")) {
                            lastTerm = textContent;
                            lastNormalizedTerm = normalize(lastTerm);
                        } else {
                            final String currentTerm = textContent;
                            final String currentNormalizedTerm = normalize(currentTerm);
                            if (currentNormalizedTerm.compareTo(lastNormalizedTerm) < 0) {
                                numberOfUnsortedTerms++;
                                if (summary.length() > 0) {
                                    summary += "\n";
                                }
                                summary += "\"" + currentTerm + "\" is not properly sorted";
                            }
                            lastTerm = currentTerm;
                            lastNormalizedTerm = currentNormalizedTerm;
                        }
                    }
                }
            }
        }

        if (((float)numberOfUnsortedTerms/(float)numberOfTerms) > 0.01) {
            // the table is considered as unsorted -> no error reported
            return null;
        }

        if (numberOfUnsortedTerms > 0) {
            return new CheckStatus(summary);
        }

        return null;
    }

    static private String normalize(final String str) {

        final String s = Normalizer.normalize(str, Normalizer.Form.NFD); // remove the accents

        String result = "";
        final int len = s.length();

        for (int j = 0; j < len; j++) {
            final int c = s.codePointAt(j);
            if (Character.isLetter(c) && Character.isUpperCase(c)) {
                result = result + new String(Character.toChars(Character.toUpperCase(c)));
            }
            if (Character.isLetter(c) && Character.isLowerCase(c)) {
                result = result + new String(Character.toChars(Character.toUpperCase(c)));
            }
            if (Character.isDigit(c)) {
                result = result + new String(Character.toChars(Character.toLowerCase(c)));
            }
        }

        return result;
    }

    static private String getTextOfElementWithoutChildren(final Element element) {

        final NodeList children = element.getChildNodes();
        String text = "";

        for (int i = 0; i < children.getLength(); i++) {
            final Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE) {
                text = text + child.getNodeValue();
            }
        }

        return text;
    }
}
