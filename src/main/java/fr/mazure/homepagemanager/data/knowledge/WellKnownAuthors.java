package fr.mazure.homepagemanager.data.knowledge;

import java.util.Optional;

import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * Records of the well-known authors
 */
public class WellKnownAuthors {

    @SuppressWarnings("javadoc") public static final AuthorData ALESSANDRO_ROUSSEL =   WellKnownAuthors.buildAuthor("Alessandro", "Roussel");
    @SuppressWarnings("javadoc") public static final AuthorData AYLIEAN_MACDONALD =    WellKnownAuthors.buildAuthor("Ayliean", "MacDonald");
    @SuppressWarnings("javadoc") public static final AuthorData BECKY_SMETHURST =      WellKnownAuthors.buildAuthor("Becky", "Smethurst");
    @SuppressWarnings("javadoc") public static final AuthorData BEN_SPARKS =           WellKnownAuthors.buildAuthor("Ben", "Sparks");
    @SuppressWarnings("javadoc") public static final AuthorData BENJAMIN_BRILLAUD =    WellKnownAuthors.buildAuthor("Benjamin", "Brillaud");
    @SuppressWarnings("javadoc") public static final AuthorData BRADY_HARAN =          WellKnownAuthors.buildAuthor("Brady", "Haran");
    @SuppressWarnings("javadoc") public static final AuthorData BURKARD_POLSTER =      WellKnownAuthors.buildAuthor("Burkard", "Polster");
    @SuppressWarnings("javadoc") public static final AuthorData CHRIS_ADAMS =          WellKnownAuthors.buildAuthor("Chris", "Adams");
    @SuppressWarnings("javadoc") public static final AuthorData DAVE_FARLEY =          WellKnownAuthors.buildAuthor("Dave", "Farley");
    @SuppressWarnings("javadoc") public static final AuthorData DAVID_KELLY =          WellKnownAuthors.buildAuthor("David", "Kelly");
    @SuppressWarnings("javadoc") public static final AuthorData DAVID_LOUAPRE =        WellKnownAuthors.buildAuthor("David", "Louapre");
    @SuppressWarnings("javadoc") public static final AuthorData DEREK_MULLER =         WellKnownAuthors.buildAuthor("Derek", "Muller");
    @SuppressWarnings("javadoc") public static final AuthorData JDMITRII_KOVANIKOV =   WellKnownAuthors.buildAuthor("Dmitrii", "Kovanikov");
    @SuppressWarnings("javadoc") public static final AuthorData GRANT_SANDERSON =      WellKnownAuthors.buildAuthor("Grant", "Sanderson");
    @SuppressWarnings("javadoc") public static final AuthorData GUNTER_ROTSAERT =      WellKnownAuthors.buildAuthor("Gunter", "Rotsaert");
    @SuppressWarnings("javadoc") public static final AuthorData JAMES_GRIME =          WellKnownAuthors.buildAuthor("James", "Grime");
    @SuppressWarnings("javadoc") public static final AuthorData JAMIE_ZAWINSKI =       WellKnownAuthors.buildAuthor("Jamie", "Zawinski");
    @SuppressWarnings("javadoc") public static final AuthorData JANNA_LEVIN =          WellKnownAuthors.buildAuthor("Janna", "Levin");
    @SuppressWarnings("javadoc") public static final AuthorData JASON_LAPERONNIE =     WellKnownAuthors.buildAuthor("Jason", "Lapeyronnie");
    @SuppressWarnings("javadoc") public static final AuthorData JEFF_DELANEY =         WellKnownAuthors.buildAuthor("Jeff", "Delaney");
    @SuppressWarnings("javadoc") public static final AuthorData JEROME_COTTANCEAU =    WellKnownAuthors.buildAuthor("Jérôme", "Cottanceau");
    @SuppressWarnings("javadoc") public static final AuthorData LEAH_STOCKLEY =        WellKnownAuthors.buildAuthor("Leah", "Stockley");
    @SuppressWarnings("javadoc") public static final AuthorData LEILA_BATTISON =       WellKnownAuthors.buildAuthor("Leila", "Battison");
    @SuppressWarnings("javadoc") public static final AuthorData LEX_FRIDMAN =          WellKnownAuthors.buildAuthor("Lex", "Fridman");
    @SuppressWarnings("javadoc") public static final AuthorData MATT_PARKER =          WellKnownAuthors.buildAuthor("Matt", "Parker");
    @SuppressWarnings("javadoc") public static final AuthorData MICHAEL_LAUNAY =       WellKnownAuthors.buildAuthor("Mickaël", "Launay");
    @SuppressWarnings("javadoc") public static final AuthorData MICHAEL_MERRIFIELD =   WellKnownAuthors.buildAuthor("Michael", "Merrifield");
    @SuppressWarnings("javadoc") public static final AuthorData NICOLAI_PARLOG =       WellKnownAuthors.buildAuthor("Nicolai", "Parlog");
    @SuppressWarnings("javadoc") public static final AuthorData PHILIPPE_GUGLIEMETTI = WellKnownAuthors.buildAuthor("Philippe", "Guglielmetti");
    @SuppressWarnings("javadoc") public static final AuthorData ROBERT_MILES =         WellKnownAuthors.buildAuthor("Robert", "Miles");
    @SuppressWarnings("javadoc") public static final AuthorData SAM_CHARRINGTON =      WellKnownAuthors.buildAuthor("Sam", "Charrington");
    @SuppressWarnings("javadoc") public static final AuthorData SIMON_WILLISON =       WellKnownAuthors.buildAuthor("Simon", "Willison");
    @SuppressWarnings("javadoc") public static final AuthorData STEPHANE_ROBERT =      WellKnownAuthors.buildAuthor("Stéphane", "Robert");
    @SuppressWarnings("javadoc") public static final AuthorData STEVEN_STROGATZ =      WellKnownAuthors.buildAuthor("Steven", "Strogatz");
    @SuppressWarnings("javadoc") public static final AuthorData TONY_PADILLA =         WellKnownAuthors.buildAuthor("Tony", "Padilla");
    @SuppressWarnings("javadoc") public static final AuthorData YONG_MOOK_KIM =        WellKnownAuthors.buildAuthor("Yong", "Mook Kim");

    /**
     * Create an author from a first and last names
     *
     * @param firstName first name
     * @param lastName last name
     * @return created author
     */
    public static AuthorData buildAuthor(final String firstName,
                                         final String lastName) {
        return new AuthorData(Optional.empty(),
                              Optional.of(firstName),
                              Optional.empty(),
                              Optional.of(lastName),
                              Optional.empty(),
                              Optional.empty());
    }

    /**
     * Create an author from a first, middle, and last names
     *
     * @param firstName first name
     * @param middleName middle name
     * @param lastName last name
     * @return created author
     */
    public static AuthorData buildAuthor(final String firstName,
                                         final String middleName,
                                         final String lastName) {
        return new AuthorData(Optional.empty(),
                              Optional.of(firstName),
                              Optional.of(middleName),
                              Optional.of(lastName),
                              Optional.empty(),
                              Optional.empty());
    }

   /**
    *  Create an author from a first, last, and given names
    *
    * @param firstName first name
    * @param lastName last name
    * @param givenName given name
    * @return created author
    */
    public static AuthorData buildAuthorWithGivenName(final String firstName,
                                                      final String lastName,
                                                      final String givenName) {
        return new AuthorData(Optional.empty(),
                              Optional.of(firstName),
                              Optional.empty(),
                              Optional.of(lastName),
                              Optional.empty(),
                              Optional.of(givenName));
    }

    /**
     *  Create an author from a given name
     *
     * @param givenName given name
     * @return created author
     */
    public static AuthorData buildAuthorFromGivenName(final String givenName) {
        return new AuthorData(Optional.empty(),
                              Optional.empty(),
                              Optional.empty(),
                              Optional.empty(),
                              Optional.empty(),
                              Optional.of(givenName));
    }

    /**
     *  Create an author from a first name
     *
     * @param firstName first name
     * @return created author
     */
    public static AuthorData buildAuthorFromFirstName(final String firstName) {
        return new AuthorData(Optional.empty(),
                              Optional.of(firstName),
                              Optional.empty(),
                              Optional.empty(),
                              Optional.empty(),
                              Optional.empty());
    }
}
