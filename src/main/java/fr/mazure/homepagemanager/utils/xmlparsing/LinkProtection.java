package fr.mazure.homepagemanager.utils.xmlparsing;

/**
 * Protection of a link
 */
public enum LinkProtection {
    /**
     * accessing the link requires a free account
     */
    FREE_REGISTRATION,
    /**
     * accessing the link requires a paid account
     */
    PAYED_REGISTRATION,
    /**
     * accessing the link does not require an account
     */
    NO_REQUIRED_REGISTRATION
}
