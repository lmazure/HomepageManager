package data.nodechecker.checker.nodeChecker;

import java.util.Calendar;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.XMLHelper;
import utils.xmlparsing.NodeType;

public class DateChecker extends NodeChecker {

	final static Calendar s_now = Calendar.getInstance();
	final static int s_now_year = s_now.get(Calendar.YEAR);
	final static int s_now_month = s_now.get(Calendar.MONTH)+1;
	final static int s_now_day = s_now.get(Calendar.DAY_OF_MONTH);
		
	final static InclusionTagSelector s_selector = new InclusionTagSelector( new NodeType[] {
			NodeType.DATE
			} );

	@Override
	public TagSelector getTagSelector() {
		return s_selector;
	}

	@Override
	public NodeRule[] getRules() {
		final NodeRule a[]= new NodeRule[2];
		a[0] = new NodeRule() { @Override
		public CheckStatus checkElement(final Element e) { return checkDateHierarchy(e);}
		                    @Override
							public String getDescription() { return "the DATE components are incorrectly structured"; } };
		a[1] = new NodeRule() { @Override
		public CheckStatus checkElement(final Element e) { return checkDateValue(e);}
		                    @Override
							public String getDescription() { return "the DATE components have an incorrect value"; } };

        return a;
	}

	private CheckStatus checkDateHierarchy(final Element e) {
		
		final int numberOfYears = XMLHelper.getElementsByNodeType(e, NodeType.YEAR).getLength();
		final int numberOfMonths = XMLHelper.getElementsByNodeType(e, NodeType.MONTH).getLength();
		final int numberOfDays = XMLHelper.getElementsByNodeType(e, NodeType.DAY).getLength();
		
		if (numberOfYears > 1) return new CheckStatus("more than one YEAR");
		if (numberOfMonths > 1) return new CheckStatus("more than one MONTH");
		if (numberOfDays > 1) return new CheckStatus("more than one DAY");

		if (numberOfYears == 0) return new CheckStatus("no YEAR");

		if ((numberOfMonths == 0) && (numberOfDays == 1)) return new CheckStatus("DAY without MONTH");

		return null;						
	}

	private CheckStatus checkDateValue(final Element e) {
		
		final NodeList years = XMLHelper.getElementsByNodeType(e, NodeType.YEAR);
		final NodeList months = XMLHelper.getElementsByNodeType(e, NodeType.MONTH);
		final NodeList days = XMLHelper.getElementsByNodeType(e, NodeType.DAY);
		
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
				if ( e.getParentNode().getNodeName().equals(NodeType.X.toString()) ) {
					return new CheckStatus("YEAR is less than 1900");
				}
			}
			if (year > s_now_year) {
				return new CheckStatus("YEAR is in the future");
			}
		} catch (@SuppressWarnings("unused") final NumberFormatException ex) {
			return new CheckStatus("YEAR ("+ yearStr + ") is not an integer");
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
			if ( (year == s_now_year) && (month > s_now_month) ) {
				return new CheckStatus("YEAR/MONTH is in the future");
			}
		} catch (@SuppressWarnings("unused") final NumberFormatException ex) {
			return new CheckStatus("MONTH ("+ monthStr + ") is not an integer");
		}

		if (numberOfDays == 0) {
			return null;
		}
		int day;
		final String dayStr = days.item(0).getTextContent();
		try {
			day = Integer.parseInt(dayStr);
			if ( day<0 ) {
				return new CheckStatus("DAY is negative");
			}
			if ( day>31 ) {
				return new CheckStatus("DAY is greater than 31");
			}
			if ( (year==s_now_year) && (month==s_now_month) && (day>s_now_day))
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
			
			if ((year!= normalizedYear) || (month != normalizedMonth) || (day != normalizedDay)) {
				return new CheckStatus("incorrect DATE");
			}
		} catch (@SuppressWarnings("unused") final IllegalArgumentException ex) {
			return new CheckStatus("incorrect DATE");			
		}
		
		return null;
	}
}
