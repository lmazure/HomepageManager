package data.nodechecker.checker.nodeChecker;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import utils.XmlHelper;
import utils.xmlparsing.ElementType;

public class DurationChecker extends NodeChecker {

    final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.DURATION
            });

    public DurationChecker() {
        super(s_selector,
              DurationChecker::checkDurationHierarchy, "the DURATION components are incorrectly structured",
              DurationChecker::checkDurationValue, "the DURATION components have an incorrect value");
    }

    private static CheckStatus checkDurationHierarchy(final Element e) {

        final int numberOfSeconds = XmlHelper.getDescendantsByElementType(e, ElementType.SECOND).getLength();
        final int numberOfMinutes = XmlHelper.getDescendantsByElementType(e, ElementType.MINUTE).getLength();
        final int numberOfHours = XmlHelper.getDescendantsByElementType(e, ElementType.HOUR).getLength();

        if (numberOfSeconds > 1) return new CheckStatus("more than one SECOND");
        if (numberOfMinutes > 1) return new CheckStatus("more than one MINUTE");
        if (numberOfHours > 1) return new CheckStatus("more than one HOUR");

        if (numberOfSeconds == 0) return new CheckStatus("no SECOND");

        if ((numberOfMinutes == 0) && (numberOfHours == 1)) return new CheckStatus("HOUR without MINUTE");

        return null;
    }

    private static CheckStatus checkDurationValue(final Element e) {

        final NodeList seconds = XmlHelper.getDescendantsByElementType(e, ElementType.SECOND);
        final NodeList minutes = XmlHelper.getDescendantsByElementType(e, ElementType.MINUTE);
        final NodeList hours = XmlHelper.getDescendantsByElementType(e, ElementType.HOUR);

        final int numberOfSeconds = seconds.getLength();
        final int numberOfMinutes = minutes.getLength();
        final int numberOfHours = hours.getLength();

        if (numberOfSeconds == 0) return null;

        final String secondStr = seconds.item(0).getTextContent();
        try {
            final int second = Integer.parseInt(secondStr);
            if (second < 0) return new CheckStatus("SECOND is negative");
            if (second > 59) return new CheckStatus("SECOND is greater than 59");
        } catch (@SuppressWarnings("unused") final NumberFormatException ex) {
            return new CheckStatus("SECOND is not an integer");
        }

        if (numberOfMinutes == 0) return null;

        final String minuteStr = minutes.item(0).getTextContent();
        try {
            final int minute = Integer.parseInt(minuteStr);
            if (minute < 0) return new CheckStatus("MINUTE is negative");
            if (minute > 59) return new CheckStatus("MINUTE is greater than 59");
        } catch (@SuppressWarnings("unused") final NumberFormatException ex) {
            return new CheckStatus("MINUTE is not an integer");
        }

        if (numberOfHours == 0) return null;

        final String hourStr = hours.item(0).getTextContent();
        try {
            final int hour = Integer.parseInt(hourStr);
            if (hour < 0) return new CheckStatus("HOUR is negative");
        } catch (@SuppressWarnings("unused") final NumberFormatException ex) {
            return new CheckStatus("HOUR is not an integer");
        }

        return null;
    }
}
