package data.nodechecker.tagSelection;

/**
 * @author Laurent
 *
 */
public interface TagSelector {

    /**
     * @param tag
     * @return is this tag verifiable by the checker?
     */
    boolean isTagCheckable(final String tag);
}
