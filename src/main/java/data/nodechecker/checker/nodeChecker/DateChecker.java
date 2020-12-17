package data.nodechecker.checker.nodeChecker;

import java.util.Calendar;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import utils.XmlHelper;
import utils.xmlparsing.ElementType;

public class DateChecker extends NodeChecker {

    final static Calendar s_now = Calendar.getInstance();
    final static int s_now_year = s_now.get(Calendar.YEAR);
    final static int s_now_month = s_now.get(Calendar.MONTH)+1;
    final static int s_now_day = s_now.get(Calendar.DAY_OF_MONTH);

    final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.DATE
            });

    public DateChecker() {
        super(s_selector,
              DateChecker::checkDateHierarchy, "the DATE components are incorrectly structured",
              DateChecker::checkDateValue, "the DATE components have an incorrect value");
    }

    private static CheckStatus checkDateHierarchy(final Element e) {

        final int numberOfYears = XmlHelper.getDescendantsByElementType(e, ElementType.YEAR).getLength();
        final int numberOfMonths = XmlHelper.getDescendantsByElementType(e, ElementType.MONTH).getLength();
        final int numberOfDays = XmlHelper.getDescendantsByElementType(e, ElementType.DAY).getLength();

        if (numberOfYears > 1) return new CheckStatus("more than one YEAR");
        if (numberOfMonths > 1) return new CheckStatus("more than one MONTH");
        if (numberOfDays > 1) return new CheckStatus("more than one DAY");

        if (numberOfYears == 0) return new CheckStatus("no YEAR");

        if ((numberOfMonths == 0) && (numberOfDays == 1)) return new CheckStatus("DAY without MONTH");

        return null;
    }

    private static CheckStatus checkDateValue(final Element e) {

        final NodeList years = XmlHelper.getDescendantsByElementType(e, ElementType.YEAR);
        final NodeList months = XmlHelper.getDescendantsByElementType(e, ElementType.MONTH);
        final NodeList days = XmlHelper.getDescendantsByElementType(e, ElementType.DAY);

        final int numberOfYears = years.getLength();
        final int numberOfMonths = months.getLength();
        final int numberOfDays = days.getLength();

        if (numberOfYears == 0) return null;
        long year;
        final String yearStr = years.item(0).getTextContent();
        try {
            year = Long.parseLong(yearStr);
            if (year < 1900) {
                // we check this only for articles
                if (XmlHelper.isOfType(e.getParentNode(), ElementType.X)) {
                    return new CheckStatus("YEAR is less than 1900");
                }
            }
            if (year > s_now_year) {
                return new CheckStatus("YEAR is in the future");
            }
        } catch (@SuppressWarnings("unused") final NumberFormatException ex) {
            return new CheckStatus("YEAR (" + yearStr + ") is not an integer");
        }

        if (numberOfMonths == 0) {
            return null;
        }
        int month;
        final String monthStr = months.item(0).getTextContent();
        try {
            month = Integer.parseInt(monthStr);
            if (month < 0) {
                return new CheckStatus("MONTH is negative");
            }
            if (month > 12) {
                return new CheckStatus("MONTH is greater than 12");
            }
            if ((year == s_now_year) && (month > s_now_month)) {
                return new CheckStatus("YEAR/MONTH is in the future");
            }
        } catch (@SuppressWarnings("unused") final NumberFormatException ex) {
            return new CheckStatus("MONTH (" + monthStr + ") is not an integer");
        }

        if (numberOfDays == 0) {
            return null;
        }
        int day;
        final String dayStr = days.item(0).getTextContent();
        try {
            day = Integer.parseInt(dayStr);
            if (day < 0) {
                return new CheckStatus("DAY is negative");
            }
            if (day > 31) {
                return new CheckStatus("DAY is greater than 31");
            }
            if ((year == s_now_year) && (month == s_now_month) && (day > s_now_day))
                return new CheckStatus("YEAR/MONTH/DAY is in the future");
        } catch (@SuppressWarnings("unused") final NumberFormatException ex) {
            return new CheckStatus("DAY (" + dayStr + ") is not an integer");
        }

        final Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set((int)year,month-1,day);

        try {
            final int normalizedYear = calendar.get(Calendar.YEAR);
            final int normalizedMonth = calendar.get(Calendar.MONTH)+1;
            final int normalizedDay = calendar.get(Calendar.DAY_OF_MONTH);

            if ((year != normalizedYear) || (month != normalizedMonth) || (day != normalizedDay)) {
                return new CheckStatus("incorrect DATE");
            }
        } catch (@SuppressWarnings("unused") final IllegalArgumentException ex) {
            return new CheckStatus("incorrect DATE");
        }

        return null;
    }
}
