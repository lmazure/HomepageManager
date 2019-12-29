package data.jsongenerator2;

public class ParserAuthorDto2 {

    final private String a_namePrefix;
    final private String a_firstName;
    final private String a_middleName;
    final private String a_lastName;
    final private String a_nameSuffix;
    final private String a_givenName;
    
    /**
     * @param namePrefix
     * @param firstName
     * @param middleName
     * @param lastName
     * @param nameSuffix
     * @param givenName
     */
    public ParserAuthorDto2(final String namePrefix,
                           final String firstName,
                           final String middleName,
                           final String lastName,
                           final String nameSuffix,
                           final String givenName) {
        a_namePrefix = namePrefix;
        a_firstName = firstName;
        a_middleName = middleName;
        a_lastName = lastName;
        a_nameSuffix = nameSuffix;
        a_givenName = givenName;
    }

    /**
     * @return the namePrefix
     */
    public String getNamePrefix() {
        return a_namePrefix;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return a_firstName;
    }

    /**
     * @return the middleName
     */
    public String getMiddleName() {
        return a_middleName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return a_lastName;
    }

    /**
     * @return the nameSuffix
     */
    public String getNameSuffix() {
        return a_nameSuffix;
    }

    /**
     * @return the givenName
     */
    public String getGivenName() {
        return a_givenName;
    }
}
