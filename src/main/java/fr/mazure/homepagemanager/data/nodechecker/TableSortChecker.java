package fr.mazure.homepagemanager.data.nodechecker;

import java.text.Normalizer;
import java.util.Optional;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.mazure.homepagemanager.data.nodechecker.tagselection.InclusionTagSelector;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

/**
*
*/
public class TableSortChecker extends NodeChecker {

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.DEFINITION2TABLE,
            ElementType.DEFINITIONTABLE
            });

    /**
    * constructor
    */
    public TableSortChecker() {
        super(s_selector,
              TableSortChecker::checkTableSorting, "incorrect table sorting");
    }

    private static CheckStatus checkTableSorting(final Element e) {

        String lastTerm = "";
        String lastNormalizedTerm = "";
        int numberOfTerms = 0;
        int numberOfUnsortedTerms = 0;
        String summary = "";

        final NodeList children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            final Node child = children.item(i);
            if ((child.getNodeType() == Node.ELEMENT_NODE) &&
                (XmlHelper.isOfType(child, ElementType.ROW))) {
                final NodeList childrenOfChild = child.getChildNodes();
                for (int j = 0; j < childrenOfChild.getLength(); j++) {
                    final Node childOfChild = childrenOfChild.item(j);
                    if ((childOfChild.getNodeType() == Node.ELEMENT_NODE) &&
                        (XmlHelper.isOfType(childOfChild, ElementType.TERM))) {
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
            return new CheckStatus("ImproperTableSorting", summary, Optional.empty());
        }

        return null;
    }

    private static String normalize(final String str) {

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

    private static String getTextOfElementWithoutChildren(final Element element) {

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
