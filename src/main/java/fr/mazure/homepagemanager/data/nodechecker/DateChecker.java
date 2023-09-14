    /**
    * constructor
    */
package fr.mazure.homepagemanager.data.nodechecker;

import java.util.Calendar;
import java.util.Optional;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import fr.mazure.homepagemanager.data.nodechecker.tagselection.InclusionTagSelector;
import fr.mazure.homepagemanager.data.violationcorrection.UpdatePageDateCorrection;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

/**
*
*/
public class DateChecker extends NodeChecker {

    private static final Calendar s_now = Calendar.getInstance();
    private static final int s_now_year = s_now.get(Calendar.YEAR);
    private static final int s_now_month = s_now.get(Calendar.MONTH)+1;
    private static final int s_now_day = s_now.get(Calendar.DAY_OF_MONTH);

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.DATE
            });

    /**
    * constructor
    */
    public DateChecker() {
        super(s_selector,
              DateChecker::checkDateHierarchy, "the DATE components are incorrectly structured",
              DateChecker::checkDateValue, "the DATE components have an incorrect value");
    }

    private static CheckStatus checkDateHierarchy(final Element e) {

        final int numberOfYears = XmlHelper.getDescendantsByElementType(e, ElementType.YEAR).getLength();
        final int numberOfMonths = XmlHelper.getDescendantsByElementType(e, ElementType.MONTH).getLength();
        final int numberOfDays = XmlHelper.getDescendantsByElementType(e, ElementType.DAY).getLength();

        if (numberOfYears > 1) {
            return new CheckStatus("IncorrectDate", "more than one YEAR", Optional.empty());
        }
        if (numberOfMonths > 1) {
            return new CheckStatus("IncorrectDate", "more than one MONTH", Optional.empty());
        }
        if (numberOfDays > 1) {
            return new CheckStatus("IncorrectDate", "more than one DAY", Optional.empty());
        }

        if (numberOfYears == 0) {
            return new CheckStatus("IncorrectDate", "no YEAR", Optional.empty());
        }

        if ((numberOfMonths == 0) && (numberOfDays == 1)) {
            return new CheckStatus("IncorrectDate", "DAY without MONTH", Optional.empty());
        }

        return null;
    }

    private static CheckStatus checkDateValue(final Element e) {

        final NodeList years = XmlHelper.getDescendantsByElementType(e, ElementType.YEAR);
        final NodeList months = XmlHelper.getDescendantsByElementType(e, ElementType.MONTH);
        final NodeList days = XmlHelper.getDescendantsByElementType(e, ElementType.DAY);

        final int numberOfYears = years.getLength();
        final int numberOfMonths = months.getLength();
        final int numberOfDays = days.getLength();

        if (numberOfYears == 0) {
            return null;
        }
        long year;
        final String yearStr = years.item(0).getTextContent();
        try {
            year = Long.parseLong(yearStr);
            if (year < 1900) {
                // we check this only for articles
                if (XmlHelper.isOfType(e.getParentNode(), ElementType.X)) {
                    return new CheckStatus("IncorrectDate", "YEAR is less than 1900", Optional.empty());
                }
            }
            if (year > s_now_year) {
                return new CheckStatus("FutureDate", "YEAR is in the future", Optional.of(new UpdatePageDateCorrection()));
            }
        } catch (@SuppressWarnings("unused") final NumberFormatException ex) {
            return new CheckStatus("IncorrectDate", "YEAR (" + yearStr + ") is not an integer", Optional.empty());
        }

        if (numberOfMonths == 0) {
            return null;
        }
        int month;
        final String monthStr = months.item(0).getTextContent();
        try {
            month = Integer.parseInt(monthStr);
            if (month < 0) {
                return new CheckStatus("IncorrectDate", "MONTH is negative", Optional.empty());
            }
            if (month > 12) {
                return new CheckStatus("IncorrectDate", "MONTH is greater than 12", Optional.empty());
            }
            if ((year == s_now_year) && (month > s_now_month)) {
                return new CheckStatus("IncorrectDate", "YEAR/MONTH is in the future", Optional.of(new UpdatePageDateCorrection()));
            }
        } catch (@SuppressWarnings("unused") final NumberFormatException ex) {
            return new CheckStatus("IncorrectDate", "MONTH (" + monthStr + ") is not an integer", Optional.empty());
        }

        if (numberOfDays == 0) {
            return null;
        }
        int day;
        final String dayStr = days.item(0).getTextContent();
        try {
            day = Integer.parseInt(dayStr);
            if (day < 0) {
                return new CheckStatus("IncorrectDate", "DAY is negative", Optional.empty());
            }
            if (day > 31) {
                return new CheckStatus("IncorrectDate", "DAY is greater than 31", Optional.empty());
            }
            if ((year == s_now_year) && (month == s_now_month) && (day > s_now_day)) {
                return new CheckStatus("IncorrectDate", "YEAR/MONTH/DAY is in the future", Optional.of(new UpdatePageDateCorrection()));
            }
        } catch (@SuppressWarnings("unused") final NumberFormatException ex) {
            return new CheckStatus("IncorrectDate", "DAY (" + dayStr + ") is not an integer", Optional.empty());
        }

        final Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set((int)year,month-1,day);

        try {
            final int normalizedYear = calendar.get(Calendar.YEAR);
            final int normalizedMonth = calendar.get(Calendar.MONTH)+1;
            final int normalizedDay = calendar.get(Calendar.DAY_OF_MONTH);

            if ((year != normalizedYear) || (month != normalizedMonth) || (day != normalizedDay)) {
                return new CheckStatus("IncorrectDate", "incorrect DATE", Optional.empty());
            }
        } catch (final IllegalArgumentException ex) {
            return new CheckStatus("IncorrectDate", "incorrect DATE (" + ex.getMessage() + ")", Optional.empty());
        }

        return null;
    }
}
