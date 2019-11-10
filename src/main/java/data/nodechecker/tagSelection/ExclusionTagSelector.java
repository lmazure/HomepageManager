package data.nodechecker.tagSelection;


/**
 * @author Laurent
 *
 */
public class ExclusionTagSelector implements TagSelector {

	final String a_tags[];
	
	/**
	 * @param tagsToIgnore
	 */
	public ExclusionTagSelector(final String tagsToIgnore[]) {
		a_tags = tagsToIgnore;
	}
	
	@Override
	public boolean isTagCheckable(final String tag) {
		for (String t: a_tags) {
		    if ( t.equals(tag)) return(false); 
		}
		return true;
	}

}
