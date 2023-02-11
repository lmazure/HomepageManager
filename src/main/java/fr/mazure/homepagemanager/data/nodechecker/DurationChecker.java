package fr.mazure.homepagemanager.data.nodechecker;

import java.util.Optional;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import fr.mazure.homepagemanager.data.nodechecker.tagselection.InclusionTagSelector;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

/**
*
*/
public class DurationChecker extends NodeChecker {

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.DURATION
            });

    /**
    * constructor
    */
    public DurationChecker() {
        super(s_selector,
              DurationChecker::checkDurationHierarchy, "the DURATION components are incorrectly structured",
              DurationChecker::checkDurationValue, "the DURATION components have an incorrect value");
    }

    private static CheckStatus checkDurationHierarchy(final Element e) {

        final int numberOfSeconds = XmlHelper.getDescendantsByElementType(e, ElementType.SECOND).getLength();
        final int numberOfMinutes = XmlHelper.getDescendantsByElementType(e, ElementType.MINUTE).getLength();
        final int numberOfHours = XmlHelper.getDescendantsByElementType(e, ElementType.HOUR).getLength();

        if (numberOfSeconds > 1) {
            return new CheckStatus("IncorrectDuration", "more than one SECOND", Optional.empty());
        }
        if (numberOfMinutes > 1) {
            return new CheckStatus("IncorrectDuration", "more than one MINUTE", Optional.empty());
        }
        if (numberOfHours > 1) {
            return new CheckStatus("IncorrectDuration", "more than one HOUR", Optional.empty());
        }

        if (numberOfSeconds == 0) {
            return new CheckStatus("IncorrectDuration", "no SECOND", Optional.empty());
        }

        if ((numberOfMinutes == 0) && (numberOfHours == 1)) {
            return new CheckStatus("IncorrectDuration", "HOUR without MINUTE", Optional.empty());
        }

        return null;
    }

    private static CheckStatus checkDurationValue(final Element e) {

        final NodeList seconds = XmlHelper.getDescendantsByElementType(e, ElementType.SECOND);
        final NodeList minutes = XmlHelper.getDescendantsByElementType(e, ElementType.MINUTE);
        final NodeList hours = XmlHelper.getDescendantsByElementType(e, ElementType.HOUR);

        final int numberOfSeconds = seconds.getLength();
        final int numberOfMinutes = minutes.getLength();
        final int numberOfHours = hours.getLength();

        if (numberOfSeconds == 0) {
            return null;
        }

        final String secondStr = seconds.item(0).getTextContent();
        try {
            final int second = Integer.parseInt(secondStr);
            if (second < 0) {
                return new CheckStatus("IncorrectDuration", "SECOND is negative", Optional.empty());
            }
            if (second > 59) {
                return new CheckStatus("IncorrectDuration", "SECOND is greater than 59", Optional.empty());
            }
        } catch (@SuppressWarnings("unused") final NumberFormatException ex) {
            return new CheckStatus("IncorrectDuration", "SECOND is not an integer", Optional.empty());
        }

        if (numberOfMinutes == 0) {
            return null;
        }

        final String minuteStr = minutes.item(0).getTextContent();
        try {
            final int minute = Integer.parseInt(minuteStr);
            if (minute < 0) {
                return new CheckStatus("IncorrectDuration", "MINUTE is negative", Optional.empty());
            }
            if (minute > 59) {
                return new CheckStatus("IncorrectDuration", "MINUTE is greater than 59", Optional.empty());
            }
        } catch (@SuppressWarnings("unused") final NumberFormatException ex) {
            return new CheckStatus("IncorrectDuration", "MINUTE is not an integer", Optional.empty());
        }

        if (numberOfHours == 0) {
            return null;
        }

        final String hourStr = hours.item(0).getTextContent();
        try {
            final int hour = Integer.parseInt(hourStr);
            if (hour < 0) {
                return new CheckStatus("IncorrectDuration", "HOUR is negative", Optional.empty());
            }
        } catch (@SuppressWarnings("unused") final NumberFormatException ex) {
            return new CheckStatus("IncorrectDuration", "HOUR is not an integer", Optional.empty());
        }

        return null;
    }
}
