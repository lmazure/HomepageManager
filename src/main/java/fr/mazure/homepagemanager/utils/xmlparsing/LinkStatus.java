package fr.mazure.homepagemanager.utils.xmlparsing;

/**
* Status of a link
*/
public enum LinkStatus {
    /**
     * the link is broken
     */
    DEAD,
    /**
     * the link is alive but its content is obsolete
     */
    OBSOLETE,
    /**
     *  the link is alive but its content is no more what it used to be
     */
    ZOMBIE,
    /**
     *  the link is alive and its content is what is expected
     */
    OK
}
