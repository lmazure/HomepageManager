package fr.mazure.homepagemanager.data.nodechecker.tagselection;

import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;

/**
 * Filter on the tags to be checked
 */
public interface TagSelector {

    /**
     * @param tag
     * @return is this tag verifiable by the checker?
     */
    boolean isTagCheckable(final ElementType tag);
}
