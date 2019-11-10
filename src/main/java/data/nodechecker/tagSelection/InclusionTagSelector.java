package data.nodechecker.tagSelection;


/**
 * @author Laurent
 *
 */
public class InclusionTagSelector implements TagSelector {

	final String a_tags[];
	
	/**
	 * @param tagsToCheck
	 */
	public InclusionTagSelector(final String tagsToCheck[]) {
		a_tags = tagsToCheck;
	}
	
	@Override
	public boolean isTagCheckable(final String tag) {
		for (String t: a_tags) {
		    if ( t.equals(tag)) return(true); 
		}
		return false;
	}

}
